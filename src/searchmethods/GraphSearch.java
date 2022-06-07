package searchmethods;

import agent.Action;
import agent.Problem;
import agent.Solution;
import agent.State;
import utils.NodeCollection;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class GraphSearch<L extends NodeCollection> implements SearchMethod {

    protected L frontier;
    protected Set<State> explored = new HashSet<>();
    protected Statistics statistics = new Statistics();    
    protected boolean stopped;

    @Override
    public Solution search(Problem problem) {
        statistics.reset();
        stopped = false;
        return graphSearch(problem);
    }

    /*
     function GRAPH-SEARCH(problem) returns a solution, or failure
        initialize the frontier using the initial state of problem
        initialize the explored set to be empty
        while(frontier is not empty)
            remove the first node from the frontier
            if the node contains a goal state then return the corresponding solution
            add the node to the explored set
            expand the node, adding the resulting nodes to the frontier only if
                not in the frontier or explored set
        return failure
     */
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
            //System.out.println("Pai " + state);
            List<Action> actions = problem.getActions(state);
            for (Action action : actions) {
                State successor = problem.getSuccessor(state, action);
                addSuccessorsToFrontier(successor, node);
                //System.out.println("Filho " + successor);
            }
            computeStatistics(actions.size());
        }
        return null;
    }

    public abstract void addSuccessorsToFrontier(State successor, Node parent);

    protected void computeStatistics(int successorsSize) {
        statistics.numExpandedNodes++;
        statistics.numGeneratedNodes += successorsSize;
        statistics.maxFrontierSize = Math.max(statistics.maxFrontierSize, frontier.size());
    }
    
    @Override
    public Statistics getStatistics(){
        return statistics;
    }

    @Override
    public void stop() {
        stopped = true;
    }

    @Override
    public boolean hasBeenStopped() {
        return stopped;
    }
}
    