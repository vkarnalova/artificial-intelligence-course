package bg.sofia.fmi.ai.recommendation.system.loader;

import java.util.List;
import java.util.stream.Collectors;

import bg.sofia.fmi.ai.recommendation.system.recommender.Joke;
import bg.sofia.fmi.ai.recommendation.system.recommender.User;

public class Dataset {
	private List<User> trainingData;
	private List<User> testData;
	private List<Joke> jokes;
	private int jokesNumber;

	public Dataset(List<User> data, List<Joke> jokes) {
		// @VK TODO we want to shuffle the data
		// Collections.shuffle(data);

		int trainingDataLength = (int) (data.size() * 0.9);
		trainingData = data.stream().limit(trainingDataLength).collect(Collectors.toList());
		testData = data.stream().skip(trainingDataLength).collect(Collectors.toList());
		this.jokes = jokes;
		jokesNumber = jokes.size();
	}

	public List<User> getTrainingData() {
		return trainingData;
	}

	public List<User> getTestData() {
		return testData;
	}

	public List<Joke> getJokes() {
		return jokes;
	}

	public int getJokesNumber() {
		return jokesNumber;
	}
}
