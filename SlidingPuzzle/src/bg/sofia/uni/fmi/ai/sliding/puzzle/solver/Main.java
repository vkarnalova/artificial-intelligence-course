package bg.sofia.uni.fmi.ai.sliding.puzzle.solver;

import java.util.Deque;

import bg.sofia.uni.fmi.ai.sliding.puzzle.graph.Node;

public class Main {

	public static void main(String[] args) {
		InputHandler inputHandler = new InputHandler();
		inputHandler.read();
		Node root = inputHandler.getRoot();

		System.out.println("Solving...");

		SlidingPuzzleSolver puzzleSolver = new SlidingPuzzleSolver();
		Deque<Node> result = puzzleSolver.idaStar(root);

		new OutputHandler().printSolution(result);
	}
}
