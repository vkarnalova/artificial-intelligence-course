package bg.sofia.uni.fmi.ai.queens.solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class NQueensSolver {
	private final Random random = new Random();

	private int[] queens; // queens[x] = y; queen on column x and row y
	private int[] rows; // rows[x] = # of queens on row x
	private int[] reverseDiagonals;
	private int[] mainDiagonals;
	private int queensNumber;

	/**
	 * Solves the N queens problem.
	 * 
	 * @param queensNumber the number of queens that should be placed on the board
	 * @return array representing the queens positions where the index is the column
	 *         and the value of the array for that index is the row for the current
	 *         queen
	 */
	public int[] solve(int queensNumber) {
		initQueens(queensNumber);

		int iter = 0;
		while (iter++ < 60) {
			int column = getColWithQueenWithMaxConflicts();

			int newRow = getRowWithMinConflict(column);
			int oldRow = queens[column];
			if (oldRow != newRow) {
				rows[oldRow]--;
				reverseDiagonals[(column - oldRow) + (queensNumber - 1)]--;
				mainDiagonals[column + oldRow]--;

				queens[column] = newRow;
				rows[newRow]++;
				reverseDiagonals[(column - newRow) + (queensNumber - 1)]++;
				mainDiagonals[column + newRow]++;
			}

		}

		if (hasConflicts()) {
			return solve(queensNumber);
		}

		return queens;
	}

	private boolean hasConflicts() {
		for (int column = 0; column < queensNumber; column++) {
			if (calculateConflictForQueen(column) > 0) {
				return true;
			}
		}
		return false;
	}

	private int getRowWithMinConflict(int column) {
		int[] conflicts = calculateConflicts(queensNumber, column);
		int minConflicts = Arrays.stream(conflicts).min().getAsInt();

		List<Integer> minConflictIndexes = new ArrayList<>();
		for (int i = 0; i < queensNumber; i++) {
			if (conflicts[i] == minConflicts) {
				minConflictIndexes.add(i);
			}
		}

		// Choose random row with minimum conflicts
		return minConflictIndexes.get(random.nextInt(minConflictIndexes.size()));
	}

	private int[] calculateConflicts(int queensNumber, int columnIndex) {
		int rowIndex = queens[columnIndex];
		int[] conflicts = new int[queensNumber];
		for (int row = 0; row < queensNumber; row++) {
			if (row != rowIndex) {
				conflicts[row] = rows[row] + reverseDiagonals[(columnIndex - row) + (queensNumber - 1)]
						+ mainDiagonals[columnIndex + row];
			} else {
				conflicts[row] = calculateConflictForQueen(columnIndex);
			}
		}
		return conflicts;
	}

	private int getColWithQueenWithMaxConflicts() {
		int[] conflicts = new int[queensNumber];
		for (int column = 0; column < queensNumber; column++) {
			conflicts[column] = calculateConflictForQueen(column);
		}

		Integer maxConflicts = Arrays.stream(conflicts).max().getAsInt();
		List<Integer> maxConflictIndexes = new ArrayList<>();
		for (int i = 0; i < queensNumber; i++) {
			if (conflicts[i] == maxConflicts) {
				maxConflictIndexes.add(i);
			}
		}

		// Choose random queen with maximum conflicts
		return maxConflictIndexes.get(random.nextInt(maxConflictIndexes.size()));
	}

	private int calculateConflictForQueen(int column) {
		int row = queens[column];
		return rows[row] + reverseDiagonals[(column - row) + (queensNumber - 1)] + mainDiagonals[column + row] - 3;
	}

	private void initQueens(int queensNumber) {
		this.queensNumber = queensNumber;
		queens = new int[queensNumber];
		rows = new int[queensNumber];
		reverseDiagonals = new int[2 * queensNumber - 1];
		mainDiagonals = new int[2 * queensNumber - 1];

		for (int column = 0; column < queensNumber; column++) {
			List<Integer> minConflictIndexes = findMinConflictIndexesForInit(column);
			int randomIndex = random.nextInt(minConflictIndexes.size());
			int randomRowIndex = minConflictIndexes.get(randomIndex);
			queens[column] = randomRowIndex;
			rows[randomRowIndex]++;
			reverseDiagonals[(column - randomRowIndex) + (queensNumber - 1)]++;
			mainDiagonals[column + randomRowIndex]++;
		}

	}

	private List<Integer> findMinConflictIndexesForInit(int columnIndex) {
		int[] conflicts = calculateConflictsForInit(columnIndex);
		Integer minConflicts = Arrays.stream(conflicts).min().getAsInt();

		List<Integer> minConflictIndexes = new ArrayList<>();
		for (int i = 0; i < queensNumber; i++) {
			if (conflicts[i] == minConflicts) {
				minConflictIndexes.add(i);
			}
		}

		return minConflictIndexes;
	}

	private int[] calculateConflictsForInit(int columnIndex) {
		int[] conflicts = new int[queensNumber];
		for (int row = 0; row < queensNumber; row++) {
			conflicts[row] = rows[row] + reverseDiagonals[(columnIndex - row) + (queensNumber - 1)]
					+ mainDiagonals[columnIndex + row];
		}
		return conflicts;
	}
}
