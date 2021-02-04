package bg.sofia.fmi.ai.recommendation.system.clustering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bg.sofia.fmi.ai.recommendation.system.recommender.User;

public class ClustersGroup {
	private Map<Centroid, List<User>> clusters;
	private Double cost;

	public ClustersGroup() {
		clusters = new HashMap<Centroid, List<User>>();
	}

	public Map<Centroid, List<User>> getClusters() {
		return clusters;
	}

	public void addClusters(List<Centroid> centroids) {
		for (Centroid centroid : centroids) {
			clusters.put(centroid, new ArrayList<>());
		}
	}

	public void addUserToCluster(User user, Centroid centroid) {
		List<User> users = clusters.get(centroid);
		if (users == null) {
			// This should never happen
			throw new IllegalStateException("Centroid was not initialized.");
		}

		users.add(user);
		clusters.put(centroid, users);
	}

	public double getCost() {
		if (cost == null) {
			cost = evaluateCost();
		}

		return cost;
	}

	private double evaluateCost() {
		double cost = 0.0;
		for (Centroid centroid : clusters.keySet()) {
			// calculate the sum of the distances between the centroid and each user
			double sum = clusters.get(centroid)
					.stream()
					.mapToDouble(
							user -> user.calculateCosineSimilarity(centroid.getRatings(), centroid.getRatings().size()))
					.sum();
			cost += clusters.get(centroid).size() * sum;
		}

		return cost;
	}
}
