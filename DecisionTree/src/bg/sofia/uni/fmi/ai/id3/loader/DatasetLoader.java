package bg.sofia.uni.fmi.ai.id3.loader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import bg.sofia.uni.fmi.ai.id3.Category;
import bg.sofia.uni.fmi.ai.id3.Example;
import bg.sofia.uni.fmi.ai.id3.Feature;
import bg.sofia.uni.fmi.ai.id3.FeatureName;

public class DatasetLoader {
	public List<Example> load(Path path) {
		List<Example> examples = new ArrayList<>();

		try (Scanner scanner = new Scanner(path)) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine().trim();
				if (!line.isEmpty()) {
					examples.add(getExample(line));
				}
			}
		} catch (IOException e) {
			throw new IllegalStateException("Unable to load dataset from path " + path.getFileName());
		}

		return examples;
	}

	private Example getExample(String line) {
		String[] split = line.split(",");
		List<Feature> features = new ArrayList<>();
		for (int i = 0; i < FeatureName.values().length; i++) {
			features.add(new Feature(FeatureName.values()[i], split[i + 1]));
		}

		Category category = split[0].equals("recurrence-events") ? Category.RECURRENCE_EVENTS
				: Category.NO_RECURRENCE_EVENTS;

		return new Example(category, features);
	}
}
