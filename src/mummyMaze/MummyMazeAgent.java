package mummyMaze;

import agent.Agent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MummyMazeAgent extends Agent<MummyMazeState> {

    protected MummyMazeState initialEnvironment;

    public MummyMazeAgent(MummyMazeState environment) {
        super(environment);
        initialEnvironment = environment.clone();
        heuristics.add(new HeuristicTileDistance());
        heuristic = heuristics.get(0);
    }

    public MummyMazeState resetEnvironment() {
        environment = initialEnvironment.clone();
        return environment;
    }

    public MummyMazeState readInitialStateFromFile(File file) throws IOException {
        java.util.Scanner scanner = new java.util.Scanner(file);
        char[][] matrix = new char[13][13];
        for (int i = 0; i < 13; i++) {
            String line = scanner.nextLine();
            matrix[i] = line.toCharArray();
        }
        return new MummyMazeState(matrix);
    }
}
