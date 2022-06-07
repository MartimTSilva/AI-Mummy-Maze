package mummyMaze;

import agent.Heuristic;

public class HeuristicEnemyDistance extends Heuristic<MummyMazeProblem, MummyMazeState>{

    @Override
    public double compute(MummyMazeState state){
        return state.computeEnemyDistances();
    }
    
    @Override
    public String toString(){
        return "Distance to enemy position";
    }
}