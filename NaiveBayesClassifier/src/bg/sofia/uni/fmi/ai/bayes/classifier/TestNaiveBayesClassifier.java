package bg.sofia.uni.fmi.ai.bayes.classifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestNaiveBayesClassifier {
	private NaiveBayesClassifier classifier = new NaiveBayesClassifier();

	public List<Double> calculateAccuracies(List<String> data, int retriesNumber) {
		List<Double> accuracies = new ArrayList<>();
		for (int i = 0; i < retriesNumber; i++) {
			Dataset dataset = new Dataset(data);
			classifier.learn(dataset.getTrainingData());

			accuracies.add(calculateAccuracy(dataset.getTestData()));
		}
		return accuracies;
	}

	public Double calculateAccuracy(List<String> testData) {
		int correctClassification = 0;
		for (String testLine : testData) {
			String[] splitTestLine = testLine.split(",");
			String[] lineFeatures = Arrays.copyOfRange(splitTestLine, 1, splitTestLine.length);
			Category category = classifier.categorise(lineFeatures);
			if (category.toString().equals(splitTestLine[0].toUpperCase())) {
				correctClassification++;
			}
		}

		return (double) correctClassification / testData.size();
	}
}
