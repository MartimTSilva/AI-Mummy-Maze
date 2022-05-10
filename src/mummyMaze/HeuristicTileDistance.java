package mummyMaze;

import agent.Heuristic;

public class HeuristicTileDistance extends Heuristic<MummyMazeProblem, MummyMazeState>{

    @Override
    public double compute(MummyMazeState state){
        //TODO:
        return state.computeGoalDistance();
    }
    
    @Override
    public String toString(){
        return "Tiles distance to final position";
    }
}