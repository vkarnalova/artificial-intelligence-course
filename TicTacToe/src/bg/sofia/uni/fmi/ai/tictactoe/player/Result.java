package bg.sofia.uni.fmi.ai.tictactoe.player;

import bg.sofia.uni.fmi.ai.tictactoe.board.Board;

public class Result {
	private Board board;
	private int score;

	public Result(Board board, int score) {
		this.board = board;
		this.score = score;
	}

	public Board getBoard() {
		return board;
	}

	public int getScore() {
		return score;
	}
}
