package sv.edu.udb.ped941.estructuras;

class Nodo<T> {

    T dato;
    Nodo<T> siguiente;

    Nodo(T dato) {

        this.dato = dato;
        this.siguiente = null;
    }
}
