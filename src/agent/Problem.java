package agent;

import java.util.List;

public abstract class Problem<S extends State> {
    protected S initialState;
    protected Heuristic heuristic;

    public Problem(S initialState) {
        this.initialState = initialState;
    }

    public abstract List<Action<S>> getActions(S state);

    public abstract State getSuccessor(S state, Action action);

    public abstract boolean isGoal(S state);

    public double computePathCost(List<Action> path) {
        double pathCost = 0;
        for (Action action : path) {
            pathCost += action.getCost();
        }
        return pathCost;
    }

    public S getInitialState() {
        return initialState;
    }

    public Heuristic getHeuristic() {
        return heuristic;
    }

    public void setHeuristic(Heuristic heuristic) {
        this.heuristic = heuristic;
    }
}
