package bg.sofia.uni.fmi.ai.bayes.classifier;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.OptionalDouble;

public class Main {

	public static void main(String[] args) {
		DataLoader loader = new DataLoader();
		List<String> data = loader.load(Paths.get("resources" + File.separator + "house-votes-84.data"));

		TestNaiveBayesClassifier testNaiveBayesClassifier = new TestNaiveBayesClassifier();
		List<Double> accuracies = testNaiveBayesClassifier.calculateAccuracies(data, 10);
		accuracies.stream().forEach(System.out::println);
		OptionalDouble average = accuracies.stream().mapToDouble(e -> e).average();
		System.out.print("Average: " + average.getAsDouble());
	}

}
