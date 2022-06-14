package searchmethods;

import agent.Action;
import agent.Problem;
import agent.Solution;
import agent.State;

import java.util.List;

public class IDAStarSearch extends InformedSearch {
    /*
     * Note that, on each iteration, the search is done in a depth first search way.
     */

    private double limit;
    private double newLimit;

    @Override
    public Solution search(Problem problem) {
        statistics.reset();
        stopped = false;

        this.heuristic = problem.getHeuristic();
        limit = heuristic.compute(problem.getInitialState());

        Solution solution = null;
        do {
            solution = graphSearch(problem);
            if (limit == newLimit)
                stopped = true;
            limit = newLimit;
        } while (solution == null && !stopped);

        return solution;
    }

    // Same as the DepthFirstSearch
    @Override
    protected Solution graphSearch(Problem problem) {
        newLimit = Double.MAX_VALUE;
        frontier.clear();
        frontier.add(new Node(problem.getInitialState()));

        while (!frontier.isEmpty() && !stopped) {
            Node node = frontier.poll();
            State state = node.getState();

            if (problem.isGoal(state))
                return new Solution(problem, node);

            List<Action> actions = problem.getActions(state);
            for (Action action : actions) {
                State successor = problem.getSuccessor(state, action);
                addSuccessorsToFrontier(successor, node);
            }
            computeStatistics(actions.size());
        }
        return null;
    }

    @Override
    public void addSuccessorsToFrontier(State successor, Node parent) {
        double g = parent.getG() + successor.getAction().getCost();
        if (!frontier.containsState(successor)) {
            double f = g + heuristic.compute(successor);
            if (f <= limit) {
                if (!parent.isCycle(successor)) {
                    frontier.add(new Node(successor, parent, g, f));
                }
            } else {
                newLimit = Math.min(newLimit, f);
            }
        } else if (frontier.getNode(successor).getG() > g) {
            frontier.removeNode(successor);
            frontier.add(new Node(successor, parent, g, g + heuristic.compute(successor)));
        }
    }

    @Override
    public String toString() {
        return "IDA* search";
    }
}
