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


    // BUSQUEDA PRIMERO EN ANCHURA
    public void busquedaPrimeroAnchura() {

        int time = 0;

        Set<String> visited = new HashSet<String>();

        Node currentNode = root;

        Queue<Node> queue = new LinkedList<>();

        queue.add(currentNode);

        while (!queue.isEmpty()) {

            time++;

            currentNode = queue.poll();

            // Si ya fue visitado no lo volvemos a revisar
            if (visited.contains(currentNode.getState())) {
                continue;
            }

            visited.add(currentNode.getState());

            if (currentNode.getState().equals(goalState)) {

                System.out.println("Estado objetivo encontrado:");
                printPath(currentNode);

                System.out.println("Profundidad: " + currentNode.getDepth());
                System.out.println("Iteraciones: " + time);
                System.out.println("Estados visitados: " + visited.size());

                return;
            }

            List<Node> children = NodeUtils.generateChildren(currentNode);

            for (Node child : children) {

                if (!visited.contains(child.getState())) {
                    queue.add(child);
                }
            }
        }

        System.out.println("No se encontro solucion");
    }


    // BUSQUEDA EN PROFUNDIDAD
    public void busquedaProfundidad() {

        int time = 0;

        Set<String> visited = new HashSet<String>();

        Node currentNode = root;

        Stack<Node> stack = new Stack<Node>();

        stack.push(currentNode);

        while (!stack.isEmpty()) {

            time++;

            currentNode = stack.pop();

            // Evita revisar estados repetidos
            if (visited.contains(currentNode.getState())) {
                continue;
            }

            visited.add(currentNode.getState());

            if (currentNode.getState().equals(goalState)) {

                System.out.println("Estado objetivo encontrado:");
                printPath(currentNode);

                System.out.println("Profundidad: " + currentNode.getDepth());
                System.out.println("Iteraciones: " + time);
                System.out.println("Estados visitados: " + visited.size());

                return;
            }

            List<Node> children = NodeUtils.generateChildren(currentNode);

            for (Node child : children) {

                if (!visited.contains(child.getState())) {
                    stack.push(child);
                }
            }
        }

        System.out.println("No se encontro solucion");
    }


    // BUSQUEDA DE COSTO UNIFORME
    public void busquedaCostoUniforme() {

        int time = 0;

        Set<String> visited = new HashSet<String>();

        Node currentNode = root;

        PriorityQueue<Node> queue =
                new PriorityQueue<>(new NodePriorityComparator());

        root.setCost(0);

        queue.add(currentNode);

        while (!queue.isEmpty()) {

            time++;

            currentNode = queue.poll();

            if (visited.contains(currentNode.getState())) {
                continue;
            }

            visited.add(currentNode.getState());

            if (currentNode.getState().equals(goalState)) {

                System.out.println("Estado objetivo encontrado:");
                printPath(currentNode);

                System.out.println("Costo: " + currentNode.getCost());
                System.out.println("Profundidad: " + currentNode.getDepth());
                System.out.println("Iteraciones: " + time);
                System.out.println("Estados visitados: " + visited.size());

                return;
            }

            List<Node> children = NodeUtils.generateChildren(currentNode);

            for (Node child : children) {

                if (!visited.contains(child.getState())) {

                    // Cada movimiento tiene costo 1
                    child.setCost(currentNode.getCost() + 1);

                    queue.add(child);
                }
            }
        }

        System.out.println("No se encontro solucion");
    }


    // BUSQUEDA EN PROFUNDIDAD LIMITADA
    public boolean busquedaProfundidadLimitada(int limite) {

        int time = 0;

        Set<String> visited = new HashSet<String>();

        Stack<Node> stack = new Stack<Node>();

        stack.push(root);

        while (!stack.isEmpty()) {

            time++;

            Node currentNode = stack.pop();

            if (currentNode.getState().equals(goalState)) {

                System.out.println("Estado objetivo encontrado:");
                printPath(currentNode);

                System.out.println("Limite: " + limite);
                System.out.println("Profundidad: " + currentNode.getDepth());
                System.out.println("Iteraciones: " + time);

                return true;
            }

            if (currentNode.getDepth() < limite) {

                visited.add(currentNode.getState());

                List<Node> children =
                        NodeUtils.generateChildren(currentNode);

                for (Node child : children) {

                    if (!visited.contains(child.getState())) {
                        stack.push(child);
                    }
                }
            }
        }

        return false;
    }


    // BUSQUEDA EN PROFUNDIDAD ITERATIVA
    public void busquedaProfundidadIterativa() {

        // Probamos diferentes limites
        for (int limite = 0; limite <= 50; limite++) {

            System.out.println("Buscando con limite: " + limite);

            // Creamos nuevamente la raiz
            root = new Node(initialState, null);

            boolean encontrado = busquedaProfundidadLimitada(limite);

            if (encontrado) {
                return;
            }
        }

        System.out.println("No se encontro solucion");
    }


    // BUSQUEDA BIDIRECCIONAL
    public void busquedaBidireccional() {

        // Una cola empieza desde el inicio
        Queue<Node> queueInicio = new LinkedList<Node>();

        // Otra cola empieza desde la meta
        Queue<Node> queueMeta = new LinkedList<Node>();

        Set<String> visitadosInicio = new HashSet<String>();
        Set<String> visitadosMeta = new HashSet<String>();

        Node inicio = new Node(initialState, null);
        Node meta = new Node(goalState, null);

        queueInicio.add(inicio);
        queueMeta.add(meta);

        int time = 0;

        while (!queueInicio.isEmpty() && !queueMeta.isEmpty()) {

            time++;

            // BUSQUEDA DESDE EL INICIO

            Node actualInicio = queueInicio.poll();

            visitadosInicio.add(actualInicio.getState());

            // Revisamos si las dos busquedas se encontraron
            if (visitadosMeta.contains(actualInicio.getState())) {

                System.out.println("Las busquedas se encontraron en:");
                System.out.println(
                    NodeUtils.formatState(actualInicio.getState())
                );

                System.out.println("Iteraciones: " + time);

                return;
            }

            List<Node> hijosInicio =
                    NodeUtils.generateChildren(actualInicio);

            for (Node child : hijosInicio) {

                if (!visitadosInicio.contains(child.getState())) {
                    queueInicio.add(child);
                }
            }


            // BUSQUEDA DESDE LA META

            Node actualMeta = queueMeta.poll();

            visitadosMeta.add(actualMeta.getState());

            // Revisamos si las dos busquedas se encontraron
            if (visitadosInicio.contains(actualMeta.getState())) {

                System.out.println("Las busquedas se encontraron en:");
                System.out.println(
                    NodeUtils.formatState(actualMeta.getState())
                );

                System.out.println("Iteraciones: " + time);

                return;
            }

            List<Node> hijosMeta =
                    NodeUtils.generateChildren(actualMeta);

            for (Node child : hijosMeta) {

                if (!visitadosMeta.contains(child.getState())) {
                    queueMeta.add(child);
                }
            }
        }

        System.out.println("No se encontro solucion");
    }


    // IMPRIMIR EL CAMINO
    private void printPath(Node node) {

        if (node == null) {
            return;
        }

        printPath(node.getParent());

        System.out.println(
            NodeUtils.formatState(node.getState())
        );
    }
}