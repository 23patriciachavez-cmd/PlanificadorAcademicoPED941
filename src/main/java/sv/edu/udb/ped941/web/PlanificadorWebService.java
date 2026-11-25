package sv.edu.udb.ped941.web;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import sv.edu.udb.ped941.estructuras.ListaEnlazada;
import sv.edu.udb.ped941.estructuras.TablaHash;
import sv.edu.udb.ped941.modelo.Materia;
import sv.edu.udb.ped941.persistencia.ProgresoRepository;
import sv.edu.udb.ped941.servicio.PlanificadorAcademico;

@Service
public class PlanificadorWebService {

    private volatile PlanificadorAcademico planificador;

    private final ProgresoRepository repositorio;

    private final JdbcTemplate jdbc;

    public PlanificadorWebService(
            ProgresoRepository repositorio,
            JdbcTemplate jdbc) {

        this.repositorio = repositorio;
        this.jdbc = jdbc;

        this.planificador =
                new PlanificadorAcademico();

        this.planificador.cargarPensumICC2024();
    }

    /// RECUPERACION DE DATOS PERSISTENTES
    
    @EventListener(ApplicationReadyEvent.class)
    public synchronized void recuperarPersistencia() {

        planificador = reconstruirDesdeBD();
    }

    private PlanificadorAcademico reconstruirDesdeBD() {

        PlanificadorAcademico nuevo =
                new PlanificadorAcademico();

        nuevo.cargarPensumICC2024();

        List<Cambio> cambios = jdbc.query(
                """
                SELECT operacion, codigo, destino,
                       nombre, uv, ciclo, area, obligatoria
                FROM cambios_pensum
                ORDER BY id
                """,

                (rs, fila) -> new Cambio(
                        rs.getString("operacion"),
                        rs.getString("codigo"),
                        rs.getString("destino"),
                        rs.getString("nombre"),
                        rs.getInt("uv"),
                        rs.getInt("ciclo"),
                        rs.getString("area"),
                        rs.getBoolean("obligatoria"))
        );

        for (Cambio cambio : cambios) {

            aplicarCambio(nuevo, cambio);
        }

        for (String codigo :
                repositorio.obtenerMateriasAprobadas()) {

            if (nuevo.buscarMateria(codigo) != null) {

                nuevo.marcarMateriaAprobada(codigo);
            }
        }

        repositorio
                .obtenerElectivaSeleccionada()
                .ifPresent(codigo -> {

                    if (nuevo.buscarElectiva(codigo) != null) {

                        nuevo.seleccionarElectiva(codigo);
                    }
                });

        return nuevo;
    }

    /// CONSULTAS GENERALES
    
    public List<Materia> listarMaterias() {

        return ordenar(
                convertir(planificador.listarMaterias()));
    }

    public List<Materia> materiasDisponibles() {

        return ordenar(
                convertir(
                        planificador.obtenerMateriasDisponibles()));
    }

    public List<Materia> materiasBloqueadas() {

        return ordenar(
                convertir(
                        planificador.obtenerMateriasBloqueadas()));
    }

    public List<Materia> simularProximoCiclo() {

        return ordenar(
                convertir(planificador.simularProximoCiclo()));
    }

    public List<Materia> listarElectivas() {

        return convertir(planificador.listarElectivas());
    }

    public Materia buscarMateria(String codigo) {

        if (codigo == null || codigo.isBlank()) {

            return null;
        }

        return planificador.buscarMateria(
                codigo.trim().toUpperCase(Locale.ROOT));
    }

    /// PROGRESO ACADEMICO
    
    public int totalMaterias() {

        return planificador.obtenerTotalMaterias();
    }

    public int totalUV() {

        return planificador.obtenerTotalUV();
    }

    public int materiasAprobadas() {

        return planificador.obtenerTotalMateriasAprobadas();
    }

    public int uvAprobadas() {

        return planificador.obtenerUVAprobadas();
    }

