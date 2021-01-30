package bg.sofia.uni.fmi.ai.bayes.classifier;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Dataset {
	List<String> trainingData;
	List<String> testData;

	public Dataset(List<String> data) {
		Collections.shuffle(data);

		int trainingDataLength = (int) (data.size() * 0.9);
		trainingData = data.stream().limit(trainingDataLength).collect(Collectors.toList());
		testData = data.stream().skip(trainingDataLength).collect(Collectors.toList());
	}

	public List<String> getTrainingData() {
		return trainingData;
	}

	public List<String> getTestData() {
		return testData;
	}
}
