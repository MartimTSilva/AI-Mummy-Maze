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

    public String getCsvSearchReport() {
        StringBuilder sb = new StringBuilder();
        sb.append(searchMethod).append(";");
        sb.append((heuristic == null ? "N/A" : heuristic)).append(";");

        if (searchMethod instanceof BeamSearch bs) {
            sb.append(bs.getBeamSize());
        } else if (searchMethod instanceof DepthLimitedSearch dls) {
            sb.append(dls.getLimit());
        } else
            sb.append("N/A");

        sb.append(";");
        if (solution == null) {
            sb.append("NO;N/A;");
        } else {
            sb.append((hasBeenStopped() ? "STOPPED" : "YES")).append(";").append(solution.getCost()).append(";");
        }
        sb.append(searchMethod.getStatistics().numExpandedNodes).append(";");
        sb.append(searchMethod.getStatistics().maxFrontierSize).append(";");
        sb.append(searchMethod.getStatistics().numGeneratedNodes);

        return sb.toString();
    }
}
