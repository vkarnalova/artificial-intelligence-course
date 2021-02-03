package bg.sofia.fmi.ai.recommendation.system.loader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import bg.sofia.fmi.ai.recommendation.system.recommender.Joke;
import bg.sofia.fmi.ai.recommendation.system.recommender.User;

public class DatasetLoader {
	public Dataset load(Path ratingsFilePath, Path jokesFilePath) {
		List<Joke> jokes = loadJokesData(jokesFilePath);
		List<User> userRatings = loadRatingsData(ratingsFilePath, jokes);

		return new Dataset(userRatings, jokes);
	}

	private List<User> loadRatingsData(Path ratingsFilePath, List<Joke> jokes) {
		List<User> users = new ArrayList<>();

		try (Scanner scanner = new Scanner(ratingsFilePath)) {
			int userId = 0;
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine().trim();
				if (!line.isEmpty()) {
					users.add(getUser(line, userId, jokes));
					userId++;
				}
			}
		} catch (IOException e) {
			throw new IllegalStateException("Unable to load dataset from path " + ratingsFilePath.getFileName());
		}

		return users;
	}

	private User getUser(String line, int userId, List<Joke> jokes) {
		User user = new User(userId);

		String[] split = line.split(","); // The first element is for # of jokes rated by this user
		for (int jokeId = 0; jokeId < split.length - 1; jokeId++) {
			Double rating = Double.parseDouble(split[jokeId + 1]);
			if (rating != 99) {
				// @VK TODO maybe 10 should be a constant somewhere
				user.addRating(jokeId, rating + 10);
				int currentJokeId = jokeId;
				Joke joke = jokes.stream().filter(j -> j.getId() == currentJokeId).findFirst().get();
				joke.addRating(userId, rating + 10);
			}
		}

		return user;
	}

	private List<Joke> loadJokesData(Path jokesFilePath) {
		List<Joke> jokes = new ArrayList<>();

		try (Scanner scanner = new Scanner(jokesFilePath)) {
			int jokeId = 0;
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine().trim();
				if (!line.isEmpty()) {
					jokes.add(new Joke(jokeId, line));
					jokeId++;
				}
			}
		} catch (IOException e) {
			throw new IllegalStateException("Unable to load dataset from path " + jokesFilePath.getFileName());
		}

		return jokes;
	}
}
