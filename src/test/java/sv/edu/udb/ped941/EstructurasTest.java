package sv.edu.udb.ped941;

import org.junit.jupiter.api.Test;

import sv.edu.udb.ped941.estructuras.Cola;
import sv.edu.udb.ped941.estructuras.ListaEnlazada;
import sv.edu.udb.ped941.estructuras.Pila;
import sv.edu.udb.ped941.estructuras.TablaHash;

import static org.junit.jupiter.api.Assertions.*;

class EstructurasTest {

    @Test
    void listaAgregarYEliminar() {

        ListaEnlazada<String> lista =
                new ListaEnlazada<>();

        lista.agregar("A");
        lista.agregar("B");

        assertEquals(
                2,
                lista.tamanio());

        assertTrue(
                lista.eliminar("A"));

        assertEquals(
                1,
                lista.tamanio());
    }

    @Test
    void colaRespetaFIFO() {

        Cola<String> cola =
                new Cola<>();

        cola.encolar("A");
        cola.encolar("B");

        assertEquals(
                "A",
                cola.desencolar());

        assertEquals(
                "B",
                cola.desencolar());
    }

    @Test
    void pilaRespetaLIFO() {

        Pila<String> pila =
                new Pila<>();

        pila.push("A");
        pila.push("B");

        assertEquals(
                "B",
                pila.pop());

        assertEquals(
                "A",
                pila.pop());
    }

    @Test
    void tablaHashBuscarDato() {

        TablaHash<String, String> tabla =
                new TablaHash<>(17);

        tabla.poner(
                "PRG01",
                "Programación I");

        assertEquals(
                "Programación I",
                tabla.obtener(
                        "PRG01"));
    }
}
