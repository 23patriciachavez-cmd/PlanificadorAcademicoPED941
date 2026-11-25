package sv.edu.udb.ped941;

import org.junit.jupiter.api.Test;

import sv.edu.udb.ped941.estructuras.ListaEnlazada;
import sv.edu.udb.ped941.modelo.Materia;
import sv.edu.udb.ped941.servicio.PlanificadorAcademico;

import static org.junit.jupiter.api.Assertions.*;

class PlanificadorAcademicoTest {

    /// BÚSQUEDA DE MATERIAS
    
    @Test
    void buscarMateriaExistente() {

        PlanificadorAcademico planificador =
                crearPlanificador();

        Materia materia =
                planificador.buscarMateria(
                        "PRD104");

        assertNotNull(
                materia);

        assertEquals(
                "Programación con Estructuras de Datos",
                materia.getNombre());

        assertEquals(
                3,
                materia.getCiclo());

        assertEquals(
                4,
                materia.getCreditos());
    }


    @Test
    void buscarMateriaInexistente() {

        PlanificadorAcademico planificador =
                crearPlanificador();

        Materia materia =
                planificador.buscarMateria(
                        "XXX999");

        assertNull(
                materia);
    }


    /// VALIDACIÓN DEL PENSUM REAL
    
    @Test
    void pensumTiene40Materias() {

        PlanificadorAcademico planificador =
                crearPlanificador();

        assertEquals(
                40,
                planificador.obtenerTotalMaterias());
    }


    @Test
    void pensumTiene161UV() {

        PlanificadorAcademico planificador =
                crearPlanificador();

        assertEquals(
                161,
                planificador.obtenerTotalUV());
    }


    @Test
    void pensumOficialEsValido() {

        PlanificadorAcademico planificador =
                crearPlanificador();

        assertTrue(
                planificador.validarPensum());

        assertTrue(
                planificador.validarPensumOficialICC());
    }


    /// CÓDIGOS DUPLICADOS
    
    @Test
    void rechazarCodigoDuplicado() {

        PlanificadorAcademico planificador =
                crearPlanificador();

        Materia duplicada =
                new Materia(
                        "CAD501",
                        "Materia duplicada",
                        4,
                        1,
                        "ICC",
                        true);

        assertThrows(
                IllegalArgumentException.class,
                () ->
                        planificador.registrarMateria(
                                duplicada));
    }


    /// MATERIAS DISPONIBLES
    
    @Test
    void materiasInicialesDisponibles() {

        PlanificadorAcademico planificador =
                crearPlanificador();

        ListaEnlazada<Materia> disponibles =
                planificador
                        .obtenerMateriasDisponibles();

        assertEquals(
                7,
                disponibles.tamanio());

        assertTrue(
                contiene(
                        disponibles,
                        "CAD501"));

        assertTrue(
                contiene(
                        disponibles,
                        "QUG501"));

        assertTrue(
                contiene(
                        disponibles,
                        "ANF231"));

        assertTrue(
                contiene(
                        disponibles,
                        "PRE104"));

        assertTrue(
                contiene(
                        disponibles,
                        "ALG501"));

        assertTrue(
                contiene(
                        disponibles,
                        "DRD101"));

        assertTrue(
                contiene(
                        disponibles,
                        "PSC231"));
    }


    /// DISPONIBILIDAD DESPUÉS DE APROBAR PRERREQUISITO
    
    @Test
    void disponiblesDespuesDeAprobarProgramacionEstructurada() {

        PlanificadorAcademico planificador =
                crearPlanificador();

        assertTrue(
                planificador
                        .marcarMateriaAprobada(
                                "PRE104"));

        ListaEnlazada<Materia> disponibles =
                planificador
                        .obtenerMateriasDisponibles();

        assertTrue(
                contiene(
                        disponibles,
                        "MDB104"));

        assertTrue(
                contiene(
                        disponibles,
                        "POO104"));

        assertFalse(
                contiene(
                        disponibles,
                        "PRE104"));
    }


    /// PRERREQUISITOS PENDIENTES
    
    @Test
    void detectarPrerequisitoPendiente() {

        PlanificadorAcademico planificador =
                crearPlanificador();

        ListaEnlazada<Materia> pendientes =
                planificador
                        .obtenerPrerequisitosPendientes(
                                "PRD104");

        assertEquals(
                1,
                pendientes.tamanio());

        assertTrue(
                contiene(
                        pendientes,
                        "POO104"));
    }


    @Test
    void prerrequisitoDejaDeEstarPendienteAlAprobarse() {

        PlanificadorAcademico planificador =
                crearPlanificador();

        planificador
                .marcarMateriaAprobada(
                        "POO104");

        ListaEnlazada<Materia> pendientes =
                planificador
                        .obtenerPrerequisitosPendientes(
                                "PRD104");

        assertTrue(
                pendientes.estaVacia());
    }


