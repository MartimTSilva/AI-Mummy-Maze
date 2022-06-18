package mummyMaze;

import agent.Heuristic;

public class HeuristicGoalAndEnemyDistance extends Heuristic<MummyMazeProblem, MummyMazeState>{

    @Override
    public double compute(MummyMazeState state){
        return state.computeGoalDistance() + state.computeEnemyDistances();
    }
    
    @Override
    public String toString(){
        return "Distance to exit and enemy position";
    }
}