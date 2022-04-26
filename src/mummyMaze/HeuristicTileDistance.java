package mummyMaze;

import agent.Heuristic;

public class HeuristicTileDistance extends Heuristic<GameProblem, GameState>{

    @Override
    public double compute(GameState state){
        return state.computeTileDistances(problem.getGoalState());
    }
    
    @Override
    public String toString(){
        return "Tiles distance to final position";
    }
}