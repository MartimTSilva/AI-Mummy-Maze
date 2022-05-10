package searchmethods;

import agent.State;
import utils.NodeLinkedList;

public class BreadthFirstSearch extends GraphSearch<NodeLinkedList> {

    public BreadthFirstSearch() {
        frontier = new NodeLinkedList();
    }

    @Override
    public void addSuccessorsToFrontier(State successor, Node parent) {
        if (!explored.contains(successor) && !frontier.containsState(successor))
            frontier.addLast(new Node(successor, parent));
    }

    @Override
    public String toString() {
        return "Breadth first search";
    }
}