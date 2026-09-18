package Puzzle8;

public class Node {
    private String state;
    private Node parent;
    private int depth;
    private int cost;

    public Node(String state, Node parent) {
        this.state = state;
        this.parent = parent;
    }

    public String getState() {
        return state;
    }

    void setState(String state) {
        this.state = state;
    }

    public Node getParent() {
        return parent;
    }

    void setParent(Node parent) {
        this.parent = parent;
    }

    public int getDepth() {
        return depth;
    }

    void setDepth(int depth) {
        this.depth = depth;
    }

    public int getCost() {
        return cost;
    }

    void setCost(int cost) {
        this.cost = cost;
    }
}