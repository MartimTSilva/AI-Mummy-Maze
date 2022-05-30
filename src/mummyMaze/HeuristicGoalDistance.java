package mummyMaze;

import agent.Heuristic;

public class HeuristicGoalDistance extends Heuristic<MummyMazeProblem, MummyMazeState>{

    @Override
    public double compute(MummyMazeState state){
        return state.computeGoalDistance();
    }
    
    @Override
    public String toString(){
        return "Distance to exit position";
    }
}