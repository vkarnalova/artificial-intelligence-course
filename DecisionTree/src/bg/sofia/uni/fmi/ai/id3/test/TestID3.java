package bg.sofia.uni.fmi.ai.id3.test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import bg.sofia.uni.fmi.ai.id3.Category;
import bg.sofia.uni.fmi.ai.id3.Example;
import bg.sofia.uni.fmi.ai.id3.ID3;
import bg.sofia.uni.fmi.ai.id3.loader.Dataset;

public class TestID3 {
	private ID3 id3 = new ID3();

	public List<Double> calculateAccuracies(List<Example> data, int retriesNumber, int minimumExamplesNumber,
			boolean stopSplitIfInfGainInsignificant) {
		List<Double> accuracies = new ArrayList<>();
		for (int i = 0; i < retriesNumber; i++) {
			List<Example> copiedData = deepCopyData(data);
			Dataset dataset = new Dataset(copiedData);
			id3.learn(dataset.getTrainingData(), minimumExamplesNumber, stopSplitIfInfGainInsignificant);

			accuracies.add(calculateAccuracy(dataset.getTestData()));
		}
		return accuracies;
	}

	private Double calculateAccuracy(List<Example> testData) {
		int correctClassification = 0;
		for (Example example : testData) {
			Category category = id3.classify(example);
			if (example.getCategory().equals(category)) {
				correctClassification++;
			}
		}

		return (double) correctClassification / testData.size();
	}

	private List<Example> deepCopyData(List<Example> data) {
		return data.stream().map(e -> e.deepCopy()).collect(Collectors.toList());
	}
}
