package bg.sofia.fmi.ai.recommendation.system.clustering;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import bg.sofia.fmi.ai.recommendation.system.recommender.User;

public class KMeans {
	private static final Random random = new Random();

	public ClustersGroup calculateClusters(List<User> users, int jokesNumber, int clustersNumber, int maxIterations) {
		List<Centroid> centroids = generateRandomCentroids(users, jokesNumber, clustersNumber);
		ClustersGroup currentClusters = new ClustersGroup();
		currentClusters.addClusters(centroids);
		ClustersGroup previousClusters = null;
		ClustersGroup bestClusters = currentClusters;

		while (maxIterations > 0) {
			for (User user : users) {
				Centroid nearestCentroid = findNearestCentroid(user, centroids, jokesNumber);
				currentClusters.addUserToCluster(user, nearestCentroid);
			}

			// if the clusters haven't change -> restart
			if (currentClusters.equals(previousClusters)) {
				centroids = generateRandomCentroids(users, jokesNumber, clustersNumber);
				currentClusters = new ClustersGroup();
				currentClusters.addClusters(centroids);
			}

			previousClusters = currentClusters;

			if (bestClusters.getCost() > currentClusters.getCost()) {
				bestClusters = currentClusters;
			}

			centroids = moveCentroids(currentClusters);
			currentClusters = new ClustersGroup();
			currentClusters.addClusters(centroids);

			maxIterations--;
		}

		return bestClusters;
	}

	public List<Centroid> moveCentroids(ClustersGroup clusters) {
		return clusters.getClusters()
				.entrySet()
				.stream()
				.map(e -> moveCentroid(e.getKey(), e.getValue()))
				.collect(Collectors.toList());
	}

	public Centroid moveCentroid(Centroid centroid, List<User> users) {
		Centroid movedCentroid = new Centroid();
		for (Map.Entry<Integer, Double> entry : centroid.getRatings().entrySet()) {

			// calculate average rating for joke
			double sumRating = 0.0;
			double usersNumber = 0.0;
			for (User user : users) {
				int jokeId = entry.getKey();
				if (user.getRatings().containsKey(jokeId)) {
					usersNumber++;
					sumRating += user.getRatings().get(jokeId);
				}
			}

			double average = sumRating == 0.0 ? 0.0 : sumRating / usersNumber;
			movedCentroid.getRatings().put(entry.getKey(), average);
		}

		return movedCentroid;
	}

	public Centroid findNearestCentroid(User user, List<Centroid> centroids, int jokesNumber) {
		double maxSimilarity = Double.MIN_VALUE;
		Centroid nearestCentroid = null;

		for (Centroid centroid : centroids) {
			double currentSimilarity = user.calculateCosineSimilarity(centroid.getRatings(), jokesNumber);
			if (currentSimilarity > maxSimilarity) {
				maxSimilarity = currentSimilarity;
				nearestCentroid = centroid;
			}
		}

		return nearestCentroid != null ? nearestCentroid : centroids.get(0);
	}

	public List<Centroid> generateRandomCentroids(List<User> users, int jokesNumber, int clustersNumber) {
		// Get the min and max ratings that have been given
		double minRating = 20.0;
		double maxRating = 0.0;

		maxRating = users.stream()
				.flatMapToDouble(user -> user.getRatings().values().stream().mapToDouble(r -> r))
				.max()
				.getAsDouble();
		minRating = users.stream()
				.flatMapToDouble(user -> user.getRatings().values().stream().mapToDouble(r -> r))
				.min()
				.getAsDouble();

		List<Centroid> centroids = new ArrayList<>();
		for (int clusterIndex = 0; clusterIndex < clustersNumber; clusterIndex++) {
			Centroid currentCentroid = new Centroid();
			for (int jokeId = 0; jokeId < jokesNumber; jokeId++) {

				// Generate values between min and max ratings
				double randomRating = random.nextDouble() * (maxRating - minRating) + minRating;
				double roundRandomRating = new BigDecimal(randomRating).setScale(2, RoundingMode.HALF_UP).doubleValue();
				currentCentroid.getRatings().put(jokeId, roundRandomRating);
			}
			centroids.add(currentCentroid);
		}

		return centroids;
	}
}
