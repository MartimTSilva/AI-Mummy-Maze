package searchmethods;

import agent.Action;
import agent.Problem;
import agent.Solution;
import agent.State;

import java.util.List;

public class DepthLimitedSearch extends DepthFirstSearch {

    protected int limit;

    public DepthLimitedSearch() {
        this(28);
    }

    public DepthLimitedSearch(int limit) {
        this.limit = limit;
    }

    @Override
    public void addSuccessorsToFrontier(State successor, Node parent) {
        if (parent.getDepth() < limit) {
            super.addSuccessorsToFrontier(successor, parent);
        }
    }

    @Override
    protected Solution graphSearch(Problem problem) {
        frontier.clear();
        frontier.add(new Node(problem.getInitialState()));

        while (!frontier.isEmpty() && !stopped) {
            Node n = frontier.poll();
            State state = n.getState();
            if (problem.isGoal(state)) {
                return new Solution(problem, n);
            }
            int successorsSize = 0;
            if (n.getDepth() < limit) {
                List<Action> actions = problem.getActions(state);
                successorsSize = actions.size();
                for(Action action : actions){
                    State successor = problem.getSuccessor(state, action);
                    addSuccessorsToFrontier(successor, n);
                }
            }
            computeStatistics(successorsSize);
        }
        return null;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public String toString() {
        return "Limited depth first search";
    }
}
