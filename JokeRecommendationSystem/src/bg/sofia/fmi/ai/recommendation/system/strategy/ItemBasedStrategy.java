package bg.sofia.fmi.ai.recommendation.system.strategy;

import java.util.List;
import java.util.stream.Collectors;

import bg.sofia.fmi.ai.recommendation.system.loader.Dataset;
import bg.sofia.fmi.ai.recommendation.system.recommender.Joke;
import bg.sofia.fmi.ai.recommendation.system.recommender.User;

public class ItemBasedStrategy implements RecommendationStrategy {
	private Dataset dataset;

	public ItemBasedStrategy(Dataset dataset) {
		this.dataset = dataset;
	}

	@Override
	public List<Pair<Joke, Double>> recommendNewJoke(User user, int jokesNumber) {

		List<Pair<Joke, Double>> recommendedJokes = dataset.getJokes()
				.stream()
				.filter(j -> user.getRatings().get(j.getId()) == null)
				.map(joke -> {
					List<Pair<Joke, Double>> jokeNearestNeighbors = findKNearestNeighbors(joke, NEIGHBORS_NUMBER);
					double rating = predictRatingForJoke(user, jokeNearestNeighbors);
					return new Pair<Joke, Double>(joke, rating);
				})
				.sorted((firstRating, secondRating) -> Double.compare(secondRating.getValue(), firstRating.getValue()))
				.limit(jokesNumber)
				.collect(Collectors.toList());

		return recommendedJokes;
	}

	@Override
	public List<Pair<Joke, Double>> recommendJoke(User user, int jokesNumber) {

		List<Pair<Joke, Double>> jokes = dataset.getJokes().stream().map(joke -> {
			List<Pair<Joke, Double>> jokeNearestNeighbors = findKNearestNeighbors(joke, NEIGHBORS_NUMBER);
			double rating = predictRatingForJoke(user, jokeNearestNeighbors);
			return new Pair<Joke, Double>(joke, rating);
		}).collect(Collectors.toList());
		List<Pair<Joke, Double>> recommendedJokes = jokes.stream()
				.sorted((firstRating, secondRating) -> Double.compare(secondRating.getValue(), firstRating.getValue()))
				.limit(jokesNumber)
				.collect(Collectors.toList());

		return recommendedJokes;
	}

	@Override
	public double predictRatingForJoke(User user, int jokeId) {
		Joke joke = dataset.getJokes().stream().filter(j -> j.getId() == jokeId).findFirst().get();
		List<Pair<Joke, Double>> jokeNearestNeighbors = findKNearestNeighbors(joke, NEIGHBORS_NUMBER);
		return predictRatingForJoke(user, jokeNearestNeighbors);
	}

	public double predictRatingForJoke(User user, List<Pair<Joke, Double>> jokeNearestNeighbors) {
		double weightedRatingsSum = 0.0;
		double weightsSum = 0.0;
		for (Pair<Joke, Double> neighbor : jokeNearestNeighbors) {
			Double rating = neighbor.getKey().getRatings().get(user.getId());

			// filter out the jokes that the current user has not rated
			if (rating != null) {
				double similarity = neighbor.getValue();
				weightedRatingsSum += rating * similarity;
				weightsSum += similarity;
			}
		}
		return weightedRatingsSum != 0.0 ? weightedRatingsSum / weightsSum : 0.0;
	}

	public List<Pair<Joke, Double>> findKNearestNeighbors(Joke joke, int nearestNeighborsNumber) {
		if (joke.getNearestNeighbors() != null) {
			return joke.getNearestNeighbors();
		}

		// calculate similarities
		List<Pair<Joke, Double>> similarities = calculateSimilarities(joke);

		// sort in descending orders
		List<Pair<Joke, Double>> nearestNeighbors = similarities.stream()
				.sorted((firstSimilarity, secondSimilarity) -> {
					return Double.compare(secondSimilarity.getValue(), firstSimilarity.getValue());
				})
				.limit(nearestNeighborsNumber)
				.collect(Collectors.toList());
		joke.setNearestNeighbors(nearestNeighbors);

		return nearestNeighbors;
	}

	private List<Pair<Joke, Double>> calculateSimilarities(Joke joke) {
		int usersNumber = dataset.getUsersNumber();

		return dataset.getJokes()
				.stream()
				.filter(j -> j.getId() != joke.getId())
				.map(neighbor -> new Pair<Joke, Double>(neighbor,
						joke.calculateCosineSimilarity(neighbor, usersNumber)))
				.collect(Collectors.toList());
	}

}
