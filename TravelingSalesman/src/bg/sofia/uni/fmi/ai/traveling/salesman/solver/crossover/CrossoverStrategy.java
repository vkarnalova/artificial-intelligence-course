package bg.sofia.uni.fmi.ai.traveling.salesman.solver.crossover;

import bg.sofia.uni.fmi.ai.traveling.salesman.path.Path;

public interface CrossoverStrategy {
	Path reproduce(Path parentOne, Path parentTwo);
}
