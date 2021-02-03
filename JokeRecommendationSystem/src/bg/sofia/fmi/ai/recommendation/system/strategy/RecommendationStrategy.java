package bg.sofia.fmi.ai.recommendation.system.strategy;

import java.util.List;

import bg.sofia.fmi.ai.recommendation.system.recommender.Joke;
import bg.sofia.fmi.ai.recommendation.system.recommender.User;

public interface RecommendationStrategy {

	public List<Pair<Joke, Double>> recommendJoke(User user);
}
