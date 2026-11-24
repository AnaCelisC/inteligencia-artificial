
package Puzzle8;

public class App {

    public static void main(String[] args) throws Exception {

        String initialState = "7621 3458";
        String goalState = "12345678 ";

        SearchTree searchTree = new SearchTree(initialState, goalState);

        //NOMBRE
                System.out.println("*******ANA PAOLA CELIS CORRALES*******");

        // BUSQUEDA EN ANCHURA
        System.out.println("*******BUSQUEDA EN ANCHURA*******");

        searchTree.busquedaPrimeroAnchura();


        // BUSQUEDA EN PROFUNDIDAD
        System.out.println("*******BUSQUEDA EN PROFUNDIDAD*******");

        searchTree.busquedaProfundidad();


        // BUSQUEDA DE COSTO UNIFORME
        System.out.println("*******BUSQUEDA DE COSTO UNIFORME*******");

        searchTree.busquedaCostoUniforme();


        // BUSQUEDA EN PROFUNDIDAD LIMITADA
        System.out.println("*******BUSQUEDA EN PROFUNDIDAD LIMITADA*******");

        if (!searchTree.busquedaProfundidadLimitada(10)) {
            System.out.println("Limite: 10");
            System.out.println("No se encontro solucion con el limite establecido");
        }

        System.out.println();


        // BUSQUEDA EN PROFUNDIDAD ITERATIVA
        System.out.println("*******BUSQUEDA EN PROFUNDIDAD ITERATIVA*******");

        searchTree.busquedaProfundidadIterativa();


        // BUSQUEDA BIDIRECCIONAL
        System.out.println("*******BUSQUEDA BIDIRECCIONAL*******");

        searchTree.busquedaBidireccional();


        System.out.println("Fin");
    }
}
