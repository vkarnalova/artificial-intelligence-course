package bg.sofia.uni.fmi.ai.bayes.classifier;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DataLoader {

	public List<String> load(Path path) {
		List<String> lines = new ArrayList<>();

		try (Scanner scanner = new Scanner(path)) {
			while (scanner.hasNextLine()) {
				String trimLine = scanner.nextLine().trim();
				if (!trimLine.isEmpty()) {
					lines.add(trimLine);
				}
			}
		} catch (IOException e) {
			throw new IllegalStateException("Unable to load dataset from path " + path.getFileName());
		}

		return lines;
	}
}
