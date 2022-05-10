package utils;

import agent.State;
import searchmethods.Node;

import java.util.HashMap;
import java.util.LinkedList;

public class NodeLinkedList extends LinkedList<Node> implements NodeCollection {

    private final HashMap<State, Node> contents;

    public NodeLinkedList() {
        super();
        contents = new HashMap<>(128);
    }

    @Override
    public void add(int index, Node element) {
        super.add(index, element);
        contents.put(element.getState(), element);
    }

    @Override
    public void clear() {
        super.clear();
        contents.clear();
    }

    @Override
    public boolean remove(Object o) {
        if (o instanceof Node) {
            Node no = (Node) o;
            contents.remove(no.getState());
        }
        return super.remove(o);
    }

    @Override
    public Node remove(int index) {
        Node no = super.remove(index);
        if (no != null) {
            contents.remove(no.getState());
        }
        return no;
    }

    @Override
    public Node removeFirst() {
        Node n = super.removeFirst();
        contents.remove(n.getState());
        return n;
    }

    @Override
    public Node poll() {
        Node n = super.poll();
        contents.remove(n.getState());
        return n;
    }

    @Override
    public boolean add(Node n) {
        contents.put(n.getState(), n);
        return super.add(n);
    }

    @Override
    public void addFirst(Node e) {
        super.addFirst(e);
        contents.put(e.getState(), e);
    }

    @Override
    public void addLast(Node e) {
        super.addLast(e);
        contents.put(e.getState(), e);
    }

    @Override
    public Node removeLast() {
        Node no = super.removeLast();
        contents.remove(no.getState());
        return no;
    }

    @Override
    public boolean containsState(State e) {
        return contents.containsKey(e);
    }

    public Node getNo(State e) {
        return contents.get(e);
    }

    public boolean removeNo(State e) {
        return remove(contents.get(e));
    }
}
