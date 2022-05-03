package mummyMaze;

import agent.Action;
import agent.Problem;

import java.util.LinkedList;
import java.util.List;

public class MummyMazeProblem extends Problem<MummyMazeState> {
    private List<Action> actions;
    private MummyMazeState goalState;

    public MummyMazeProblem(MummyMazeState initialState) {
        super(initialState);
        this.actions = new LinkedList<>();
        this.actions.add(new ActionLeft());
        this.actions.add(new ActionDown());
        this.actions.add(new ActionRight());
        this.actions.add(new ActionUp());
        this.actions.add(new ActionStop());
    }

    public List<Action<MummyMazeState>> getActions(MummyMazeState state) {
        List<Action<MummyMazeState>> possibleActions = new LinkedList<>();
        for (Action action : actions){
            if (action.isValid(state))
                possibleActions.add(action);
        }

        return possibleActions;
    }

    public MummyMazeState getSuccessor(MummyMazeState state, Action action) {
        MummyMazeState successor = state.clone();
        action.execute(successor);
        return successor;
    }

    public boolean isGoal(MummyMazeState state) {
        return state.equals(goalState);
    }

    public double computePathCost(List<Action> path) {
        return path.size();
    }

    public MummyMazeState getGoalState() {
        return goalState;
    }
}