    public double porcentajeMaterias() {

        int total = totalMaterias();

        return total == 0
                ? 0.0
                : materiasAprobadas() * 100.0 / total;
    }

    public double porcentajeUV() {

        int total = totalUV();

        return total == 0
                ? 0.0
                : uvAprobadas() * 100.0 / total;
    }

    public int siguienteCiclo() {

        return planificador.obtenerSiguienteCicloAcademico();
    }

    /// MARCAR Y DESMARCAR MATERIAS
    
    public synchronized void alternarMateria(String codigo) {

        Materia materia = buscarMateria(codigo);

        if (materia == null) {

            throw new IllegalArgumentException(
                    "La materia indicada no existe.");
        }

        String codigoReal = materia.getCodigo();

        boolean nuevoEstado = !materia.isAprobada();

        if ("ELEC38".equals(codigoReal)
                && nuevoEstado
                && planificador.obtenerElectivaSeleccionada() == null) {

            throw new IllegalArgumentException(
                    "Primero debe seleccionar una electiva.");
        }

        repositorio.guardarEstadoMateria(
                codigoReal,
                nuevoEstado);

        if (nuevoEstado) {

            planificador.marcarMateriaAprobada(codigoReal);

        } else {

            planificador.desmarcarMateriaAprobada(codigoReal);
        }
    }

    /// ELECTIVAS
    
    public synchronized void seleccionarElectiva(String codigo) {

        String codigoReal = normalizarCodigo(codigo);

        Materia electiva =
                planificador.buscarElectiva(codigoReal);

        if (electiva == null) {

            throw new IllegalArgumentException(
                    "La electiva seleccionada no existe.");
        }

        Materia espacio =
                planificador.buscarMateria("ELEC38");

        Materia actual =
                planificador.obtenerElectivaSeleccionada();

        if (espacio.isAprobada()
                && actual != null
                && !actual.getCodigo().equals(codigoReal)) {

            throw new IllegalArgumentException(
                    "Desmarque la electiva aprobada antes de cambiarla.");
        }

        repositorio.guardarElectivaSeleccionada(codigoReal);

        planificador.seleccionarElectiva(codigoReal);
    }

    public synchronized void limpiarElectiva() {

        Materia espacio =
                planificador.buscarMateria("ELEC38");

        if (espacio.isAprobada()) {

            throw new IllegalArgumentException(
                    "Primero desmarque la electiva aprobada.");
        }

        repositorio.limpiarElectiva();

        planificador.limpiarElectivaSeleccionada();
    }

    public Materia electivaSeleccionada() {

        return planificador.obtenerElectivaSeleccionada();
    }

    public boolean puedeCursarElectiva() {

        return planificador.puedeCursarElectivaSeleccionada();
    }

    /// PRERREQUISITOS
    
    public List<Materia> prerrequisitosDe(String codigo) {

        Materia materia = buscarMateria(codigo);

        if (materia == null) {

            return new ArrayList<>();
        }

        return convertir(
                planificador.obtenerPrerequisitos(
                        materia.getCodigo()));
    }

    /// RF12 - MATERIAS DEPENDIENTES
    
    public List<Materia> materiasDependientesDe(String codigo) {

        List<Materia> resultado = new ArrayList<>();

        Materia buscada = buscarMateria(codigo);

        if (buscada == null) {

            return resultado;
        }

        for (Materia materia : listarMaterias()) {

            for (Materia requisito :
                    prerrequisitosDe(materia.getCodigo())) {

                if (requisito.getCodigo()
                        .equalsIgnoreCase(buscada.getCodigo())) {

                    resultado.add(materia);

                    break;
                }
            }
        }

        return ordenar(resultado);
    }

    /// RF13 - CADENA COMPLETA DE PRERREQUISITOS
    
    public List<Materia> cadenaCompletaPrerequisitos(
            String codigo) {

        List<Materia> resultado = new ArrayList<>();

        TablaHash<String, Boolean> visitados =
                new TablaHash<>(53);

        Materia materia = buscarMateria(codigo);

        if (materia == null) {

            return resultado;
        }

        visitados.poner(materia.getCodigo(), true);

        recorrerPrerequisitos(
                materia.getCodigo(),
                resultado,
                visitados);

        return resultado;
    }

