package bg.sofia.fmi.ai.recommendation.system.recommender;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bg.sofia.fmi.ai.recommendation.system.loader.Dataset;
import bg.sofia.fmi.ai.recommendation.system.strategy.ItemBasedStrategy;
import bg.sofia.fmi.ai.recommendation.system.strategy.Pair;
import bg.sofia.fmi.ai.recommendation.system.strategy.RecommendationStrategy;
import bg.sofia.fmi.ai.recommendation.system.strategy.RecommendationStrategyType;
import bg.sofia.fmi.ai.recommendation.system.strategy.UserBasedStrategy;

public class Recommender {

	private Dataset dataset;
	private Map<RecommendationStrategyType, RecommendationStrategy> strategies;

	public Recommender(Dataset dataset) {
		this.dataset = dataset;
		strategies = new HashMap<RecommendationStrategyType, RecommendationStrategy>();
		strategies.put(RecommendationStrategyType.USER_BASED, new UserBasedStrategy(dataset));
		strategies.put(RecommendationStrategyType.ITEM_BASED, new ItemBasedStrategy(dataset));
	}

	public List<Pair<Joke, Double>> recommendJoke(User user, RecommendationStrategyType strategyType) {
		RecommendationStrategy strategy = strategies.get(strategyType);

		return strategy.recommendJoke(user);
	}

}
