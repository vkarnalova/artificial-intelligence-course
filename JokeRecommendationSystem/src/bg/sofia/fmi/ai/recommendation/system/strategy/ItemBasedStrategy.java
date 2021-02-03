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
	public List<Pair<Joke, Double>> recommendJoke(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Joke> findKNearestNeighbors(Joke joke, int nearestNeogborsNumber) {
		int jokesNumber = dataset.getJokesNumber();

		// sort in descending orders
		List<Joke> sorted = dataset.getJokes()
				.stream()
				.filter(j -> j.getId() == joke.getId())
				.sorted((firstJoke, secondJoke) -> {
					return Double.compare(joke.calculateCosineSimilarity(firstJoke, jokesNumber),
							joke.calculateCosineSimilarity(secondJoke, jokesNumber));
				})
				.collect(Collectors.toList());

		return sorted.stream().limit(nearestNeogborsNumber).collect(Collectors.toList());
	}

	public double predictRatingForJoke(User user, List<Joke> nearestNeighbors, int jokeId) {
		// filter out the jokes that the user has not rated
		List<Joke> ratedNeighbors = nearestNeighbors.stream()
				.filter(joke -> user.getRatings().containsKey(joke.getId()))
				.collect(Collectors.toList());

		double sumRatings = ratedNeighbors.stream().mapToDouble(joke -> joke.getRatings().get(user.getId())).sum();
		return sumRatings / ratedNeighbors.size();
	}

}
