package Puzzle8;

import java.util.List;
import java.util.PriorityQueue;

public class App {
    public static void main(String[] args) throws Exception {

        
                                // 3x3 = 9
        String initialState = "7621 3458"; // random initial state
        String goalState = "12345678 "; // goal state
        SearchTree searchTree = new SearchTree(initialState, goalState);
        searchTree.UniformCostSearch();
        // searchTree.breadthFirstSearch();
        // searchTree.deepFirstSearch();
        System.out.println("End");

        System.out.println("INITIAL STATE> " + initialState);
        List<Node> children = NodeUtils.generateChildren(new Node(initialState, null));
        for (Node node : children) {
            System.out.println(node.getState());
        }


        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(new NodePriorityComparator());
        Node n1 = new Node ("n1", null);
        n1.setCost(5);

        Node n2 = new Node ("n2", null);
        n2.setCost(3);

        Node n3 = new Node ("n3", null);
        n3.setCost(7);

        queue.add(n1);
        queue.add(n2);
        queue.add(n3);

    }
}
