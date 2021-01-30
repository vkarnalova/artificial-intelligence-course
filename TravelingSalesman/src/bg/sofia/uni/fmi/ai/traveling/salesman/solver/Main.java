package bg.sofia.uni.fmi.ai.traveling.salesman.solver;

import java.util.Scanner;

import bg.sofia.uni.fmi.ai.traveling.salesman.solver.crossover.PartiallyMappedCrossoverStrategy;

public class Main {

	public static void main(String[] args) {
		int numberCities = readCitiesNumberFromInput();

		new TravelingSalesmanSolver().solve(numberCities, new PartiallyMappedCrossoverStrategy());
	}

	private static int readCitiesNumberFromInput() {
		try (Scanner scanner = new Scanner(System.in)) {
			return scanner.nextInt();
		}
	}
}
