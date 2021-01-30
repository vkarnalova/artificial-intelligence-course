package bg.sofia.uni.fmi.ai.traveling.salesman.path;

import java.util.Comparator;

public class PathComparator implements Comparator<Path> {

	@Override
	public int compare(Path firstPath, Path secondPath) {
		double firstPathFitness = firstPath.getFitness();
		double secondPathFitness = secondPath.getFitness();

		if (firstPathFitness < secondPathFitness) {
			return -1;
		} else if (firstPathFitness > secondPathFitness) {
			return 1;
		} else {
			return 0;
		}
	}

}
