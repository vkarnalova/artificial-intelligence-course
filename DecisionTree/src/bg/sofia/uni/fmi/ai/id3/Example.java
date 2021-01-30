package bg.sofia.uni.fmi.ai.id3;

import java.util.List;
import java.util.stream.Collectors;

public class Example {
	Category category;
	List<Feature> features;

	public Example(Category category, List<Feature> features) {
		this.category = category;
		this.features = features;
	}

	public Category getCategory() {
		return category;
	}

	public List<Feature> getFeatures() {
		return features;
	}

	public void removeFeature(FeatureName featureName) {
		features.removeIf(feature -> feature.name.equals(featureName));
	}

	public Example deepCopy() {
		List<Feature> copiedFeatures = features.stream().map(f -> f.deepCopy()).collect(Collectors.toList());
		return new Example(category, copiedFeatures);
	}

}
