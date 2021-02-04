package bg.sofia.fmi.ai.recommendation.system;

import java.io.File;
import java.nio.file.Paths;

import bg.sofia.fmi.ai.recommendation.system.clustering.ClustersGroup;
import bg.sofia.fmi.ai.recommendation.system.clustering.KMeans;
import bg.sofia.fmi.ai.recommendation.system.loader.Dataset;
import bg.sofia.fmi.ai.recommendation.system.loader.DatasetLoader;
import bg.sofia.fmi.ai.recommendation.system.recommender.Recommender;

public class Main {
	public static void main(String[] args) {
		DatasetLoader loader = new DatasetLoader();
		Dataset dataset = loader.load(Paths.get("resources" + File.separator + "JokesRatings.csv"),
				Paths.get("resources" + File.separator + "JokesSet.csv"));

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

//		ItemBasedStrategy strategy = new ItemBasedStrategy(dataset);
//		List<Pair<Joke, Double>> neighbors = strategy.findKNearestNeighbors(dataset.getJokes().get(1), 2);
//		neighbors.stream().forEach(n -> System.out.println(n.getKey().getId() + " " + n.getValue()));
//		List<Pair<Joke, Double>> recommendedJokes = strategy.recommendJoke(dataset.getTestData().get(0));
//		recommendedJokes.stream().forEach(joke -> System.out.println(joke.getKey().getId() + " " + joke.getValue()));

		KMeans kMeans = new KMeans();
		for (int clustersNumber = 1; clustersNumber < 2; clustersNumber++) {
			ClustersGroup clusters = kMeans.calculateClusters(dataset.getTrainingData(), dataset.getJokesNumber(),
					clustersNumber, 60);
			System.out.println(clustersNumber + " " + clusters.getCost());
		}

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