    private void recorrerPrerequisitos(
            String codigo,
            List<Materia> resultado,
            TablaHash<String, Boolean> visitados) {

        for (Materia requisito : prerrequisitosDe(codigo)) {

            String codigoRequisito =
                    requisito.getCodigo();

            if (visitados.obtener(codigoRequisito) != null) {

                continue;
            }

            visitados.poner(codigoRequisito, true);

            recorrerPrerequisitos(
                    codigoRequisito,
                    resultado,
                    visitados);

            resultado.add(requisito);
        }
    }

    /// ADMINISTRACION DEL PENSUM
    
    public synchronized void registrarMateria(
            String codigo,
            String nombre,
            int uv,
            int ciclo,
            String area,
            boolean obligatoria) {

        codigo = normalizarCodigo(codigo);

        validarDatos(nombre, uv, ciclo, area);

        if (planificador.buscarElectiva(codigo) != null) {

            throw new IllegalArgumentException(
                    "El codigo pertenece al catalogo de electivas.");
        }

        actualizarPensum(
                new Cambio(
                        "REGISTRAR",
                        codigo,
                        null,
                        nombre.trim(),
                        uv,
                        ciclo,
                        area.trim(),
                        obligatoria));
    }

    public synchronized void modificarMateria(
            String codigo,
            String nombre,
            int uv,
            int ciclo,
            String area,
            boolean obligatoria) {

        codigo = normalizarCodigo(codigo);

        validarDatos(nombre, uv, ciclo, area);

        if ("ELEC38".equals(codigo)) {

            throw new IllegalArgumentException(
                    "El espacio curricular de electiva esta protegido.");
        }

        actualizarPensum(
                new Cambio(
                        "MODIFICAR",
                        codigo,
                        null,
                        nombre.trim(),
                        uv,
                        ciclo,
                        area.trim(),
                        obligatoria));
    }

    public synchronized void agregarPrerequisito(
            String origen,
            String destino) {

        origen = normalizarCodigo(origen);
        destino = normalizarCodigo(destino);

        if ("ELEC38".equals(destino)) {

            throw new IllegalArgumentException(
                    "El requisito de la electiva esta protegido.");
        }

        actualizarPensum(
                new Cambio(
                        "AGREGAR_RELACION",
                        origen,
                        destino,
                        null,
                        0,
                        0,
                        null,
                        false));
    }

    public synchronized void eliminarPrerequisito(
            String origen,
            String destino) {

        origen = normalizarCodigo(origen);
        destino = normalizarCodigo(destino);

        if ("ELEC38".equals(destino)) {

            throw new IllegalArgumentException(
                    "El requisito de la electiva esta protegido.");
        }

        actualizarPensum(
                new Cambio(
                        "ELIMINAR_RELACION",
                        origen,
                        destino,
                        null,
                        0,
                        0,
                        null,
                        false));
    }

    /// PERSISTENCIA DE CAMBIOS ADMINISTRATIVOS
    
    private void actualizarPensum(Cambio cambio) {

        PlanificadorAcademico candidato =
                reconstruirDesdeBD();

        // Primero se verifica el cambio con las estructuras Java.

        aplicarCambio(candidato, cambio);

        if (!candidato.validarPensum()) {

            throw new IllegalArgumentException(
                    "El cambio genera un ciclo academico.");
        }

        // Solo se persisten los cambios aceptados.

        jdbc.update(
                """
                INSERT INTO cambios_pensum
                (operacion, codigo, destino, nombre,
                 uv, ciclo, area, obligatoria)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """,

                cambio.operacion(),
                cambio.codigo(),
                cambio.destino(),
                cambio.nombre(),
                cambio.uv(),
                cambio.ciclo(),
                cambio.area(),
                cambio.obligatoria()
        );

        planificador = candidato;
    }

