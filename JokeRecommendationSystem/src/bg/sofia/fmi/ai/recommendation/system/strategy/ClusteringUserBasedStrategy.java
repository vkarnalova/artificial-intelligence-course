package bg.sofia.fmi.ai.recommendation.system.strategy;

import java.util.List;
import java.util.stream.Collectors;

import bg.sofia.fmi.ai.recommendation.system.clustering.Centroid;
import bg.sofia.fmi.ai.recommendation.system.clustering.ClustersGroup;
import bg.sofia.fmi.ai.recommendation.system.clustering.KMeans;
import bg.sofia.fmi.ai.recommendation.system.loader.Dataset;
import bg.sofia.fmi.ai.recommendation.system.recommender.Joke;
import bg.sofia.fmi.ai.recommendation.system.recommender.User;

public class ClusteringUserBasedStrategy extends UserBasedStrategy {
	private Dataset dataset;
	private ClustersGroup clusters;

	public ClusteringUserBasedStrategy(Dataset dataset) {
		super(dataset);
		this.dataset = dataset;
		KMeans kMeans = new KMeans();
		clusters = kMeans.calculateClusters(dataset.getTrainingData(), dataset.getJokesNumber(), 4, 60);
	}

	@Override
	public List<Pair<Joke, Double>> recommendNewJoke(User user, int jokesNumber) {
		Centroid centroid = clusters.findClusterForUser(user, dataset.getJokesNumber());
		List<User> usersInCluster = clusters.getClusters().get(centroid);
		List<Pair<User, Double>> neighbors = findKNearestNeighbors(user, usersInCluster, NEIGHBORS_NUMBER);

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
		Centroid centroid = clusters.findClusterForUser(user, dataset.getJokesNumber());
		List<User> usersInCluster = clusters.getClusters().get(centroid);
		List<Pair<User, Double>> neighbors = findKNearestNeighbors(user, usersInCluster, NEIGHBORS_NUMBER);

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
		Centroid centroid = clusters.findClusterForUser(user, dataset.getJokesNumber());
		List<User> usersInCluster = clusters.getClusters().get(centroid);
		List<Pair<User, Double>> neighbors = findKNearestNeighbors(user, usersInCluster, NEIGHBORS_NUMBER);
		return predictRatingForJoke(neighbors, jokeId);
	}

	public List<Pair<User, Double>> findKNearestNeighbors(User user, List<User> usersInCluster,
			int nearestNeighborsNumber) {

		// calculate cosine similarities;
		List<Pair<User, Double>> similarities = calculateSimilarities(user, usersInCluster);

		// sort in descending order
		List<Pair<User, Double>> nearestNeighbors = similarities.stream()
				.sorted((firstSimilarity, secondSimilarity) -> Double.compare(secondSimilarity.getValue(),
						firstSimilarity.getValue()))
				.limit(nearestNeighborsNumber)
				.collect(Collectors.toList());

		return nearestNeighbors;
	}

}
