package sv.edu.udb.ped941.servicio;

import sv.edu.udb.ped941.arbol.ArbolCurricular;
import sv.edu.udb.ped941.estructuras.ListaEnlazada;
import sv.edu.udb.ped941.grafo.GrafoAcademico;
import sv.edu.udb.ped941.modelo.Materia;

public class PlanificadorAcademico {

    private final GrafoAcademico grafo;

    private final ArbolCurricular arbol;

    public PlanificadorAcademico() {

        grafo =
                new GrafoAcademico(
                        53);

        arbol =
                new ArbolCurricular(
                        "Plan de Estudios");
    }

    public void registrarMateria(
            Materia materia) {

        if (!grafo.agregarMateria(
                materia)) {

            throw new IllegalArgumentException(
                    "Ya existe una materia "
                    + "con el código "
                    + materia.getCodigo()
                    + ".");
        }
    }

    public Materia buscarMateria(
            String codigo) {

        return grafo.buscarMateria(
                codigo);
    }

    public void modificarMateria(
            String codigo,
            String nombre,
            int creditos,
            int ciclo,
            String area,
            boolean obligatoria) {

        Materia materia =
                buscarMateria(
                        codigo);

        if (materia == null) {

            throw new IllegalArgumentException(
                    "La materia no existe.");
        }

        materia.setNombre(
                nombre);

        materia.setCreditos(
                creditos);

        materia.setCiclo(
                ciclo);

        materia.setArea(
                area);

        materia.setObligatoria(
                obligatoria);
    }

    public void agregarPrerequisito(
            String codigoPrerequisito,
            String codigoMateria) {

        grafo.agregarPrerequisito(
                codigoPrerequisito,
                codigoMateria);
    }

    public boolean eliminarPrerequisito(
            String prerequisito,
            String materia) {

        return grafo
                .eliminarPrerequisito(
                        prerequisito,
                        materia);
    }

    public boolean marcarMateriaAprobada(
            String codigo) {

        Materia materia =
                buscarMateria(
                        codigo);

        if (materia == null) {

            throw new IllegalArgumentException(
                    "La materia no existe.");
        }

        if (materia.isAprobada()) {

            return false;
        }

        materia.setAprobada(
                true);

        return true;
    }

    public ListaEnlazada<Materia>
    obtenerMateriasDisponibles() {

        ListaEnlazada<Materia>
                disponibles =
                new ListaEnlazada<>();

        ListaEnlazada<Materia>
                materias =
                grafo.obtenerMaterias();

        for (int i = 0;
             i < materias.tamanio();
             i++) {

            Materia materia =
                    materias.obtener(i);

            if (materia.isAprobada()) {
                continue;
            }

            ListaEnlazada<Materia>
                    requisitos =
                    grafo.obtenerPrerequisitos(
                            materia.getCodigo());

            boolean disponible =
                    true;

            for (int j = 0;
                 j < requisitos.tamanio();
                 j++) {

                if (!requisitos
                        .obtener(j)
                        .isAprobada()) {

                    disponible =
                            false;

                    break;
                }
            }

            if (disponible) {

                disponibles.agregar(
                        materia);
            }
        }

        return disponibles;
    }

    public ListaEnlazada<Materia>
    obtenerPrerequisitosPendientes(
            String codigo) {

        Materia materia =
                buscarMateria(
                        codigo);

        if (materia == null) {

            throw new IllegalArgumentException(
                    "La materia no existe.");
        }

        ListaEnlazada<Materia>
                pendientes =
                new ListaEnlazada<>();

        ListaEnlazada<Materia>
                requisitos =
                grafo.obtenerPrerequisitos(
                        codigo);

        for (int i = 0;
             i < requisitos.tamanio();
             i++) {

            Materia requisito =
                    requisitos.obtener(i);

            if (!requisito.isAprobada()) {

                pendientes.agregar(
                        requisito);
            }
        }

        return pendientes;
    }

    public ListaEnlazada<Materia>
    obtenerMateriasBloqueadas() {

        ListaEnlazada<Materia>
                bloqueadas =
                new ListaEnlazada<>();

        ListaEnlazada<Materia>
                materias =
                grafo.obtenerMaterias();

        for (int i = 0;
             i < materias.tamanio();
             i++) {

            Materia materia =
                    materias.obtener(i);

            if (materia.isAprobada()) {
                continue;
            }

            ListaEnlazada<Materia>
                    pendientes =
                    obtenerPrerequisitosPendientes(
                            materia.getCodigo());

            if (!pendientes.estaVacia()) {

                bloqueadas.agregar(
                        materia);
            }
        }

        return bloqueadas;
    }

    public ListaEnlazada<Materia>
    obtenerPrerequisitos(
            String codigo) {

        return grafo
                .obtenerPrerequisitos(
                        codigo);
    }

    public ListaEnlazada<Materia>
    generarOrdenAcademico() {

        return grafo
                .generarOrdenTopologico();
    }

    public boolean validarPensum() {

        return !grafo.detectarCiclo();
    }

    public ListaEnlazada<Materia>
    listarMaterias() {

        return grafo
                .obtenerMaterias();
    }

    public String mostrarGrafo() {

        return grafo
                .mostrarGrafo();
    }

    public String mostrarPensumJerarquico() {

        arbol.reconstruir(
                listarMaterias());

        return arbol
                .mostrarJerarquia();
    }

    public void cargarDatosEjemplo() {

        if (!listarMaterias()
                .estaVacia()) {

            return;
        }

        registrarMateria(
                new Materia(
                        "PRG01",
                        "Programación I",
                        4,
                        1,
                        "Programación",
                        true));

        registrarMateria(
                new Materia(
                        "MAT01",
                        "Matemática I",
                        4,
                        1,
                        "Matemática",
                        true));

        registrarMateria(
                new Materia(
                        "PRG02",
                        "Programación II",
                        4,
                        2,
                        "Programación",
                        true));

        registrarMateria(
                new Materia(
                        "BDT02",
                        "Bases de Datos",
                        4,
                        2,
                        "Bases de datos",
                        true));

        registrarMateria(
                new Materia(
                        "PED03",
                        "Estructuras de Datos",
                        4,
                        3,
                        "Programación",
                        true));

        registrarMateria(
                new Materia(
                        "ALG03",
                        "Análisis de Algoritmos",
                        4,
                        3,
                        "Programación",
                        true));

        registrarMateria(
                new Materia(
                        "PRO04",
                        "Proyecto Integrador",
                        4,
                        4,
                        "Proyecto",
                        true));

        agregarPrerequisito(
                "PRG01",
                "PRG02");

        agregarPrerequisito(
                "PRG01",
                "BDT02");

        agregarPrerequisito(
                "PRG02",
                "PED03");

        agregarPrerequisito(
                "PRG02",
                "ALG03");

        agregarPrerequisito(
                "MAT01",
                "ALG03");

        agregarPrerequisito(
                "PED03",
                "PRO04");

        agregarPrerequisito(
                "BDT02",
                "PRO04");

        agregarPrerequisito(
                "ALG03",
                "PRO04");
    }
}
