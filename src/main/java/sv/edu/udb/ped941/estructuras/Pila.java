package sv.edu.udb.ped941.estructuras;

public class Pila<T> {

    private Nodo<T> tope;

    private int tamanio;

    public void push(
            T dato) {

        Nodo<T> nuevo =
                new Nodo<>(dato);

        nuevo.siguiente =
                tope;

        tope =
                nuevo;

        tamanio++;
    }

    public T pop() {

        if (tope == null) {
            return null;
        }

        T dato =
                tope.dato;

        tope =
                tope.siguiente;

        tamanio--;

        return dato;
    }

    public T peek() {

        return tope == null
                ? null
                : tope.dato;
    }

    public boolean estaVacia() {

        return tope == null;
    }

    public int tamanio() {

        return tamanio;
    }
}