    /// AUTORREFERENCIA
    
    @Test
    void rechazarAutorreferencia() {

        PlanificadorAcademico planificador =
                crearPlanificador();

        assertThrows(
                IllegalArgumentException.class,
                () ->
                        planificador
                                .agregarPrerequisito(
                                        "CAD501",
                                        "CAD501"));
    }


    /// RELACIÓN DUPLICADA
    
    @Test
    void rechazarRelacionDuplicada() {

        PlanificadorAcademico planificador =
                crearPlanificador();

        assertThrows(
                IllegalArgumentException.class,
                () ->
                        planificador
                                .agregarPrerequisito(
                                        "PRE104",
                                        "POO104"));
    }


    /// DETECCIÓN DE CICLOS
    
    @Test
    void rechazarCiclo() {

        PlanificadorAcademico planificador =
                crearPlanificador();

        /*Ruta existente: PRE104 -> MDB104 -> DMD104 -> LIC104 -> LIS104 -> DPS104 -> DES104
         * Agregar DES104 -> PRE104 produciría un ciclo. */

        assertThrows(
                IllegalArgumentException.class,
                () ->
                        planificador
                                .agregarPrerequisito(
                                        "DES104",
                                        "PRE104"));

        assertTrue(
                planificador.validarPensum());
    }


    /// ORDENAMIENTO TOPOLÓGICO
    
    @Test
    void ordenTopologicoProcesaLas40Materias() {

        PlanificadorAcademico planificador =
                crearPlanificador();

        ListaEnlazada<Materia> orden =
                planificador
                        .generarOrdenAcademico();

        assertEquals(
                40,
                orden.tamanio());
    }


    @Test
    void ordenTopologicoRespetaPrerequisitos() {

        PlanificadorAcademico planificador =
                crearPlanificador();

        ListaEnlazada<Materia> orden =
                planificador
                        .generarOrdenAcademico();


        // Programación

        assertAntes(
                orden,
                "PRE104",
                "POO104");

        assertAntes(
                orden,
                "POO104",
                "PRD104");


        // Base de datos y desarrollo

        assertAntes(
                orden,
                "PRE104",
                "MDB104");

        assertAntes(
                orden,
                "MDB104",
                "DMD104");

        assertAntes(
                orden,
                "DMD104",
                "LIC104");

        assertAntes(
                orden,
                "LIC104",
                "LIS104");

        assertAntes(
                orden,
                "LIS104",
                "DPS104");

        assertAntes(
                orden,
                "DPS104",
                "DES104");


        // Redes

        assertAntes(
                orden,
                "DRD101",
                "IRD101");

        assertAntes(
                orden,
                "IRD101",
                "DSS101");

        assertAntes(
                orden,
                "IRD101",
                "ASR104");

        assertAntes(
                orden,
                "IRD101",
                "ASI104");


        // Ingeniería de software

        assertAntes(
                orden,
                "DDP106",
                "INS104");

        assertAntes(
                orden,
                "INS104",
                "NTI104");

        assertAntes(
                orden,
                "INS104",
                "CDS104");

        assertAntes(
                orden,
                "INS104",
                "AUS104");


        // Electiva

        assertAntes(
                orden,
                "NTI104",
                "ELEC38");
    }


    /// PROGRESO ACADÉMICO
    
    @Test
    void progresoInicialEsCero() {

        PlanificadorAcademico planificador =
                crearPlanificador();

        assertEquals(
                0,
                planificador
                        .obtenerTotalMateriasAprobadas());

        assertEquals(
                0,
                planificador
                        .obtenerUVAprobadas());

        assertEquals(
                0.0,
                planificador
                        .obtenerPorcentajeAvanceMaterias(),
                0.001);

        assertEquals(
                0.0,
                planificador
                        .obtenerPorcentajeAvanceUV(),
                0.001);
    }


    @Test
    void progresoDespuesDeAprobarPrimerCiclo() {

        PlanificadorAcademico planificador =
                crearPlanificador();

        planificador
                .marcarMateriaAprobada(
                        "CAD501");

        planificador
                .marcarMateriaAprobada(
                        "QUG501");

        planificador
                .marcarMateriaAprobada(
                        "ANF231");

        planificador
                .marcarMateriaAprobada(
                        "PRE104");

        assertEquals(
                4,
                planificador
                        .obtenerTotalMateriasAprobadas());

        // 4 + 4 + 3 + 4 = 15 UV.
        
        assertEquals(
                15,
                planificador
                        .obtenerUVAprobadas());

        assertEquals(
                10.0,
                planificador
                        .obtenerPorcentajeAvanceMaterias(),
                0.001);

        assertEquals(
                (15.0 * 100.0) / 161.0,
                planificador
                        .obtenerPorcentajeAvanceUV(),
                0.001);
    }


