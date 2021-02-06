package bg.sofia.fmi.ai.recommendation.system.strategy;

import java.util.List;
import java.util.stream.Collectors;

import bg.sofia.fmi.ai.recommendation.system.loader.Dataset;
import bg.sofia.fmi.ai.recommendation.system.recommender.Joke;
import bg.sofia.fmi.ai.recommendation.system.recommender.User;

public class UserBasedStrategy implements RecommendationStrategy {
	private Dataset dataset;

	public UserBasedStrategy(Dataset dataset) {
		this.dataset = dataset;
	}

	@Override
	public List<Pair<Joke, Double>> recommendNewJoke(User user, int jokesNumber) {
		List<Pair<User, Double>> neighbors = findKNearestNeighbors(user, NEIGHBORS_NUMBER);

		List<Pair<Joke, Double>> recommendedJokes = dataset.getJokes()
				.stream()
				.filter(j -> user.getRatings().get(j.getId()) == null)
				.map(joke -> {
					double rating = predictRatingForJoke(neighbors, joke.getId());
					return new Pair<Joke, Double>(joke, rating);
				})
				.sorted((firstRating, secondRating) -> Double.compare(secondRating.getValue(), firstRating.getValue()))
				.limit(jokesNumber)
				.collect(Collectors.toList());

		return recommendedJokes;
	}

	@Override
	public List<Pair<Joke, Double>> recommendJoke(User user, int jokesNumber) {
		List<Pair<User, Double>> neighbors = findKNearestNeighbors(user, NEIGHBORS_NUMBER);

		List<Pair<Joke, Double>> recommendedJokes = dataset.getJokes().stream().map(joke -> {
			double rating = predictRatingForJoke(neighbors, joke.getId());
			return new Pair<Joke, Double>(joke, rating);
		})
				.sorted((firstRating, secondRating) -> Double.compare(secondRating.getValue(), firstRating.getValue()))
				.limit(jokesNumber)
				.collect(Collectors.toList());

		return recommendedJokes;
	}

	@Override
	public double predictRatingForJoke(User user, int jokeId) {
		List<Pair<User, Double>> neighbors = findKNearestNeighbors(user, NEIGHBORS_NUMBER);
		return predictRatingForJoke(neighbors, jokeId);
	}

	public double predictRatingForJoke(List<Pair<User, Double>> nearestNeighbors, int jokeId) {
		double weightedRatingsSum = 0.0;
		double weightsSum = 0.0;
		for (Pair<User, Double> neighbor : nearestNeighbors) {
			Double rating = neighbor.getKey().getRatings().get(jokeId);
			if (rating != null) {
				double similarity = neighbor.getValue();
				weightedRatingsSum += rating * similarity;
				weightsSum += similarity;
			}
		}
		return weightedRatingsSum != 0.0 ? weightedRatingsSum / weightsSum : 0.0;
	}

	public List<Pair<User, Double>> findKNearestNeighbors(User user, int nearestNeighborsNumber) {

		// calculate cosine similarities;
		List<Pair<User, Double>> similarities = calculateSimilarities(user, dataset.getTrainingData());

		// sort in descending order
		List<Pair<User, Double>> nearestNeighbors = similarities.stream()
				.sorted((firstSimilarity, secondSimilarity) -> Double.compare(secondSimilarity.getValue(),
						firstSimilarity.getValue()))
				.limit(nearestNeighborsNumber)
				.collect(Collectors.toList());

		return nearestNeighbors;
	}

	protected List<Pair<User, Double>> calculateSimilarities(User user, List<User> users) {
		int jokesNumber = dataset.getJokesNumber();

		List<Pair<User, Double>> similarities = users.stream()
				.map(neighbor -> new Pair<User, Double>(neighbor,
						user.calculateCosineSimilarity(neighbor, jokesNumber)))
				.collect(Collectors.toList());
		return similarities;
	}

}
