package bg.sofia.fmi.ai.recommendation.system.recommender;

import java.util.HashMap;
import java.util.Map;

public class User {
	private int id;
	private Map<Integer, Double> ratings; // jokeId -> rating

	public User(int id) {
		this.id = id;
		ratings = new HashMap<Integer, Double>();
	}

	public void addRating(int jokeId, double rating) {
		ratings.put(jokeId, rating);
	}

	public Map<Integer, Double> getRatings() {
		return ratings;
	}

	public int getId() {
		return id;
	}

	public double calculateCosineSimilarity(User other, int jokesNumber) {
		return calculateCosineSimilarity(other.getRatings(), jokesNumber);
	}

	public double calculateCosineSimilarity(Map<Integer, Double> otherUserRatings, int jokesNumber) {
		double dotProduct = 0.0;
		double firstNorm = 0.0;
		double secondNorm = 0.0;

		for (int jokeId = 0; jokeId < jokesNumber; jokeId++) {
			double firstCoordinate = this.getRatings().containsKey(jokeId) ? this.getRatings().get(jokeId) : 0.0;
			double secondCoordinate = otherUserRatings.containsKey(jokeId) ? otherUserRatings.get(jokeId) : 0.0;

			dotProduct += firstCoordinate * secondCoordinate;
			firstNorm += Math.pow(firstCoordinate, 2);
			secondNorm += Math.pow(secondCoordinate, 2);
		}

		return dotProduct != 0.0 ? dotProduct / (Math.sqrt(firstNorm) * Math.sqrt(secondNorm)) : 0.0;
	}

	public User createPartialCopy() {
		User copiedUser = new User(this.id);

		// Copy half of the ratings
		ratings.entrySet().stream().limit(ratings.entrySet().size() / 2).forEach(entry -> {
			copiedUser.getRatings().put(entry.getKey(), entry.getValue());
		});

		return copiedUser;
	}
}
