package bg.sofia.uni.fmi.ai.id3;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import bg.sofia.uni.fmi.ai.id3.node.Node;

public class ID3 {
	private static final double DELTA = 0.01;
	Node root;

	public void learn(List<Example> trainingData, int minimumExamplesNumber, boolean stopSplitIfInfGainInsignificant) {
		root = runID3(trainingData, minimumExamplesNumber, stopSplitIfInfGainInsignificant);
	}

	public Category classify(Example example) {
		return root.classify(example);
	}

	public Node runID3(List<Example> trainingData, int minimumExamplesNumber, boolean stopSplitIfInfGainInsignificant) {
		OccurrencesData occurancesData = countOccurs(trainingData);

		double targetEntropy = calculateTargetEntropy(occurancesData.noRecurrenceEventsNumber,
				occurancesData.recurrenceEventsNumber);

		// If the node is a leaf
		if (targetEntropy == 0.0) {
			return new Node(null, trainingData.get(0).category);
		} else if (trainingData.size() < minimumExamplesNumber) {
			return createChildBasedOnCurrentCategories(occurancesData);
		}

		FeatureName feature = calculateBestAttribute(trainingData, occurancesData, targetEntropy,
				stopSplitIfInfGainInsignificant);

		// Stop splitting if the current information gain is insignificant
		if (feature == null && stopSplitIfInfGainInsignificant) {
			return createChildBasedOnCurrentCategories(occurancesData);
		}

		Map<Feature, List<Example>> dividedTrainingData = divideTrainingData(trainingData, feature);

		Node node = new Node(feature, null);
		for (Entry<Feature, List<Example>> entry : dividedTrainingData.entrySet()) {
			Node child = runID3(entry.getValue(), minimumExamplesNumber, stopSplitIfInfGainInsignificant);
			if (child == null) {
				Category category = (occurancesData.noRecurrenceEventsNumber >= occurancesData.recurrenceEventsNumber)
						? Category.NO_RECURRENCE_EVENTS
						: Category.RECURRENCE_EVENTS;
				node.addChild(entry.getKey(), new Node(null, category));
			} else {
				node.addChild(entry.getKey(), child);
			}
		}

		return node;
	}

	private Node createChildBasedOnCurrentCategories(OccurrencesData occurancesData) {
		if (occurancesData.noRecurrenceEventsNumber == occurancesData.recurrenceEventsNumber) {
			return null;
		}

		Category category = (occurancesData.noRecurrenceEventsNumber > occurancesData.recurrenceEventsNumber)
				? Category.NO_RECURRENCE_EVENTS
				: Category.RECURRENCE_EVENTS;
		return new Node(null, category);
	}

	private Map<Feature, List<Example>> divideTrainingData(List<Example> trainingData, FeatureName featureName) {
		Map<Feature, List<Example>> dividedData = trainingData.stream().collect(Collectors.groupingBy(example -> {
			return example.getFeatures().stream().filter(feature -> feature.name.equals(featureName)).findFirst().get();
		}, Collectors.toList()));

		// deep copy
		for (Entry<Feature, List<Example>> entry : dividedData.entrySet()) {
			List<Example> copiedExamples = deepCopyData(entry.getValue());

			// remove feature
			copiedExamples.stream().forEach(example -> example.removeFeature(featureName));
			dividedData.put(entry.getKey(), copiedExamples);
		}

		return dividedData;
	}

	private List<Example> deepCopyData(List<Example> data) {
		return data.stream().map(e -> e.deepCopy()).collect(Collectors.toList());
	}

	private FeatureName calculateBestAttribute(List<Example> trainingData, OccurrencesData occurancesData,
			double targetEntropy, boolean stopSplitIfInfGainInsignificant) {
		int examplesNumber = trainingData.size();

		FeatureName bestFeatureName = null;
		double bestFeatureInfGain = Double.MIN_VALUE;
		for (Feature feature : trainingData.get(0).getFeatures()) {
			double currentInfGain = calculateInformationGain(feature.name, occurancesData, examplesNumber,
					targetEntropy);
			if (currentInfGain > bestFeatureInfGain) {
				bestFeatureName = feature.name;
				bestFeatureInfGain = currentInfGain;
			}
		}

		if (bestFeatureInfGain < DELTA && stopSplitIfInfGainInsignificant) {
			return null;
		}

		return bestFeatureName;
	}

	public double calculateInformationGain(FeatureName featureName, OccurrencesData occurancesData, int examplesNumber,
			double targetEntropy) {
		return targetEntropy - calculateFeatureEntropy(featureName, occurancesData, examplesNumber);
	}

	private Double calculateFeatureEntropy(FeatureName featureName, OccurrencesData occurancesData,
			int examplesNumber) {
		Stream<Entry<Feature, Map<Category, Integer>>> filter = occurancesData.occurrences.entrySet()
				.stream()
				.filter(entry -> entry.getKey().name.equals(featureName));
		DoubleStream mapToDouble = filter.mapToDouble(entry -> {
			return calculatefeatureProbability(entry.getValue(), examplesNumber) * calculateEntropy(entry.getValue());
		});
		return mapToDouble.sum();
	}

	private double calculatefeatureProbability(Map<Category, Integer> featureOccursByCategory, int examplesNumber) {
		int featureExamples = featureOccursByCategory.values().stream().mapToInt(Integer::intValue).sum();
		return (double) featureExamples / examplesNumber;
	}

	private double calculateEntropy(Map<Category, Integer> featureOccursByCategory) {
		int total = featureOccursByCategory.values().stream().mapToInt(Integer::intValue).sum();
		return Arrays.stream(Category.values())
				.filter(category -> featureOccursByCategory.get(category) != null)
				.map(category -> featureOccursByCategory.get(category))
				.mapToDouble(occurance -> {
					double probability = (double) occurance / total;
					return (-1) * probability * log2(probability);
				})
				.sum();
	}

	public OccurrencesData countOccurs(List<Example> trainingData) {
		Map<Feature, Map<Category, Integer>> occurrences = new HashMap<>();
		Iterator<Example> iterator = trainingData.iterator();

		int noRecurrenceEventsNumber = 0;
		int recurrenceEventsNumber = 0;
		while (iterator.hasNext()) {
			Example example = iterator.next();

			if (example.getCategory().equals(Category.NO_RECURRENCE_EVENTS)) {
				noRecurrenceEventsNumber++;
			} else {
				recurrenceEventsNumber++;
			}

			example.getFeatures().forEach(feature -> addFeature(feature, example.getCategory(), occurrences));
		}

		return new OccurrencesData(occurrences, noRecurrenceEventsNumber, recurrenceEventsNumber);
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

	public Double calculateTargetEntropy(int firstCategoryCount, int secondCategoryCount) {
		int total = firstCategoryCount + secondCategoryCount;
		double firstCategoryProb = (double) firstCategoryCount / total;
		double secondCategoryProb = (double) secondCategoryCount / total;

		double firstEntropy = (firstCategoryProb != 0.0) ? (-1) * firstCategoryProb * log2(firstCategoryProb) : 0.0;
		double secondEntropy = (secondCategoryProb != 0.0) ? (-1) * secondCategoryProb * log2(secondCategoryProb) : 0.0;

		return firstEntropy + secondEntropy;
	}

	public double log2(double num) {
		return Math.log(num) / Math.log(2);
	}

}
