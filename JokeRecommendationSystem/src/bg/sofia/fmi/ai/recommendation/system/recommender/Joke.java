package bg.sofia.fmi.ai.recommendation.system.recommender;

import java.util.HashMap;
import java.util.Map;

public class Joke {
	private int id;
	private String text;
	private Map<Integer, Double> ratings; // userId -> rating

	public Joke(int id, String text) {
		this.id = id;
		this.text = text;
		ratings = new HashMap<Integer, Double>();
	}

	public int getId() {
		return id;
	}

	public String getText() {
		return text;
	}

	public void addRating(int userId, double rating) {
		ratings.put(userId, rating);
	}

	public Map<Integer, Double> getRatings() {
		return ratings;
	}

	public double calculateCosineSimilarity(Joke other, int usersNumber) {
		double dotProduct = 0.0;
		double firstNorm = 0.0;
		double secondNorm = 0.0;

		for (int userId = 0; userId < usersNumber; userId++) {
			double firstCoordinate = this.getRatings().containsKey(userId) ? this.getRatings().get(userId) : 0.0;
			double secondCoordinate = other.getRatings().containsKey(userId) ? other.getRatings().get(userId) : 0.0;

			dotProduct += firstCoordinate * secondCoordinate;
			firstNorm += Math.pow(firstCoordinate, 2);
			secondNorm += Math.pow(secondCoordinate, 2);
		}

		return dotProduct / (Math.sqrt(firstNorm) * Math.sqrt(secondNorm));
	}

}
