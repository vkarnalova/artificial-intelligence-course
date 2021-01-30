package bg.sofia.uni.fmi.ai.id3.loader;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import bg.sofia.uni.fmi.ai.id3.Example;

public class Dataset {
	List<Example> trainingData;
	List<Example> testData;

	public Dataset(List<Example> data) {
		Collections.shuffle(data);

		int trainingDataLength = (int) (data.size() * 0.9);
		trainingData = data.stream().limit(trainingDataLength).collect(Collectors.toList());
		testData = data.stream().skip(trainingDataLength).collect(Collectors.toList());
	}

	public List<Example> getTrainingData() {
		return trainingData;
	}

	public List<Example> getTestData() {
		return testData;
	}
}
