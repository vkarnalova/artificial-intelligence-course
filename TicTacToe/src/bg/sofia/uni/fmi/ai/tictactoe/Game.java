package bg.sofia.uni.fmi.ai.tictactoe;

import java.util.Scanner;

import bg.sofia.uni.fmi.ai.tictactoe.board.Board;
import bg.sofia.uni.fmi.ai.tictactoe.board.State;
import bg.sofia.uni.fmi.ai.tictactoe.player.Result;
import bg.sofia.uni.fmi.ai.tictactoe.player.TicTacToePlayer;

public class Game {
	private static final String BRACKETS_REGEX = "[\\[\\] ]";

	public void startGame() {
		try (Scanner scanner = new Scanner(System.in)) {
			System.out.print("Type y if you want to be first, type n otherwise: ");
			String first = scanner.nextLine().trim();
			if (first.equals("y")) {
				play(State.CIRCLE);
			} else if (first.equals("n")) {
				play(State.CROSS);
			} else {
				System.out.println("Invalid input");
			}
		}
	}

	private void play(State player) {
		Board board = new Board();
		TicTacToePlayer ticTacToe = new TicTacToePlayer();

		try (Scanner scanner = new Scanner(System.in)) {
			while (!board.isGameOver()) {
				if (player.equals(State.CROSS)) {
					Result result = ticTacToe.play(player, board);
					board = result.getBoard();
					print(board);
				} else if (player.equals(State.CIRCLE)) {
					System.out.print("Type your position: ");
					Position position = handleHumanInput(scanner.nextLine());
					board.move(player, position.getRow(), position.getColumn());
					print(board);
				}
				player = player.equals(State.CROSS) ? State.CIRCLE : State.CROSS;
			}
		}

		if (board.isGameOver()) {
			if (board.getWinner().equals(State.CIRCLE)) {
				System.out.println("You are the winner!");
			} else if (board.getWinner().equals(State.CROSS)) {
				System.out.println("The computer has won :(");
			} else {
				System.out.println("The game is a draw.");
			}
		}
	}

	public void print(Board board) {
		System.out.println("The current state of the board is: ");

		State[][] states = board.getBoard();
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				System.out.print(states[i][j].getValue() + " ");
			}
			System.out.println();
		}
		System.out.println();
	}

	private Position handleHumanInput(String line) {
		String processedLine = line.trim().replaceAll(BRACKETS_REGEX, "");
		String[] split = processedLine.split(",");
		return new Position(Integer.valueOf(split[0]), Integer.valueOf(split[1]));
	}

	private class Position {
		private int row;
		private int column;

		public Position(int row, int column) {
			this.row = row;
			this.column = column;
		}

		public int getRow() {
			return row;
		}

		public int getColumn() {
			return column;
		}
	}
}
