package bg.sofia.uni.fmi.ai.tictactoe.board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {
	static final int MAX_BOARD_SIZE = 3;

	private State[][] board;
	private int numberMadeMoves;
	private boolean isGameOver;
	private State winner;

	public Board() {
		board = new State[MAX_BOARD_SIZE][MAX_BOARD_SIZE];
		for (int row = 0; row < MAX_BOARD_SIZE; row++) {
			for (int column = 0; column < MAX_BOARD_SIZE; column++) {
				board[row][column] = State.BLANK;
			}
		}
		numberMadeMoves = 0;
	}

	public Board(State[][] board, int numberMadeMoves) {
		this.board = new State[MAX_BOARD_SIZE][MAX_BOARD_SIZE];
		for (int column = 0; column < MAX_BOARD_SIZE; column++) {
			for (int row = 0; row < MAX_BOARD_SIZE; row++) {
				this.board[row][column] = board[row][column];
			}
		}
		this.numberMadeMoves = numberMadeMoves;
	}

	public Board(Board copyBoard) {
		board = new State[MAX_BOARD_SIZE][MAX_BOARD_SIZE];
		for (int column = 0; column < MAX_BOARD_SIZE; column++) {
			for (int row = 0; row < MAX_BOARD_SIZE; row++) {
				board[row][column] = copyBoard.getBoard()[row][column];
			}
		}
		numberMadeMoves = copyBoard.getNumberMadeMoves();
	}

	public List<Board> getSuccessors(State player) {
		List<Board> successors = new ArrayList<>();
		for (int i = 0; i < MAX_BOARD_SIZE * MAX_BOARD_SIZE; i++) {
			int row = i % MAX_BOARD_SIZE;
			int column = i / MAX_BOARD_SIZE;

			if (board[row][column].equals(State.BLANK)) {
				Board successor = new Board(this);
				successor.move(player, row, column);
				successors.add(successor);
			}
		}

		return successors;
	}

	public void move(State playerTurn, int row, int column) {
		if (board[row][column] != State.BLANK) {
			throw new IllegalStateException("Invalid movement [" + row + ", " + column + "].");
		}

		board[row][column] = playerTurn;
		numberMadeMoves++;

		checkForWinner(row, column);

		// The game is a draw.
		if (!isGameOver && numberMadeMoves == MAX_BOARD_SIZE * MAX_BOARD_SIZE) {
			isGameOver = true;
			winner = State.BLANK;
		}
	}

	public State[][] getBoard() {
		return board;
	}

	public boolean isGameOver() {
		return isGameOver;
	}

	public State getWinner() {
		return winner;
	}

	private int getNumberMadeMoves() {
		return numberMadeMoves;
	}

	private void checkForWinner(int row, int column) {
		checkRows(row);
		checkColumn(column);
		checkMainDiagonal();
		checkReverseDiagonal();
	}

	private void checkReverseDiagonal() {
		State state = board[0][MAX_BOARD_SIZE - 1];
		boolean flagGameOver = true;
		for (int index = 0; index < MAX_BOARD_SIZE; index++) {
			if (board[index][MAX_BOARD_SIZE - index - 1] != state) {
				flagGameOver = false;
			}
		}

		if (flagGameOver && (state.equals(State.CROSS) || state.equals(State.CIRCLE))) {
			isGameOver = flagGameOver;
			winner = state;
		}
	}

	private void checkMainDiagonal() {
		State state = board[0][0];
		boolean flagGameOver = true;
		for (int index = 0; index < MAX_BOARD_SIZE; index++) {
			if (board[index][index] != state) {
				flagGameOver = false;
			}
		}

		if (flagGameOver && (state.equals(State.CROSS) || state.equals(State.CIRCLE))) {
			isGameOver = flagGameOver;
			winner = state;
		}
	}

	private void checkRows(int row) {
		State state = board[row][0];
		boolean allMatch = Arrays.stream(board[row]).allMatch(currentState -> currentState.equals(state));
		if (allMatch) {
			winner = state;
			isGameOver = true;
		}
	}

	private void checkColumn(int column) {
		State state = board[0][column];

		boolean flagGameOver = true;
		for (int row = 0; row < MAX_BOARD_SIZE; row++) {
			if (board[row][column] != state) {
				flagGameOver = false;
				break;
			}
		}

		if (flagGameOver) {
			isGameOver = true;
			winner = state;
		}
	}
}
