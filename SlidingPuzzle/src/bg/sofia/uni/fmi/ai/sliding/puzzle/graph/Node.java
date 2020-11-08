package bg.sofia.uni.fmi.ai.sliding.puzzle.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import bg.sofia.uni.fmi.ai.sliding.puzzle.tile.Movement;
import bg.sofia.uni.fmi.ai.sliding.puzzle.tile.Tile;

public class Node {
	private Tile puzzle[];
	private int boardSize;
	private Movement movementFromParent;
	private int manhattanDistance;

	public Node(Tile tiles[], int boardSize, Movement movementFromParent) {
		this.puzzle = tiles;
		this.boardSize = boardSize;
		this.movementFromParent = movementFromParent;
		manhattanDistance = calculateManhattanDistance();
	}

	public int getManhattanDistance() {
		return manhattanDistance;
	}

	private int calculateManhattanDistance() {
		int manhattanDistance = 0;
		for (Tile tile : puzzle) {
			if (!tile.isEmpty()) {
				manhattanDistance += tile.calculateManhattanDistance(findGoalTile(tile));
			}
		}

		return manhattanDistance;
	}

	public Tile findGoalTile(Tile tile) {
		for (Tile currentTile : puzzle) {
			if (tile.getCurrentValue() == currentTile.getExpextedValue()) {
				return currentTile;
			}
		}

		throw new IllegalStateException("This should never happen.");
	}

	public boolean isFinalNode() {
		return Arrays.stream(puzzle)
				.filter(tile -> tile.getCurrentValue() != tile.getExpextedValue())
				.findFirst()
				.isEmpty();
	}

	public List<Node> getOrderedSuccessors() {
		int indexEmptyTile = findIndexOfEmptyTile();

		List<Node> successors = new ArrayList<>();

		Tile emptyTile = puzzle[indexEmptyTile];
		int emptyTileXCoordinate = emptyTile.getPosition().getxCoordinate();
		int emptyTileYCoordinate = emptyTile.getPosition().getyCoordinate();
		successors.add(move(indexEmptyTile, emptyTileXCoordinate - 1, emptyTileYCoordinate, Movement.DOWN));
		successors.add(move(indexEmptyTile, emptyTileXCoordinate, emptyTileYCoordinate - 1, Movement.RIGHT));
		successors.add(move(indexEmptyTile, emptyTileXCoordinate, emptyTileYCoordinate + 1, Movement.LEFT));
		successors.add(move(indexEmptyTile, emptyTileXCoordinate + 1, emptyTileYCoordinate, Movement.UP));

		return successors.stream().filter(successor -> successor != null).collect(Collectors.toList());
	}

	public Node move(int indexEmptyTile, int newXCoordinate, int newYCoordinate, Movement movement) {
		if (newXCoordinate < 0 || newXCoordinate >= boardSize || newYCoordinate < 0 || newYCoordinate >= boardSize) {
			return null;
		}

		Tile[] copiedPuzzle = new Tile[boardSize * boardSize];
		for (int i = 0; i < boardSize * boardSize; i++) {
			copiedPuzzle[i] = puzzle[i].copyTile();
		}

		copiedPuzzle[indexEmptyTile]
				.setCurrentValue(copiedPuzzle[boardSize * newXCoordinate + newYCoordinate].getCurrentValue());
		copiedPuzzle[boardSize * newXCoordinate + newYCoordinate].setCurrentValue(0);

		return new Node(copiedPuzzle, boardSize, movement);
	}

	public Tile[] getPuzzle() {
		return puzzle;
	}

	public Movement getMovementFromParent() {
		return movementFromParent;
	}

	public int findIndexOfEmptyTile() {
		for (int i = 0; i < puzzle.length; i++) {
			if (puzzle[i].isEmpty()) {
				return i;
			}
		}

		throw new IllegalStateException("This should never happen.");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(puzzle);
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
		Node other = (Node) obj;
		for (int i = 0; i < puzzle.length; i++) {
			if (!puzzle[i].equals(other.getPuzzle()[i])) {
				return false;
			}
		}
		return true;
	}
}
