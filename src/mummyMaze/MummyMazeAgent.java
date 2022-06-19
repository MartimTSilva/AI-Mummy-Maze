package mummyMaze;

import agent.Agent;
import searchmethods.BeamSearch;
import searchmethods.DepthLimitedSearch;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static mummyMaze.MummyMazeState.SIZE;

public class MummyMazeAgent extends Agent<MummyMazeState> {

    protected MummyMazeState initialEnvironment;

    public MummyMazeAgent(MummyMazeState environment) {
        super(environment);
        initialEnvironment = environment.clone();
        heuristics.add(new HeuristicGoalDistance());
        heuristics.add(new HeuristicEnemyDistance());
        heuristics.add(new HeuristicGoalAndEnemyDistance());
        heuristic = heuristics.get(0);
    }

    public MummyMazeState resetEnvironment() {
        environment = initialEnvironment.clone();
        return environment;
    }

    public MummyMazeState readInitialStateFromFile(File file) throws IOException {
        Cell exit = null;
        ArrayList<Cell> doors = null, keys = null, traps = null;

        java.util.Scanner scanner = new java.util.Scanner(file);
        char[][] matrix = new char[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            String line = scanner.nextLine();
            matrix[i] = line.toCharArray();
            for (int j = 0; j < SIZE; j++) {
                switch (matrix[i][j]) {
                    case Cell.EXIT -> exit = new Cell(i, j, matrix[i][j]);
                    case Cell.TRAP -> {
                        if (traps == null) {
                            traps = new ArrayList<>();
                        }
                        traps.add(new Cell(i, j, matrix[i][j]));
                    }
                    case Cell.HORIZ_DOOR_CLOSED, Cell.HORIZ_DOOR_OPEN, Cell.VERT_DOOR_CLOSED, Cell.VERT_DOOR_OPEN -> {
                        if (doors == null) {
                            doors = new ArrayList<>();
                        }
                        doors.add(new Cell(i, j, matrix[i][j]));
                    }
                    case Cell.KEY -> {
                        if (keys == null) {
                            keys = new ArrayList<>();
                        }
                        keys.add(new Cell(i, j, matrix[i][j]));
                    }
                }
            }
        }
        initialEnvironment = new MummyMazeState(matrix, exit, doors, keys, traps);
        resetEnvironment();
        return environment;
    }

    public String getStatistics() {
        StringBuilder str = new StringBuilder();
        str.append(searchMethod).append(";");
        str.append(heuristic).append(";");

        if (solution == null){
            str.append("No solution found").append(";").append("0").append(";");
            str.append(searchMethod.getStatistics().numExpandedNodes).append(";");
            str.append(searchMethod.getStatistics().maxFrontierSize).append(";");
            str.append(searchMethod.getStatistics().numGeneratedNodes).append(";");
            return str.toString();
        }
        str.append("Yes").append(";").append(solution.getCost()).append(";");

        str.append(searchMethod.getStatistics().numExpandedNodes).append(";");
        str.append(searchMethod.getStatistics().maxFrontierSize).append(";");
        str.append(searchMethod.getStatistics().numGeneratedNodes).append(";");

        return str.toString();
    }

}
