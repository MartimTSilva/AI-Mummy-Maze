package mummyMaze;

import agent.Agent;

import java.io.File;
import java.io.IOException;
import static mummyMaze.MummyMazeState.SIZE;

public class MummyMazeAgent extends Agent<MummyMazeState> {

    protected MummyMazeState initialEnvironment;

    public MummyMazeAgent(MummyMazeState environment) {
        super(environment);
        initialEnvironment = environment.clone();
        heuristics.add(new HeuristicGoalDistance());
        heuristics.add(new HeuristicEnemyDistance());
        heuristic = heuristics.get(0);
    }

    public MummyMazeState resetEnvironment() {
        environment = initialEnvironment.clone();
        return environment;
    }

    public MummyMazeState readInitialStateFromFile(File file) throws IOException {
        java.util.Scanner scanner = new java.util.Scanner(file);
        char[][] matrix = new char[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            String line = scanner.nextLine();
            matrix[i] = line.toCharArray();
        }

        initialEnvironment = new MummyMazeState(matrix);
        resetEnvironment();
        return environment;
    }
}
