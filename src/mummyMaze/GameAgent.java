package mummyMaze;

import agent.Agent;

import java.io.File;
import java.io.IOException;

public class GameAgent extends Agent<GameState>{

    protected GameState initialEnvironment;

    public GameAgent(GameState environment) {
        super(environment);
        initialEnvironment = environment.clone();
        heuristics.add(new HeuristicTileDistance());
        heuristic = heuristics.get(0);
    }
            
    public GameState resetEnvironment(){
        environment = initialEnvironment.clone();
        return environment;
    }
                 
    public String readInitialStateFromFile(File file) throws IOException {
        java.util.Scanner scanner = new java.util.Scanner(file);
        StringBuilder state = new StringBuilder();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            state.append(line + "\n");
        }

        /*int[][] matrix = new int [3][3];
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                matrix[i][j] = scanner.nextInt();
            }
            scanner.nextLine();
        }
        initialEnvironment = new GameState(matrix);
        resetEnvironment();*/

        return state.toString();
    }
}
