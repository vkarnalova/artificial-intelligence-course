package bg.sofia.uni.fmi.ai.sliding.puzzle.solver;

import java.util.ArrayDeque;
import java.util.Deque;

import bg.sofia.uni.fmi.ai.sliding.puzzle.graph.Node;

public class OutputHandler {

	public void printSolution(Deque<Node> result) {
		Deque<String> stepsToSolution = getStepsToSolution(result);
		System.out.println(stepsToSolution.size());
		while (!stepsToSolution.isEmpty()) {
			System.out.println(stepsToSolution.poll());
		}
	}

	private Deque<String> getStepsToSolution(Deque<Node> result) {
		Deque<String> steps = new ArrayDeque<>();
		while (result.peek().getMovementFromParent() != null) {
			Node currentNode = result.poll();
			steps.push(currentNode.getMovementFromParent().getValue());
		}

		return steps;
	}
}
