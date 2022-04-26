package searchmethods;

import agent.Action;
import agent.Problem;
import agent.Solution;
import agent.State;
import utils.NodeLinkedList;

import java.util.List;

public class DepthFirstSearch extends GraphSearch<NodeLinkedList> {

    public DepthFirstSearch() {
        frontier = new NodeLinkedList();
    }

    //Graph Search without explored list
    @Override
    protected Solution graphSearch(Problem problem) {
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
        if (!frontier.containsState(successor)) {
            if (!parent.isCycle(successor)) {
                frontier.addFirst(new Node(successor, parent));
            }
        }
    }

    @Override
    public String toString() {
        return "Depth first search";
    }
}
