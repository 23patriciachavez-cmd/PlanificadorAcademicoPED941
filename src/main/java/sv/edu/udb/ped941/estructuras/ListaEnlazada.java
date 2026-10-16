package sv.edu.udb.ped941.estructuras;

public class ListaEnlazada<T> {

    private Nodo<T> cabeza;
    private Nodo<T> cola;
    private int tamanio;

    public void agregar(
            T dato) {

        Nodo<T> nuevo =
                new Nodo<>(dato);

        if (cabeza == null) {

            cabeza = nuevo;
            cola = nuevo;

        } else {

            cola.siguiente =
                    nuevo;

            cola =
                    nuevo;
        }

        tamanio++;
    }

    public boolean eliminar(
            T dato) {

        Nodo<T> actual =
                cabeza;

        Nodo<T> anterior =
                null;

        while (actual != null) {

            boolean iguales =
                    dato == null
                    ? actual.dato == null
                    : dato.equals(
                            actual.dato);

            if (iguales) {

                if (anterior == null) {

                    cabeza =
                            actual.siguiente;

                } else {

                    anterior.siguiente =
                            actual.siguiente;
                }

                if (actual == cola) {

                    cola =
                            anterior;
                }

                tamanio--;

                return true;
            }

            anterior =
                    actual;

            actual =
                    actual.siguiente;
        }

        return false;
    }

    public boolean contiene(
            T dato) {

        Nodo<T> actual =
                cabeza;

        while (actual != null) {

            boolean iguales =
                    dato == null
                    ? actual.dato == null
                    : dato.equals(
                            actual.dato);

            if (iguales) {
                return true;
            }

            actual =
                    actual.siguiente;
        }

        return false;
    }

    public T obtener(
            int indice) {

        if (indice < 0
                || indice >= tamanio) {

            throw new IndexOutOfBoundsException(
                    "Índice fuera de rango.");
        }

        Nodo<T> actual =
                cabeza;

        for (int i = 0;
             i < indice;
             i++) {

            actual =
                    actual.siguiente;
        }

        return actual.dato;
    }

    public int tamanio() {
        return tamanio;
    }

    public boolean estaVacia() {
        return tamanio == 0;
    }

    public void limpiar() {

        cabeza = null;
        cola = null;
        tamanio = 0;
    }

    @Override
    public String toString() {

        StringBuilder texto =
                new StringBuilder("[");

        Nodo<T> actual =
                cabeza;

        while (actual != null) {

            texto.append(
                    actual.dato);

            if (actual.siguiente != null) {

                texto.append(", ");
            }

            actual =
                    actual.siguiente;
        }

        texto.append("]");

        return texto.toString();
    }
}
