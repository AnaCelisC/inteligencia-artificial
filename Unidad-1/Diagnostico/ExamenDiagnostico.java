//Diagnóstico
//Nombre: Ana Paola Celis Corrales

class Nodo {
    String nombre;
    Nodo izq, der;

    public Nodo(String nombre) {
        this.nombre = nombre;
        this.izq = null;
        this.der = null;
    }
}

class Arbol {
    Nodo raiz;

    public Arbol() {
        this.raiz = null;
    }

    public boolean vacio() {
        return raiz == null;
    }

    public Nodo buscarNodo(String nombre) {
        Nodo actual = raiz;

        while (actual != null) {
            if (actual.nombre.equals(nombre)) {
                return actual;
            }

            if (nombre.compareTo(actual.nombre) < 0) {
                actual = actual.izq;
            } else {
                actual = actual.der;
            }
        }

        return null;
    }
}

