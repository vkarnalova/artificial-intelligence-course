package bg.sofia.uni.fmi.ai.sliding.puzzle.solver;

import java.util.ArrayDeque;
import java.util.Deque;

import bg.sofia.uni.fmi.ai.sliding.puzzle.graph.Node;

public class SlidingPuzzleSolver {

	private static final int SOLUTION_FOUND = -1;

	/**
	 * Begins the Iterative deepening A star search from the root.
	 * 
	 * @param root The root
	 * @return the path to the solution
	 */
	public Deque<Node> idaStar(Node root) {
		int threshold = root.getManhattanDistance();
		Deque<Node> path = new ArrayDeque<>();
		path.push(root);
		while (true) {
			int result = ids(path, 0, threshold);
			if (result == SOLUTION_FOUND) {
				return path;
			}
			threshold = result;
		}
	}

	/**
	 * Recursively searches for a solution starting from the path's head. The path
	 * acts as a stack. The search will stop when the cost of the path to the
	 * current node is greater than a threshold.
	 * 
	 * @param path              The nodes that were visited in order. It acts as a
	 *                          stack.
	 * @param costToCurrentNode The cost to get to the current node from the root of
	 *                          the graph.
	 * @param threshold         The maximum boundary for the current search.
	 * @return -1 if a solution is found, the smallest cost of path that is greater
	 *         than the threshold otherwise
	 */
	private int ids(Deque<Node> path, int costToCurrentNode, int threshold) {
		Node currentNode = path.peek();
		int costOfPath = costToCurrentNode + currentNode.getManhattanDistance();
		if (costOfPath > threshold) {
			return costOfPath;
		} else if (currentNode.isFinalNode()) {
			return SOLUTION_FOUND;
		}

		int minCostOfPath = Integer.MAX_VALUE;
		for (Node successor : currentNode.getOrderedSuccessors()) {
			if (!path.contains(successor)) {
				path.push(successor);
				int result = ids(path, costToCurrentNode + 1, threshold);
				if (result == SOLUTION_FOUND) {
					return result;
				} else if (result < minCostOfPath) {
					minCostOfPath = result;
				}
				path.poll();
			}
		}
		return minCostOfPath;
	}
}
