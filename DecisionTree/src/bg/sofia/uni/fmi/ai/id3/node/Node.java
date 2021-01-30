package bg.sofia.uni.fmi.ai.id3.node;

import java.util.HashMap;
import java.util.Map;

import bg.sofia.uni.fmi.ai.id3.Category;
import bg.sofia.uni.fmi.ai.id3.Example;
import bg.sofia.uni.fmi.ai.id3.Feature;
import bg.sofia.uni.fmi.ai.id3.FeatureName;

public class Node {
	private FeatureName featureName;
	private Category category;
	private Map<Feature, Node> children;

	public Node(FeatureName featureName, Category category) {
		this.featureName = featureName;
		this.category = category;
		this.children = new HashMap<Feature, Node>();
	}

	public void addChild(Feature feature, Node childNode) {
		children.put(feature, childNode);
	}

	public FeatureName getFeatureName() {
		return featureName;
	}

	public Category getCategory() {
		return category;
	}

	public Map<Feature, Node> getChildren() {
		return children;
	}

	public Category classify(Example example) {
		if (category != null) {
			return category;
		}

		Feature feature = example.getFeatures().stream().filter(f -> f.name.equals(featureName)).findFirst().get();
		Node child = children.get(feature);

		return child != null ? child.classify(example) : null;
	}
}