    /// SIMULADOR DE PRÓXIMO CICLO
    
    @Test
    void simuladorInicialMuestraCicloUno() {

        PlanificadorAcademico planificador =
                crearPlanificador();

        assertEquals(
                1,
                planificador
                        .obtenerSiguienteCicloAcademico());

        ListaEnlazada<Materia> simulacion =
                planificador
                        .simularProximoCiclo();

        assertEquals(
                4,
                simulacion.tamanio());

        assertTrue(
                contiene(
                        simulacion,
                        "CAD501"));

        assertTrue(
                contiene(
                        simulacion,
                        "QUG501"));

        assertTrue(
                contiene(
                        simulacion,
                        "ANF231"));

        assertTrue(
                contiene(
                        simulacion,
                        "PRE104"));
    }


    @Test
    void simuladorDespuesDelPrimerCicloMuestraCicloDos() {

        PlanificadorAcademico planificador =
                crearPlanificador();

        planificador
                .marcarMateriaAprobada(
                        "CAD501");

        planificador
                .marcarMateriaAprobada(
                        "QUG501");

        planificador
                .marcarMateriaAprobada(
                        "ANF231");

        planificador
                .marcarMateriaAprobada(
                        "PRE104");

        assertEquals(
                2,
                planificador
                        .obtenerSiguienteCicloAcademico());

        ListaEnlazada<Materia> simulacion =
                planificador
                        .simularProximoCiclo();

        assertEquals(
                4,
                simulacion.tamanio());

        assertTrue(
                contiene(
                        simulacion,
                        "ALG501"));

        assertTrue(
                contiene(
                        simulacion,
                        "CAI501"));

        assertTrue(
                contiene(
                        simulacion,
                        "MDB104"));

        assertTrue(
                contiene(
                        simulacion,
                        "POO104"));
    }


    /// DESMARCAR MATERIA
    
    @Test
    void desmarcarMateriaAprobada() {

        PlanificadorAcademico planificador =
                crearPlanificador();

        assertTrue(
                planificador
                        .marcarMateriaAprobada(
                                "CAD501"));

        assertTrue(
                planificador
                        .buscarMateria(
                                "CAD501")
                        .isAprobada());

        assertTrue(
                planificador
                        .desmarcarMateriaAprobada(
                                "CAD501"));

        assertFalse(
                planificador
                        .buscarMateria(
                                "CAD501")
                        .isAprobada());

        assertFalse(
                planificador
                        .desmarcarMateriaAprobada(
                                "CAD501"));
    }


    /// PLAN COMPLETADO
    
    @Test
    void planCompletadoNoTieneSiguienteCiclo() {

        PlanificadorAcademico planificador =
                crearPlanificador();

        ListaEnlazada<Materia> materias =
                planificador
                        .listarMaterias();

        for (int i = 0;
             i < materias.tamanio();
             i++) {

            planificador
                    .marcarMateriaAprobada(
                            materias
                                    .obtener(i)
                                    .getCodigo());
        }

        assertEquals(
                40,
                planificador
                        .obtenerTotalMateriasAprobadas());

        assertEquals(
                161,
                planificador
                        .obtenerUVAprobadas());

        assertEquals(
                100.0,
                planificador
                        .obtenerPorcentajeAvanceMaterias(),
                0.001);

        assertEquals(
                100.0,
                planificador
                        .obtenerPorcentajeAvanceUV(),
                0.001);

        assertEquals(
                0,
                planificador
                        .obtenerSiguienteCicloAcademico());

        assertTrue(
                planificador
                        .simularProximoCiclo()
                        .estaVacia());
    }


    /// RESUMEN DE PROGRESO
    
    @Test
    void resumenProgresoContieneDatosCorrectos() {

        PlanificadorAcademico planificador =
                crearPlanificador();

        planificador
                .marcarMateriaAprobada(
                        "CAD501");

        planificador
                .marcarMateriaAprobada(
                        "QUG501");

        planificador
                .marcarMateriaAprobada(
                        "ANF231");

        planificador
                .marcarMateriaAprobada(
                        "PRE104");

        String resumen =
                planificador
                        .obtenerResumenProgreso();

        assertNotNull(
                resumen);

        assertTrue(
                resumen.contains(
                        "4 / 40"));

        assertTrue(
                resumen.contains(
                        "15 / 161"));

        assertTrue(
                resumen.contains(
                        "Siguiente ciclo pendiente: 2"));
    }


