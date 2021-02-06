package bg.sofia.fmi.ai.recommendation.system.recommender;

import java.util.List;
import java.util.Map;

import bg.sofia.fmi.ai.recommendation.system.loader.Dataset;
import bg.sofia.fmi.ai.recommendation.system.strategy.ClusteringUserBasedStrategy;
import bg.sofia.fmi.ai.recommendation.system.strategy.ItemBasedStrategy;
import bg.sofia.fmi.ai.recommendation.system.strategy.Pair;
import bg.sofia.fmi.ai.recommendation.system.strategy.RecommendationStrategy;
import bg.sofia.fmi.ai.recommendation.system.strategy.UserBasedStrategy;

public class RecommenderTest {
	public void calculateHitRate(Dataset dataset) {
		System.out.println("Item based strategy:");
		double hitRate = calculateHitRate(dataset.getTestData(), new ItemBasedStrategy(dataset));
		System.out.println("Hit rate: " + hitRate);

		System.out.println("User based strategy:");
		hitRate = calculateHitRate(dataset.getTestData(), new UserBasedStrategy(dataset));
		System.out.println("Hit rate: " + hitRate);

		System.out.println("User based strategy with clustering:");
		hitRate = calculateHitRate(dataset.getTestData(), new ClusteringUserBasedStrategy(dataset));
		System.out.println("Hit rate: " + hitRate);
	}

	private double calculateHitRate(List<User> testData, RecommendationStrategy strategy) {
		double hits = 0.0;
		for (User user : testData) {
			List<Pair<Joke, Double>> recommendedJokes = strategy.recommendJoke(user, 20);
			hits += recommendedJokes.stream()
					.mapToInt(joke -> user.getRatings().containsKey(joke.getKey().getId()) ? 1 : 0)
					.sum();
		}

		return hits / (double) testData.size();
	}

	public void calculateRMSE(Dataset dataset) {
		System.out.println("Item based strategy:");
		double rmse = calculateRMSE(dataset.getTestData(), new ItemBasedStrategy(dataset));
		System.out.println("RMSE: " + rmse);

		System.out.println("User based strategy:");
		rmse = calculateRMSE(dataset.getTestData(), new UserBasedStrategy(dataset));
		System.out.println("RMSE: " + rmse);

		System.out.println("User based strategy with clustering:");
		rmse = calculateRMSE(dataset.getTestData(), new ClusteringUserBasedStrategy(dataset));
		System.out.println("RMSE: " + rmse);
	}

	private double calculateRMSE(List<User> testData, RecommendationStrategy strategy) {
		double sumError = 0.0;
		double ratedJokes = 0.0;
		for (User testUser : testData) {
			User partialCopy = testUser.createPartialCopy();

			// find the ratings of all jokes that the user has rated that are not in the
			// partial copy
			for (Map.Entry<Integer, Double> entry : testUser.getRatings().entrySet()) {
				if (!partialCopy.getRatings().containsKey(entry.getKey())) {
					double predictedRating = strategy.predictRatingForJoke(partialCopy, entry.getKey());
					double actualRating = entry.getValue();
					sumError += Math.pow(predictedRating - actualRating, 2);
					ratedJokes++;
				}
			}
		}
		return Math.sqrt(sumError / ratedJokes);
	}
}
