package sv.edu.udb.ped941;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sv.edu.udb.ped941.estructuras.ListaEnlazada;
import sv.edu.udb.ped941.modelo.Materia;
import sv.edu.udb.ped941.servicio.PlanificadorAcademico;

import static org.junit.jupiter.api.Assertions.*;

class PlanificadorAcademicoTest {

    private PlanificadorAcademico planificador;

    @BeforeEach
    void preparar() {

        planificador =
                new PlanificadorAcademico();

        planificador
                .cargarDatosEjemplo();
    }

    @Test
    void buscarMateriaExistente() {

        assertNotNull(
                planificador
                        .buscarMateria(
                                "PRG01"));
    }

    @Test
    void buscarMateriaInexistente() {

        assertNull(
                planificador
                        .buscarMateria(
                                "XXX99"));
    }

    @Test
    void rechazarCodigoDuplicado() {

        assertThrows(
                IllegalArgumentException.class,
                () ->
                planificador
                        .registrarMateria(
                                new Materia(
                                        "PRG01",
                                        "Materia duplicada",
                                        4,
                                        1,
                                        "Prueba",
                                        true)));
    }

    @Test
    void materiasInicialesDisponibles() {

        ListaEnlazada<Materia> disponibles =
                planificador
                        .obtenerMateriasDisponibles();

        assertEquals(
                2,
                disponibles.tamanio());

        assertTrue(
                contiene(
                        disponibles,
                        "PRG01"));

        assertTrue(
                contiene(
                        disponibles,
                        "MAT01"));
    }

    @Test
    void disponiblesDespuesDeAprobarPrimerCiclo() {

        planificador
                .marcarMateriaAprobada(
                        "PRG01");

        planificador
                .marcarMateriaAprobada(
                        "MAT01");

        ListaEnlazada<Materia> disponibles =
                planificador
                        .obtenerMateriasDisponibles();

        assertTrue(
                contiene(
                        disponibles,
                        "PRG02"));

        assertTrue(
                contiene(
                        disponibles,
                        "BDT02"));
    }

    @Test
    void detectarPrerequisitoPendiente() {

        ListaEnlazada<Materia> pendientes =
                planificador
                        .obtenerPrerequisitosPendientes(
                                "PED03");

        assertTrue(
                contiene(
                        pendientes,
                        "PRG02"));
    }

    @Test
    void rechazarAutorreferencia() {

        assertThrows(
                IllegalArgumentException.class,
                () ->
                planificador
                        .agregarPrerequisito(
                                "PRG01",
                                "PRG01"));
    }

    @Test
    void rechazarRelacionDuplicada() {

        assertThrows(
                IllegalArgumentException.class,
                () ->
                planificador
                        .agregarPrerequisito(
                                "PRG01",
                                "PRG02"));
    }

    @Test
    void rechazarCiclo() {

        assertThrows(
                IllegalArgumentException.class,
                () ->
                planificador
                        .agregarPrerequisito(
                                "PRO04",
                                "PRG01"));
    }

    @Test
    void ordenTopologicoRespetaPrerequisitos() {

        ListaEnlazada<Materia> orden =
                planificador
                        .generarOrdenAcademico();

        assertTrue(
                indice(
                        orden,
                        "PRG01")
                <
                indice(
                        orden,
                        "PRG02"));

        assertTrue(
                indice(
                        orden,
                        "PRG02")
                <
                indice(
                        orden,
                        "PED03"));

        assertTrue(
                indice(
                        orden,
                        "PED03")
                <
                indice(
                        orden,
                        "PRO04"));
    }

    private boolean contiene(
            ListaEnlazada<Materia> lista,
            String codigo) {

        return indice(
                lista,
                codigo) >= 0;
    }

    private int indice(
            ListaEnlazada<Materia> lista,
            String codigo) {

        for (int i = 0;
             i < lista.tamanio();
             i++) {

            if (lista
                    .obtener(i)
                    .getCodigo()
                    .equals(codigo)) {

                return i;
            }
        }

        return -1;
    }
}
