package bg.sofia.uni.fmi.ai.bayes.classifier;

public class Feature {
	FeatureName name;
	char value;

	public Feature(FeatureName name, char value) {
		this.name = name;
		this.value = value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + value;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Feature other = (Feature) obj;
		if (name != other.name) {
			return false;
		}
		if (value != other.value) {
			return false;
		}
		return true;
	}
}
