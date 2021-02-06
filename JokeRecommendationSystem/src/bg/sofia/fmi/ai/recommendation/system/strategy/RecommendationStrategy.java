package bg.sofia.fmi.ai.recommendation.system.strategy;

import java.util.List;

import bg.sofia.fmi.ai.recommendation.system.recommender.Joke;
import bg.sofia.fmi.ai.recommendation.system.recommender.User;

public interface RecommendationStrategy {
	public static final int NEIGHBORS_NUMBER = 20;

	public List<Pair<Joke, Double>> recommendNewJoke(User user, int jokesNumber);

	public List<Pair<Joke, Double>> recommendJoke(User user, int jokesNumber);

	public double predictRatingForJoke(User user, int jokeId);
}
