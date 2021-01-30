package bg.sofia.uni.fmi.ai.traveling.salesman.solver.crossover;

import java.util.Arrays;
import java.util.Random;

import bg.sofia.uni.fmi.ai.traveling.salesman.path.City;
import bg.sofia.uni.fmi.ai.traveling.salesman.path.Path;

public class PartiallyMappedCrossoverStrategy implements CrossoverStrategy {
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

		// Copy cities from first parent's segment
		copyFirstParentSegment(parentOne, cities, randomStartIndex, randomEndIndex);

		// Copy cities from second parent's segment
		copySecondParentSegment(parentOne, parentTwo, cities, randomStartIndex, randomEndIndex);

		// Copy cities from the remaining part of parent two
		copySecondParent(parentTwo, numberCities, cities);

		return new Path(cities);
	}

	private void copySecondParent(Path parentTwo, int numberCities, City[] cities) {
		for (int childIndex = 0; childIndex < numberCities; childIndex++) {
			if (cities[childIndex] == null) {
				cities[childIndex] = parentTwo.getCities()[childIndex];
			}
		}
	}

	private void copySecondParentSegment(Path parentOne, Path parentTwo, City[] cities, int randomStartIndex,
			int randomEndIndex) {
		int numberCities = cities.length;
		for (int parentTwoIndex = randomStartIndex; parentTwoIndex < randomEndIndex; parentTwoIndex++) {
			City parentTwoCity = parentTwo.getCities()[parentTwoIndex];

			// Copy only those elements that are not already present in cities
			if (!Arrays.stream(cities).anyMatch(city -> parentTwoCity.equals(city))) {
				int index = parentTwoIndex;
				int mappedIndexParentTwo = -1;

				// Try only finite amount of times in order to avoid being stuck in a cycle
				int numberTry = 0;
				do {
					numberTry++;
					City parentOneCity = parentOne.getCities()[index];
					mappedIndexParentTwo = findCityIndex(parentTwo.getCities(), parentOneCity);
					if (mappedIndexParentTwo == -1) {
						break;
					}
					index = mappedIndexParentTwo;
				} while (cities[mappedIndexParentTwo] != null && numberTry <= numberCities);

				if (mappedIndexParentTwo != -1) {
					cities[mappedIndexParentTwo] = parentTwoCity;
				}
			}
		}
	}

	private void copyFirstParentSegment(Path parentOne, City[] cities, int randomStartIndex, int randomEndIndex) {
		for (int parentOneIndex = randomStartIndex; parentOneIndex < randomEndIndex; parentOneIndex++) {
			City parentCity = parentOne.getCities()[parentOneIndex];
			cities[parentOneIndex] = new City(parentCity.getxCoordinate(), parentCity.getyCoordinate());
		}
	}

	private int findCityIndex(City[] cities, City parentOneCity) {
		for (int cityIndex = 0; cityIndex < cities.length; cityIndex++) {
			if (cities[cityIndex].equals(parentOneCity)) {
				return cityIndex;
			}
		}
		return -1;
	}

}
