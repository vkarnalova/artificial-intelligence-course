package bg.sofia.uni.fmi.ai.id3;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.OptionalDouble;

import bg.sofia.uni.fmi.ai.id3.loader.DatasetLoader;
import bg.sofia.uni.fmi.ai.id3.test.TestID3;

public class Main {

	public static void main(String[] args) {
		DatasetLoader loader = new DatasetLoader();
		List<Example> data = loader.load(Paths.get("resources" + File.separator + "breast-cancer.data"));

		TestID3 testID3 = new TestID3();
		List<Double> accuracies = testID3.calculateAccuracies(data, 10, 20, true);
		accuracies.stream().forEach(System.out::println);
		OptionalDouble average = accuracies.stream().mapToDouble(e -> e).average();
		System.out.println("Average: " + average.getAsDouble());

		System.out.println();
		accuracies = testID3.calculateAccuracies(data, 10, 25, true);
		accuracies.stream().forEach(System.out::println);
		average = accuracies.stream().mapToDouble(e -> e).average();
		System.out.println("Average: " + average.getAsDouble());
	}

}
