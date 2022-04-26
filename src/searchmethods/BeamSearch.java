package searchmethods;

import agent.Action;
import agent.Problem;
import agent.Solution;
import agent.State;
import utils.NodePriorityQueue;

import java.util.List;

public class BeamSearch extends AStarSearch {

    private int beamSize;

    public BeamSearch() {
        this(100);
    }

    public BeamSearch(int beamSize) {
        this.beamSize = beamSize;
    }

    @Override
    protected Solution graphSearch(Problem problem) {
        frontier.clear();
        explored.clear();
        frontier.add(new Node(problem.getInitialState()));

        while (!frontier.isEmpty() && !stopped) {
            Node node = frontier.poll();
            State state = node.getState();

            if (problem.isGoal(state))
                return new Solution(problem, node);

            explored.add(state);
            List<Action> actions = problem.getActions(state);
            for (Action action : actions) {
                State successor = problem.getSuccessor(state, action);
                addSuccessorsToFrontier(successor, node);
            }
            manageFrontierSize();
            computeStatistics(actions.size());
        }
        return null;
    }

    private void manageFrontierSize() {
        if (frontier.size() > beamSize) {
            NodePriorityQueue aux = new NodePriorityQueue();
            for (int i = 0; i < beamSize; i++) {
                aux.add(frontier.poll());
            }
            frontier = aux;
        }
    }

    public void setBeamSize(int beamSize) {
        this.beamSize = beamSize;
    }

    public int getBeamSize() {
        return beamSize;
    }

    @Override
    public String toString() {
        return "Beam search";
    }
}
