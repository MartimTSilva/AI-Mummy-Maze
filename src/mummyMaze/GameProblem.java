package mummyMaze;

import agent.Action;
import agent.Problem;

import java.util.LinkedList;
import java.util.List;

public class GameProblem extends Problem<GameState> {
    private List<Action> actions;
    private GameState goalState;

    public GameProblem(GameState initialState) {
        super(initialState);
        this.actions = new LinkedList<>();
        this.actions.add(new ActionLeft());
        this.actions.add(new ActionDown());
        this.actions.add(new ActionRight());
        this.actions.add(new ActionUp());
        this.actions.add(new ActionStop());
        goalState = new GameState(GameState.GOAL_MATRIX);
    }

    public List<Action<GameState>> getActions(GameState state) {
        List<Action<GameState>> possibleActions = new LinkedList<>();
        for (Action action : actions){
            if (action.isValid(state))
                possibleActions.add(action);
        }

        return possibleActions;
    }

    public GameState getSuccessor(GameState state, Action action) {
        GameState successor = state.clone();
        action.execute(successor);
        return successor;
    }

    public boolean isGoal(GameState state) {
        return state.equals(goalState);
    }

    public double computePathCost(List<Action> path) {
        return path.size();
    }

    public GameState getGoalState() {
        return goalState;
    }
}
