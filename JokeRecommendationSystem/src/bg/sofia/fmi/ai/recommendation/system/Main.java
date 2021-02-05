package bg.sofia.fmi.ai.recommendation.system;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

import bg.sofia.fmi.ai.recommendation.system.loader.Dataset;
import bg.sofia.fmi.ai.recommendation.system.loader.DatasetLoader;
import bg.sofia.fmi.ai.recommendation.system.recommender.Joke;
import bg.sofia.fmi.ai.recommendation.system.recommender.Recommender;
import bg.sofia.fmi.ai.recommendation.system.recommender.User;
import bg.sofia.fmi.ai.recommendation.system.strategy.Pair;
import bg.sofia.fmi.ai.recommendation.system.strategy.RecommendationStrategyType;

public class Main {
	public static void main(String[] args) {
		DatasetLoader loader = new DatasetLoader();
		Dataset dataset = loader.load(Paths.get("resources" + File.separator + "JokesRatings.csv"),
				Paths.get("resources" + File.separator + "JokesSet.csv"));

		Recommender recommender = new Recommender(dataset);

		try (Scanner scanner = new Scanner(System.in)) {
			System.out.print("Type 1 if you want to choose a user, type 2 if you want to run automatic tests ");
			String mode = scanner.nextLine().trim();
			if (mode.equals("1")) {
				System.out.print("Test users ids: ");
				String usersId = dataset.getTestData()
						.stream()
						.map(u -> String.valueOf(u.getId()))
						.collect(Collectors.joining(","));
				System.out.println(usersId);
				System.out.print("Type the id of the user that you want to get a joke recommended: ");
				int userId = scanner.nextInt();
				Optional<User> user = dataset.getTestData().stream().filter(u -> u.getId() == userId).findFirst();
				if (user.isPresent()) {
					System.out.println("1-User based filtering");
					System.out.println("2-Item based filtering");
					System.out.println("3-User based filtering with clustering");
					System.out.println("Type the number of the strategy: ");
					RecommendationStrategyType strategy = getStrategy(scanner.nextInt());
					if (strategy != null) {
						List<Pair<Joke, Double>> recommendJoke = recommender.recommendJoke(user.get(), strategy);
						recommendJoke.stream()
								.forEach(recemmendation -> System.out
										.println(recemmendation.getKey().getText() + " " + recemmendation.getValue()));
					} else {
						System.out.println("Invalid strategy.");
					}
				} else {
					System.out.println("Invalid id.");
				}

			} else if (mode.equals("2")) {
				//
			} else {
				System.out.println("Invalid input");
			}
		}
	}

	private static RecommendationStrategyType getStrategy(int strategyId) {
		switch (strategyId) {
			case 1:
				return RecommendationStrategyType.USER_BASED;
			case 2:
				return RecommendationStrategyType.ITEM_BASED;
			case 3:
				return RecommendationStrategyType.USER_BASED_AND_CLUSTERING;
			default:
				return null;
		}
	}
}
