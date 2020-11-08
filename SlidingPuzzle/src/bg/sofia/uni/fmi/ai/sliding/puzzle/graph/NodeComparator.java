package bg.sofia.uni.fmi.ai.sliding.puzzle.graph;

import java.util.Comparator;

public class NodeComparator implements Comparator<Node> {

	@Override
	public int compare(Node firstNode, Node secondNode) {
		int firstNodeDistance = firstNode.getManhattanDistance();
		int secondNodeDistance = secondNode.getManhattanDistance();

		if (firstNodeDistance < secondNodeDistance) {
			return -1;
		} else if (firstNodeDistance > secondNodeDistance) {
			return 1;
		} else {
			return 0;
		}
	}

}
