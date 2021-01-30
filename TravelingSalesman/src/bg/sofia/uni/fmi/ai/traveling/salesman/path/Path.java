package bg.sofia.uni.fmi.ai.traveling.salesman.path;

public class Path {
	private City[] cities;
	private double fitness;

	public Path(City[] cities) {
		this.cities = cities;
		this.fitness = calculateFitness();
	}

	public City[] getCities() {
		return cities;
	}

	public double getFitness() {
		return fitness;
	}

	public void swapCities(int firstIndex, int secondIndex) {
		City tempCity = cities[firstIndex];
		cities[firstIndex] = cities[secondIndex];
		cities[secondIndex] = tempCity;
	}

	private double calculateFitness() {
		double sum = 0;
		for (int i = 0; i < cities.length - 1; i++) {
			sum += cities[i].calculateDistance(cities[i + 1]);
		}
		return sum;
	}

}
