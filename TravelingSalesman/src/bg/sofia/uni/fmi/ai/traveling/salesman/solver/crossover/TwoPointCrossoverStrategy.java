package bg.sofia.uni.fmi.ai.traveling.salesman.solver.crossover;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import bg.sofia.uni.fmi.ai.traveling.salesman.path.City;
import bg.sofia.uni.fmi.ai.traveling.salesman.path.Path;

public class TwoPointCrossoverStrategy implements CrossoverStrategy {
	private Random random = new Random();

	@Override
	public Path reproduce(Path parentOne, Path parentTwo) {
		int numberCities = parentOne.getCities().length;
		City[] cities = new City[numberCities];
		int randomStartIndex = random.nextInt(numberCities - 1);
		int randomEndIndex = random.nextInt(numberCities);
		while (randomEndIndex <= randomStartIndex) {
			randomEndIndex = random.nextInt(numberCities);
		}

		copyFirstParentSegment(parentOne, cities, randomStartIndex, randomEndIndex);
		copySecondParent(parentTwo, cities, randomEndIndex);

		return new Path(cities);
	}

	private void copySecondParent(Path parentTwo, City[] cities, int startIndex) {
		List<City> parentCities = Arrays.stream(parentTwo.getCities()).skip(startIndex).collect(Collectors.toList());
		parentCities.addAll(Arrays.stream(parentTwo.getCities()).limit(startIndex).collect(Collectors.toList()));
		Iterator<City> parentCitiesIterator = parentCities.iterator();

		int citiesIndex = startIndex;
		while (parentCitiesIterator.hasNext()) {
			City parentCity = parentCitiesIterator.next();
			if (!Arrays.stream(cities).anyMatch(city -> parentCity.equals(city))) {
				cities[citiesIndex] = parentCity;
				citiesIndex++;
			}

			if (citiesIndex >= cities.length) {
				citiesIndex = 0;
			}
		}
	}

	private void copyFirstParentSegment(Path parentOne, City[] cities, int randomStartIndex, int randomEndIndex) {
		for (int parentOneIndex = randomStartIndex; parentOneIndex < randomEndIndex; parentOneIndex++) {
			City parentCity = parentOne.getCities()[parentOneIndex];
			cities[parentOneIndex] = new City(parentCity.getxCoordinate(), parentCity.getyCoordinate());
		}
	}

}