    private void aplicarCambio(
            PlanificadorAcademico destino,
            Cambio cambio) {

        switch (cambio.operacion()) {

            case "REGISTRAR" ->

                    destino.registrarMateria(
                            new Materia(
                                    cambio.codigo(),
                                    cambio.nombre(),
                                    cambio.uv(),
                                    cambio.ciclo(),
                                    cambio.area(),
                                    cambio.obligatoria()));

            case "MODIFICAR" ->

                    destino.modificarMateria(
                            cambio.codigo(),
                            cambio.nombre(),
                            cambio.uv(),
                            cambio.ciclo(),
                            cambio.area(),
                            cambio.obligatoria());

            case "AGREGAR_RELACION" ->

                    destino.agregarPrerequisito(
                            cambio.codigo(),
                            cambio.destino());

            case "ELIMINAR_RELACION" -> {

                boolean eliminado =
                        destino.eliminarPrerequisito(
                                cambio.codigo(),
                                cambio.destino());

                if (!eliminado) {

                    throw new IllegalArgumentException(
                            "La relacion indicada no existe.");
                }
            }

            default -> throw new IllegalArgumentException(
                    "Operacion desconocida.");
        }
    }

    /// VALIDACION DEL GRAFO Y ORDEN TOPOLOGICO
    
    public boolean pensumValido() {

        return planificador.validarPensumOficialICC();
    }

    public boolean grafoSinCiclos() {

        return planificador.validarPensum();
    }

    public String mostrarGrafo() {

        return planificador.mostrarGrafo();
    }

    public String mostrarPensumJerarquico() {

        return planificador.mostrarPensumJerarquico();
    }

    public List<Materia> generarOrdenAcademico() {

        // No ordenar esta lista: debe conservar el orden de Kahn.

        return convertir(
                planificador.generarOrdenAcademico());
    }

    /// VALIDACIONES DE ENTRADA
    
    private String normalizarCodigo(String codigo) {

        if (codigo == null) {

            throw new IllegalArgumentException(
                    "El codigo es obligatorio.");
        }

        String resultado =
                codigo.trim().toUpperCase(Locale.ROOT);

        if (!resultado.matches("[A-Z0-9_-]{1,20}")) {

            throw new IllegalArgumentException(
                    "El codigo de materia no es valido.");
        }

        return resultado;
    }

    private void validarDatos(
            String nombre,
            int uv,
            int ciclo,
            String area) {

        if (nombre == null
                || nombre.isBlank()
                || nombre.length() > 250) {

            throw new IllegalArgumentException(
                    "El nombre de la materia no es valido.");
        }

        if (uv < 1 || uv > 30) {

            throw new IllegalArgumentException(
                    "Las UV deben estar entre 1 y 30.");
        }

        if (ciclo < 1 || ciclo > 10) {

            throw new IllegalArgumentException(
                    "El ciclo debe estar entre 1 y 10.");
        }

        if (area == null
                || area.isBlank()
                || area.length() > 100) {

            throw new IllegalArgumentException(
                    "El area academica no es valida.");
        }
    }

    /// CONVERSION PARA THYMELEAF
    
    private List<Materia> convertir(
            ListaEnlazada<Materia> origen) {

        List<Materia> resultado = new ArrayList<>();

        if (origen == null) {

            return resultado;
        }

        for (int i = 0; i < origen.tamanio(); i++) {

            resultado.add(origen.obtener(i));
        }

        return resultado;
    }

    private List<Materia> ordenar(List<Materia> materias) {

        materias.sort(
                Comparator.comparingInt(Materia::getCiclo)
                        .thenComparing(Materia::getCodigo));

        return materias;
    }

    /// REGISTRO DE CAMBIOS
    
    private record Cambio(
            String operacion,
            String codigo,
            String destino,
            String nombre,
            int uv,
            int ciclo,
            String area,
            boolean obligatoria) {
    }
}
