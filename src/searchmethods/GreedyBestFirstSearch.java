package searchmethods;

import agent.State;

public class GreedyBestFirstSearch extends InformedSearch {

    //f = h
    @Override
    public void addSuccessorsToFrontier(State successor, Node parent) {
        double g = parent.getG() + successor.getAction().getCost();

        if (!frontier.containsState(successor)) {
            if (!explored.contains(successor)) {
                frontier.add(new Node(successor, parent, g, heuristic.compute(successor)));
            }
        } else if (g < frontier.getNode(successor).getG()) {
            frontier.removeNode(successor);
            frontier.add(new Node(successor, parent, g, heuristic.compute(successor)));
        }
    }

    @Override
    public String toString() {
        return "Greedy best first search";
    }
}