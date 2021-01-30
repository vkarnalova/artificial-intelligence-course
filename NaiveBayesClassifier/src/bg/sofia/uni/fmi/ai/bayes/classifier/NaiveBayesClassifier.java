package bg.sofia.uni.fmi.ai.bayes.classifier;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class NaiveBayesClassifier {
	private Map<Feature, Map<Category, Double>> featureLogProbabilities;
	private Map<Category, Double> categoryLogProbabilities;

	public void learn(List<String> trainingData) {
		Map<Feature, Map<Category, Integer>> occurrences = new HashMap<>();

		Iterator<String> iterator = trainingData.iterator();

		int democratsCounter = 0;
		int republicansCounter = 0;
		while (iterator.hasNext()) {
			String line = iterator.next();
			String[] split = line.split(",");

			Category category = null;
			if (split[0].equals("republican")) {
				category = Category.REPUBLICAN;
				republicansCounter++;
			} else {
				category = Category.DEMOCRAT;
				democratsCounter++;
			}

			addFeaturesForLine(line, category, occurrences);
		}

		createFeatureLogProbabilities(occurrences, democratsCounter, republicansCounter);
		createCategoryLogProbabilities(democratsCounter, republicansCounter);
	}

	private void createCategoryLogProbabilities(int democratsCounter, int republicansCounter) {
		categoryLogProbabilities = new HashMap<>();
		int all = democratsCounter + republicansCounter;
		categoryLogProbabilities.put(Category.DEMOCRAT, Math.log((double) (democratsCounter + 1) / (all + 2)));
		categoryLogProbabilities.put(Category.REPUBLICAN, Math.log((double) (republicansCounter + 1) / (all + 2)));
	}

	public Category categorise(String[] featureValues) {
		Double democratProb = getCategoryProbability(featureValues, Category.DEMOCRAT);
		Double republicanProb = getCategoryProbability(featureValues, Category.REPUBLICAN);
		return democratProb < republicanProb ? Category.REPUBLICAN : Category.DEMOCRAT;
	}

	private Double getCategoryProbability(String[] featureValues, Category category) {
		Double categoryProbability = categoryLogProbabilities.get(category);
		for (int i = 0; i < featureValues.length; i++) {
			Feature feature = new Feature(FeatureName.values()[i], featureValues[i].charAt(0));
			Map<Category, Double> categoryForFeatureLogProb = featureLogProbabilities.get(feature);
			if (categoryForFeatureLogProb != null) {
				Double featureLogProb = categoryForFeatureLogProb.get(category);
				if (featureLogProb != null) {
					categoryProbability += featureLogProb;
				}
			}
		}

		return categoryProbability;
	}

	private void createFeatureLogProbabilities(Map<Feature, Map<Category, Integer>> occurrences, int democratsCounter,
			int republicansCounter) {
		featureLogProbabilities = new HashMap<>();

		for (Entry<Feature, Map<Category, Integer>> featureEntry : occurrences.entrySet()) {
			Feature feature = featureEntry.getKey();
			Map<Category, Double> categoryProbabilities = new HashMap<>();

			for (Entry<Category, Integer> categoryEntry : featureEntry.getValue().entrySet()) {
				Category category = categoryEntry.getKey();
				if (category.equals(Category.REPUBLICAN)) {
					double logProbability = Math
							.log((double) (categoryEntry.getValue() + 1) / (republicansCounter + 2));
					categoryProbabilities.put(category, logProbability);
				} else if (category.equals(Category.DEMOCRAT)) {
					double logProbability = Math.log((double) (categoryEntry.getValue() + 1) / (democratsCounter + 2));
					categoryProbabilities.put(category, logProbability);
				}
			}

			featureLogProbabilities.put(feature, categoryProbabilities);
		}
	}

	private void addFeaturesForLine(String line, Category category, Map<Feature, Map<Category, Integer>> occurrences) {
		String[] split = line.split(",");
		for (int i = 0; i < FeatureName.values().length; i++) {
			addFeature(new Feature(FeatureName.values()[i], split[i + 1].charAt(0)), category, occurrences);
		}
	}

	private void addFeature(Feature feature, Category category, Map<Feature, Map<Category, Integer>> occurrences) {
		Map<Category, Integer> classOccur = occurrences.get(feature);
		if (classOccur == null) {
			classOccur = new HashMap<Category, Integer>();
		}

		Integer numberOccurrences = classOccur.get(category);
		if (numberOccurrences == null) {
			classOccur.put(category, 1);
		} else {
			classOccur.put(category, numberOccurrences + 1);
		}

		occurrences.put(feature, classOccur);
	}

	public Map<Feature, Map<Category, Double>> getFeatureLogProbabilities() {
		return featureLogProbabilities;
	}

	public Map<Category, Double> getCategoryLogProbabilities() {
		return categoryLogProbabilities;
	}
}
