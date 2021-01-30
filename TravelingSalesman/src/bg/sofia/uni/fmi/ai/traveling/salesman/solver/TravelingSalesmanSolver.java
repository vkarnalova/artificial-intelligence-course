package bg.sofia.uni.fmi.ai.traveling.salesman.solver;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.Random;

import bg.sofia.uni.fmi.ai.traveling.salesman.path.City;
import bg.sofia.uni.fmi.ai.traveling.salesman.path.Path;
import bg.sofia.uni.fmi.ai.traveling.salesman.path.PathComparator;
import bg.sofia.uni.fmi.ai.traveling.salesman.solver.crossover.CrossoverStrategy;

public class TravelingSalesmanSolver {
	private static final int MAX_GENERATION_NUMBER = 100;
	private static final int MAX_COORDINATE = 10000;

	private Random random = new Random();
	private PathComparator pathComparator = new PathComparator();

	private int generationLength;
	private int numberCities;

	/**
	 * Solves the Traveling salesman problem using a genetic algorithm.
	 * 
	 * @param numberCities      Number of cities that the salesman has to visit
	 * @param crossoverStrategy Strategy used for reproduction
	 */
	public void solve(int numberCities, CrossoverStrategy crossoverStrategy) {
		generationLength = numberCities - numberCities % 2;
		this.numberCities = numberCities;
		Path[] generation = initGeneration();

		printBestPath(generation, 0);

		int generationNumber = 1;
		while (generationNumber <= MAX_GENERATION_NUMBER) {
			Path[] children = reproduce(generation, crossoverStrategy);
			mutate(children);
			generation = buildNextGeneration(generation, children);

			if (generationNumber % 10 == 0) {
				printBestPath(generation, generationNumber);
			}

			generationNumber++;
		}
	}

	private Path[] buildNextGeneration(Path[] generation, Path[] children) {
		Arrays.sort(generation, pathComparator);
		Arrays.sort(children, pathComparator);

		int numberElementsOfOldGeneration = (int) (0.2 * generationLength);
		Path[] newGeneration = new Path[generationLength];
		for (int i = 0; i < numberElementsOfOldGeneration; i++) {
			newGeneration[i] = generation[i];
		}
		for (int childIndex = 0; childIndex < generationLength - numberElementsOfOldGeneration; childIndex++) {
			newGeneration[childIndex + numberElementsOfOldGeneration] = children[childIndex];
		}

		Collections.shuffle(Arrays.asList(newGeneration));
		return newGeneration;
	}

	private void mutate(Path[] children) {
		for (int i = 0; i < children.length; i++) {
			int randomCityIndex = random.nextInt(numberCities);
			int randomOtherCityIndex = 0;
			do {
				randomOtherCityIndex = random.nextInt(numberCities);
			} while (randomCityIndex == randomOtherCityIndex);

			children[i].swapCities(randomCityIndex, randomOtherCityIndex);
		}
	}

	private Path[] reproduce(Path[] generation, CrossoverStrategy crossoverStrategy) {
		Path[] children = new Path[generationLength];
		for (int i = 0; i < generationLength; i += 2) {
			children[i] = crossoverStrategy.reproduce(generation[i], generation[i + 1]);
			children[i + 1] = crossoverStrategy.reproduce(generation[i + 1], generation[i]);
		}

		return children;
	}

	private Path[] initGeneration() {
		City[] cities = generateRandomPoints(numberCities);
		Path[] generation = new Path[generationLength];
		for (int i = 0; i < generationLength; i++) {
			Collections.shuffle(Arrays.asList(cities));
			generation[i] = new Path(cities);
		}
		return generation;
	}

	private City[] generateRandomPoints(int numberCities) {
		City[] cities = new City[numberCities];
		for (int i = 0; i < numberCities; i++) {
			cities[i] = new City(random.nextInt(MAX_COORDINATE), random.nextInt(MAX_COORDINATE));
		}
		return cities;
	}

	private void printBestPath(Path[] generation, int generationNumber) {
		System.out.println("Generation " + generationNumber + ":");

		Optional<Path> minPathOptional = Arrays.stream(generation).min(pathComparator);
		if (minPathOptional.isPresent()) {
			System.out.println("Fitness: " + minPathOptional.get().getFitness());
		}

	}
}
