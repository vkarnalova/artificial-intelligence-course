package bg.sofia.uni.fmi.ai.id3;

public class Feature {
	public FeatureName name;
	public String value;

	public Feature(FeatureName name, String value) {
		this.name = name;
		this.value = value;
	}

	public Feature deepCopy() {
		return new Feature(name, value);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		if (value == null) {
			if (other.value != null) {
				return false;
			}
		} else if (!value.equals(other.value)) {
			return false;
		}
		return true;
	}
}
