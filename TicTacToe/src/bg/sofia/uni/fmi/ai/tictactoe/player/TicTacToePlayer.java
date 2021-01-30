package bg.sofia.uni.fmi.ai.tictactoe.player;

import bg.sofia.uni.fmi.ai.tictactoe.board.Board;
import bg.sofia.uni.fmi.ai.tictactoe.board.State;

public class TicTacToePlayer {

	public Result play(State player, Board board) {
		return miniMaxAlphaBetaPruning(player, board, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
	}

	private Result miniMaxAlphaBetaPruning(State player, Board board, int alpha, int beta, int depth) {
		if (board.isGameOver()) {
			return new Result(board, score(board, depth));
		}

		if (player.equals(State.CROSS)) {
			return getMax(player, board, alpha, beta, depth + 1);
		}

		return getMin(player, board, alpha, beta, depth + 1);
	}

	private Result getMax(State player, Board board, int alpha, int beta, int depth) {
		Board bestSuccessor = null;
		for (Board successor : board.getSuccessors(player)) {
			State successorPlayer = player.equals(State.CROSS) ? State.CIRCLE : State.CROSS;
			Result result = miniMaxAlphaBetaPruning(successorPlayer, successor, alpha, beta, depth);

			if (result.getScore() > alpha) {
				alpha = result.getScore();
				bestSuccessor = successor;
			}

			if (alpha >= beta) {
				break;
			}

		}

		return new Result(bestSuccessor, alpha);
	}

	private Result getMin(State player, Board board, int alpha, int beta, int depth) {
		Board bestSuccessor = null;
		for (Board successor : board.getSuccessors(player)) {
			State successorPlayer = player.equals(State.CROSS) ? State.CIRCLE : State.CROSS;
			Result result = miniMaxAlphaBetaPruning(successorPlayer, successor, alpha, beta, depth);

			if (result.getScore() < beta) {
				beta = result.getScore();
				bestSuccessor = successor;
			}

			if (alpha >= beta) {
				break;
			}

		}

		return new Result(bestSuccessor, beta);
	}

	private int score(Board board, int depth) {
		if (board.getWinner().equals(State.CROSS)) {
			return 10 - depth;
		} else if (board.getWinner().equals(State.CIRCLE)) {
			return -10 + depth;
		}
		return 0;
	}
}
