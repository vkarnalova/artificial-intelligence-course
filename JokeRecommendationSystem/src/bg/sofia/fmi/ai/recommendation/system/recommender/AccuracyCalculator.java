package bg.sofia.fmi.ai.recommendation.system.recommender;

import java.util.List;
import java.util.stream.Collectors;

public class AccuracyCalculator {
	public double calculateAccuracy(List<User> testData) {
		List<User> partialCopy = testData.stream().map(user -> user.createPartialCopy()).collect(Collectors.toList());

		return 0.0;
	}
}
