package bg.sofia.uni.fmi.ai.sliding.puzzle.solver;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import bg.sofia.uni.fmi.ai.sliding.puzzle.graph.Node;
import bg.sofia.uni.fmi.ai.sliding.puzzle.tile.Position;
import bg.sofia.uni.fmi.ai.sliding.puzzle.tile.Tile;

public class InputHandler {
	public static final int DEFAULT_EMPTY_TILE_INDEX = -1;

	int tilesNumber;
	int emptyTileIndexSolution;
	int boardSize;
	List<Integer> numbers;

	public void read() {
		try (Scanner scanner = new Scanner(System.in)) {
			tilesNumber = scanner.nextInt();
			emptyTileIndexSolution = scanner.nextInt();
			numbers = new ArrayList<Integer>();
			boardSize = (int) Math.sqrt(tilesNumber + 1);
			for (int i = 0; i <= tilesNumber; i++) {
				numbers.add(scanner.nextInt());
			}
		}
	}

	public int getEmptyTileIndex() {
		if (emptyTileIndexSolution == DEFAULT_EMPTY_TILE_INDEX) {
			return tilesNumber;
		}
		return emptyTileIndexSolution;
	}

	public List<Integer> getTileNumbers() {
		return numbers;
	}

	public Node getRoot() {
		Tile[] tiles = new Tile[tilesNumber + 1];
		List<Integer> expectedValues = calculateExpectedValues();
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				tiles[i * boardSize + j] = new Tile(new Position(i, j), numbers.get(i * boardSize + j),
						expectedValues.get(i * boardSize + j));
			}
		}

		return new Node(tiles, boardSize, null);
	}

	private List<Integer> calculateExpectedValues() {
		int emptyTileIndex = getEmptyTileIndex();
		List<Integer> expectedValues = new ArrayList<Integer>();
		for (int i = 0; i < emptyTileIndex; i++) {
			expectedValues.add(i + 1);
		}
		expectedValues.add(0);
		for (int i = emptyTileIndex + 1; i < tilesNumber + 1; i++) {
			expectedValues.add(i);
		}
		return expectedValues;
	}
}
