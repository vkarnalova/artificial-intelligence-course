package bg.sofia.uni.fmi.ai.queens.solver;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		int numberQueens = readQueensNumberFromInput();

		System.out.println("Solving...");

		long startTime = System.currentTimeMillis();
		int[] queens = new NQueensSolver().solve(numberQueens);
		long endTime = System.currentTimeMillis();

		printResult(queens, numberQueens, endTime - startTime);
	}

	private static void printResult(int[] queens, int numberQueens, long time) {
		if (numberQueens < 50) {
			for (int column = 0; column < numberQueens; column++) {
				for (int row = 0; row < numberQueens; row++) {
					if (row == queens[column]) {
						System.out.print("* ");
					} else {
						System.out.print("- ");
					}
				}
				System.out.println();
			}
		} else {
			System.out.println("Time " + time + "(millis)");
		}
	}

	private static int readQueensNumberFromInput() {
		try (Scanner scanner = new Scanner(System.in)) {
			return scanner.nextInt();
		}
	}

}
