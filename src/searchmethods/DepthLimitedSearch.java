package searchmethods;

import agent.State;

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
