package bg.sofia.fmi.ai.recommendation.system.clustering;

import java.util.HashMap;
import java.util.Map;

public class Centroid {
	private Map<Integer, Double> ratings; // jokeId -> rating

	public Centroid() {
		ratings = new HashMap<Integer, Double>();
	}

	public Map<Integer, Double> getRatings() {
		return ratings;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ratings == null) ? 0 : ratings.hashCode());
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
		Centroid other = (Centroid) obj;
		if (ratings == null) {
			if (other.ratings != null) {
				return false;
			}
		} else if (!ratings.equals(other.ratings)) {
			return false;
		}
		return true;
	}
}
