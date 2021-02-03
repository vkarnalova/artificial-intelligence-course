package bg.sofia.fmi.ai.recommendation.system;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

import bg.sofia.fmi.ai.recommendation.system.loader.Dataset;
import bg.sofia.fmi.ai.recommendation.system.loader.DatasetLoader;
import bg.sofia.fmi.ai.recommendation.system.recommender.Joke;
import bg.sofia.fmi.ai.recommendation.system.recommender.Recommender;
import bg.sofia.fmi.ai.recommendation.system.strategy.ItemBasedStrategy;
import bg.sofia.fmi.ai.recommendation.system.strategy.Pair;

public class Main {
	public static void main(String[] args) {
		DatasetLoader loader = new DatasetLoader();
		Dataset dataset = loader.load(Paths.get("resources" + File.separator + "JokesRatings-small.csv"),
				Paths.get("resources" + File.separator + "JokesSet-small.csv"));

		Recommender recommender = new Recommender(dataset);
//		List<Pair<Joke, Double>> jokes = recommender.recommendJoke(dataset.getTestData().get(0),
//				RecommendationStrategyType.USER_BASED);
//		jokes.stream().forEach(joke -> System.out.println(joke.getKey().getId() + " " + joke.getValue()));
//		UserBasedStrategy strategy = new UserBasedStrategy(dataset);
//		List<Pair<User, Double>> neighbors = strategy.findKNearestNeighbors(dataset.getTestData().get(0), 2);
//		neighbors.stream().forEach(n -> System.out.println(n.getKey().getId() + " " + n.getValue()));
//		System.out.println(strategy.predictRatingForJoke(neighbors, 0));
//		List<Pair<Joke, Double>> recommendedJokes = strategy.recommendJoke(dataset.getTestData().get(0));
//		recommendedJokes.stream().forEach(joke -> System.out.println(joke.getKey().getId() + " " + joke.getValue()));

		ItemBasedStrategy strategy = new ItemBasedStrategy(dataset);
		List<Pair<Joke, Double>> neighbors = strategy.findKNearestNeighbors(dataset.getJokes().get(1), 2);
		neighbors.stream().forEach(n -> System.out.println(n.getKey().getId() + " " + n.getValue()));
		List<Pair<Joke, Double>> recommendedJokes = strategy.recommendJoke(dataset.getTestData().get(0));
		recommendedJokes.stream().forEach(joke -> System.out.println(joke.getKey().getId() + " " + joke.getValue()));

//		System.out.println(dataset.getJokes().get(0).calculateCosineSimilarity(dataset.getJokes().get(1), 5));

//		System.out.println(strategy.predictRatingForJoke(neighbors, 0));
//		List<Pair<Joke, Double>> recommendedJokes = strategy.recommendJoke(dataset.getTestData().get(0));
//		recommendedJokes.stream().forEach(joke -> System.out.println(joke.getKey().getId() + " " + joke.getValue()));

//		RecommendationStrategy<User> strategy = new UserBasedStrategy(dataset);
//		List<User> neighbors = strategy.findKNearestNeighbors(dataset.getTestData().get(0), 2);
//		System.out.println(dataset.getTestData().get(0).getId());
//		neighbors.stream().forEach(user -> System.out.println(user.getId()));
//		System.out.println(strategy.predictRatingForJoke(dataset.getTrainingData(), 0));
//		System.out.println(recommender.recommendJoke(dataset.getTestData().get(0)).get(0));
	}
}