    /// CATÁLOGO DE ELECTIVAS
    
    @Test
    void catalogoTieneNueveElectivas() {

        PlanificadorAcademico planificador =
                crearPlanificador();

        ListaEnlazada<Materia> electivas =
                planificador
                        .listarElectivas();

        assertEquals(
                9,
                electivas.tamanio());

        assertTrue(
                contiene(
                        electivas,
                        "GEN102"));

        assertTrue(
                contiene(
                        electivas,
                        "EAL102"));

        assertTrue(
                contiene(
                        electivas,
                        "ALI103"));

        assertTrue(
                contiene(
                        electivas,
                        "ACD104"));

        assertTrue(
                contiene(
                        electivas,
                        "IAR105"));

        assertTrue(
                contiene(
                        electivas,
                        "SLI106"));

        assertTrue(
                contiene(
                        electivas,
                        "ROB109"));

        assertTrue(
                contiene(
                        electivas,
                        "DVC110"));

        assertTrue(
                contiene(
                        electivas,
                        "GET111"));
    }


    @Test
    void catalogoElectivasNoAlteraPensumOficial() {

        PlanificadorAcademico planificador =
                crearPlanificador();

        assertEquals(
                40,
                planificador
                        .obtenerTotalMaterias());

        assertEquals(
                161,
                planificador
                        .obtenerTotalUV());

        assertTrue(
                planificador
                        .validarPensumOficialICC());

        assertEquals(
                9,
                planificador
                        .listarElectivas()
                        .tamanio());
    }


    /// SELECCIÓN DE ELECTIVA
    
    @Test
    void seleccionarElectivaValida() {

        PlanificadorAcademico planificador =
                crearPlanificador();

        planificador
                .seleccionarElectiva(
                        "ACD104");

        Materia seleccionada =
                planificador
                        .obtenerElectivaSeleccionada();

        assertNotNull(
                seleccionada);

        assertEquals(
                "ACD104",
                seleccionada.getCodigo());

        assertEquals(
                "Aprendizaje Automático y Ciencia de Datos",
                seleccionada.getNombre());
    }


    @Test
    void rechazarElectivaInexistente() {

        PlanificadorAcademico planificador =
                crearPlanificador();

        assertThrows(
                IllegalArgumentException.class,
                () ->
                        planificador
                                .seleccionarElectiva(
                                        "XXX999"));
    }


    /// PRERREQUISITO DE ELECTIVA
    
    @Test
    void electivaRequiereNTI104() {

        PlanificadorAcademico planificador =
                crearPlanificador();

        planificador
                .seleccionarElectiva(
                        "IAR105");

        /* Antes de aprobar NTI104, todavía no se puede cursar. */
    
        assertFalse(
                planificador
                        .puedeCursarElectivaSeleccionada());

        planificador
                .marcarMateriaAprobada(
                        "NTI104");

        /*
         * Después de aprobar NTI104,
         * la electiva queda habilitada.
         */
        assertTrue(
                planificador
                        .puedeCursarElectivaSeleccionada());
    }


    /// MÉTODO AUXILIAR
    
    private PlanificadorAcademico
    crearPlanificador() {

        PlanificadorAcademico planificador =
                new PlanificadorAcademico();

        planificador
                .cargarPensumICC2024();

        return planificador;
    }


    /// BUSCAR CÓDIGOS EN LISTAS
    
    private boolean contiene(
            ListaEnlazada<Materia> lista,
            String codigo) {

        return indice(
                lista,
                codigo) >= 0;
    }


    /// OBTENER POSICIÓN
    
    private int indice(
            ListaEnlazada<Materia> lista,
            String codigo) {

        for (int i = 0;
             i < lista.tamanio();
             i++) {

            Materia materia =
                    lista.obtener(i);

            if (materia
                    .getCodigo()
                    .equalsIgnoreCase(
                            codigo)) {

                return i;
            }
        }

        return -1;
    }


    /// VALIDAR PRECEDENCIA EN KAHN
    
    private void assertAntes(
            ListaEnlazada<Materia> orden,
            String prerequisito,
            String dependiente) {

        int posicionPrerequisito =
                indice(
                        orden,
                        prerequisito);

        int posicionDependiente =
                indice(
                        orden,
                        dependiente);

        assertTrue(
                posicionPrerequisito >= 0,
                "No se encontró "
                + prerequisito
                + " en el orden académico.");

        assertTrue(
                posicionDependiente >= 0,
                "No se encontró "
                + dependiente
                + " en el orden académico.");

        assertTrue(
                posicionPrerequisito
                        < posicionDependiente,
                prerequisito
                + " debe aparecer antes que "
                + dependiente
                + ".");
    }
}
