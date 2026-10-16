package sv.edu.udb.ped941.arbol;

import sv.edu.udb.ped941.estructuras.ListaEnlazada;

public class NodoArbol {

    private final String nombre;

    private final ListaEnlazada<NodoArbol>
            hijos;

    public NodoArbol(
            String nombre) {

        this.nombre =
                nombre;

        this.hijos =
                new ListaEnlazada<>();
    }

    public String getNombre() {

        return nombre;
    }

    public ListaEnlazada<NodoArbol>
    getHijos() {

        return hijos;
    }

    public void agregarHijo(
            NodoArbol hijo) {

        hijos.agregar(
                hijo);
    }
}
