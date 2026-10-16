package sv.edu.udb.ped941.estructuras;

public class Cola<T> {

    private Nodo<T> frente;
    private Nodo<T> finalCola;

    private int tamanio;

    public void encolar(
            T dato) {

        Nodo<T> nuevo =
                new Nodo<>(dato);

        if (frente == null) {

            frente = nuevo;
            finalCola = nuevo;

        } else {

            finalCola.siguiente =
                    nuevo;

            finalCola =
                    nuevo;
        }

        tamanio++;
    }

    public T desencolar() {

        if (estaVacia()) {
            return null;
        }

        T dato =
                frente.dato;

        frente =
                frente.siguiente;

        tamanio--;

        if (frente == null) {

            finalCola =
                    null;
        }

        return dato;
    }

    public T frente() {

        return frente == null
                ? null
                : frente.dato;
    }

    public boolean estaVacia() {

        return frente == null;
    }

    public int tamanio() {

        return tamanio;
    }
}
