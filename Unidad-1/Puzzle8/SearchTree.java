package Puzzle8;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public class SearchTree {
    Node root;
    String initialState;
    String goalState;

    public SearchTree(String initialState, String goalState) {
        this.initialState = initialState;
        this.goalState = goalState;
        this.root = new Node(initialState, null);
    }

    public void breadthFirstSearch() {
        int time = 0;
        // Crear estructura de datos para almacenar los nodos visitados
        Set<String> visited = new HashSet<String>();
        // 1. Buscar el nodo raíz y agregarlo a la cola
        Node currentNode = root;
        Queue<Node> queue = new LinkedList<>();
        queue.add(currentNode);
        // 2. Mientras la cola no esté vacía, hacer lo siguiente:
        while (!queue.isEmpty()) {
            time++;
            // 3. Sacar el primer nodo de la cola y verificar si es el nodo objetivo
            currentNode = queue.poll();
            visited.add(currentNode.getState());
            // System.out.println(NodeUtils.formatState(currentNode.getState()));
            if(currentNode.getState().equals(goalState)) {
                System.out.println("Goal state found: " + currentNode.getState());
                // print the path from root to goal
                printPath(currentNode);
                break;
            }
            // 4. Si no es el nodo objetivo, generar sus hijos y agregarlos a la cola
            List<Node> children = NodeUtils.generateChildren(currentNode);
            for (Node child : children) {
                if (!visited.contains(child.getState()))
                    queue.add(child);
            }
        }

        // Datos estadisticos...
        System.out.println("Time: " + time);
        System.out.println("Estados Visitados: " + visited.size());
        System.out.println("Queue: " + queue.size());
    }

    public void deepFirstSearch() {
        int time = 0;
        // Crear estructura de datos para almacenar los nodos visitados
        Set<String> visited = new HashSet<String>();
        // 1. Buscar el nodo raíz y agregarlo a la cola
        Node currentNode = root;
        Stack<Node> stack = new Stack<Node>();  // Cambiar de Queue a Stack para DFS
        stack.push(currentNode);
        // 2. Mientras la cola no esté vacía, hacer lo siguiente:
        while (!stack.isEmpty()) {
            time++;
            // 3. Sacar el primer nodo de la cola y verificar si es el nodo objetivo
            currentNode = stack.pop();
            visited.add(currentNode.getState());
            // System.out.println(NodeUtils.formatState(currentNode.getState()));
            if(currentNode.getState().equals(goalState)) {
                System.out.println("Goal state found: " + currentNode.getState());
                // print the path from root to goal
                printPath(currentNode);
                break;
            }
            // 4. Si no es el nodo objetivo, generar sus hijos y agregarlos a la cola
            List<Node> children = NodeUtils.generateChildren(currentNode);
            for (Node child : children) {
                if (!visited.contains(child.getState()))
                    stack.add(child);
            }
        }

        // Datos estadisticos...
        System.out.println("Time: " + time);
        System.out.println("Estados Visitados: " + visited.size());
        System.out.println("Stack: " + stack.size());
    }

    public void UniformCostSearch() {
        int time = 0;
        // Crear estructura de datos para almacenar los nodos visitados
        Set<String> visited = new HashSet<String>();
        // 1. Buscar el nodo raíz y agregarlo a la cola
        Node currentNode = root;
        // PriorityQueue<Node> queue = new PriorityQueue<>((n1, n2) -> Integer.compare(n1.getCost(), n2.getCost()));
        PriorityQueue<Node> queue = new PriorityQueue<>(new NodePriorityComparator());
        queue.add(currentNode);
        // 2. Mientras la cola no esté vacía, hacer lo siguiente:
        while (!queue.isEmpty()) {
            time++;
            // 3. Sacar el primer nodo de la cola y verificar si es el nodo objetivo
            currentNode = queue.poll();
            visited.add(currentNode.getState());
            // System.out.println(NodeUtils.formatState(currentNode.getState()));
            if(currentNode.getState().equals(goalState)) {
                System.out.println("Goal state found: " + currentNode.getState());
                // print the path from root to goal
                printPath(currentNode);
                break;
            }
            // 4. Si no es el nodo objetivo, generar sus hijos y agregarlos a la cola
            List<Node> children = NodeUtils.generateChildren(currentNode);
            for (Node child : children) {
                if (!visited.contains(child.getState())){
                    // Falto agregar la profundidad del hijo antes de calcular el costo
                    child.setCost(child.getParent().getDepth() + 1); // Set cost as depth for uniform cost search
                    queue.add(child);
                }   
            }
        }


       /*  private int heuristic(string state) {
            // Implement your heuristic function here
            // For example, you can use the Manhattan distance or the number of misplaced tiles
            return 0; // Placeholder
        }*/

        // Datos estadisticos...
        System.out.println("Time: " + time);
        System.out.println("Estados Visitados: " + visited.size());
        System.out.println("Queue: " + queue.size());
    }

    private void printPath(Node node) {
        if (node == null) {
            return;
        }
        printPath(node.getParent());
        System.out.println(NodeUtils.formatState(node.getState()));
    }

}
