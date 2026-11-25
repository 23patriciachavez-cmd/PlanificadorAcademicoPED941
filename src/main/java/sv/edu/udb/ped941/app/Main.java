package sv.edu.udb.ped941.app;

import java.util.Scanner;

import sv.edu.udb.ped941.estructuras.ListaEnlazada;
import sv.edu.udb.ped941.modelo.Materia;
import sv.edu.udb.ped941.modelo.Rol;
import sv.edu.udb.ped941.modelo.Usuario;
import sv.edu.udb.ped941.servicio.AutenticacionService;
import sv.edu.udb.ped941.servicio.PlanificadorAcademico;

public class Main {

    private static final Scanner scanner =
            new Scanner(System.in);

    private static final PlanificadorAcademico planificador =
            new PlanificadorAcademico();

    private static final AutenticacionService autenticacion =
            new AutenticacionService();


    /// MAIN
    
    public static void main(String[] args) {

        /*
         * Carga el pensum real ICC:
         *
         * 40 materias
         * 161 UV
         * prerrequisitos reales
         * catálogo de electivas
         */
        planificador.cargarPensumICC2024();

        boolean ejecutar =
                true;

        while (ejecutar) {

            mostrarEncabezado();

            System.out.println(
                    "1. Iniciar sesión");

            System.out.println(
                    "0. Salir");

            int opcion =
                    leerEntero(
                            "Seleccione una opción: ");

            switch (opcion) {

                case 1 ->
                        iniciarSesion();

                case 0 -> {

                    ejecutar =
                            false;

                    System.out.println();
                    System.out.println(
                            "Programa finalizado.");
                }

                default ->
                        System.out.println(
                                "Opción no válida.");
            }
        }
    }


    /// ENCABEZADO
    
    private static void mostrarEncabezado() {

        System.out.println();
        System.out.println(
                "==============================================");

        System.out.println(
                "        PLANIFICADOR ACADÉMICO PED941");

        System.out.println(
                " Ingeniería en Ciencias de la Computación");

        System.out.println(
                "==============================================");
    }


    /// AUTENTICACIÓN
   
    private static void iniciarSesion() {

        System.out.println();
        System.out.println(
                "========== INICIO DE SESIÓN ==========");

        String nombreUsuario =
                leerTexto(
                        "Usuario: ");

        String password =
                leerTexto(
                        "Contraseña: ");

        Usuario usuario =
                autenticacion.iniciarSesion(
                        nombreUsuario,
                        password);

        if (usuario == null) {

            System.out.println();
            System.out.println(
                    "Usuario o contraseña incorrectos.");

            return;
        }

        System.out.println();
        System.out.println(
                "Inicio de sesión exitoso.");

        if (usuario.getRol()
                == Rol.ADMINISTRADOR) {

            menuAdministrador();

        } else if (usuario.getRol()
                == Rol.ESTUDIANTE) {

            menuEstudiante();

        } else {

            System.out.println(
                    "El usuario no posee un rol válido.");
        }
    }


    /// MENÚ ADMINISTRADOR
    
    private static void menuAdministrador() {

        boolean sesionActiva =
                true;

        while (sesionActiva) {

            System.out.println();
            System.out.println(
                    "==============================================");

            System.out.println(
                    "             MENÚ ADMINISTRADOR");

            System.out.println(
                    "==============================================");

            System.out.println(
                    "1. Mostrar pensum");

            System.out.println(
                    "2. Listar materias");

            System.out.println(
                    "3. Buscar materia");

            System.out.println(
                    "4. Registrar nueva materia");

            System.out.println(
                    "5. Modificar materia");

            System.out.println(
                    "6. Agregar prerrequisito");

            System.out.println(
                    "7. Eliminar prerrequisito");

            System.out.println(
                    "8. Mostrar grafo");

            System.out.println(
                    "9. Validar pensum");

            System.out.println(
                    "10. Generar orden académico");

            System.out.println(
                    "11. Mostrar métricas del pensum");

            System.out.println(
                    "12. Mostrar catálogo de electivas");

            System.out.println(
                    "0. Cerrar sesión");

            int opcion =
                    leerEntero(
                            "Seleccione una opción: ");

            try {

                switch (opcion) {

                    case 1 ->
                            mostrarPensum();

                    case 2 ->
                            listarMaterias();

                    case 3 ->
                            buscarMateria();

                    case 4 ->
                            registrarMateria();

                    case 5 ->
                            modificarMateria();

                    case 6 ->
                            agregarPrerequisito();

                    case 7 ->
                            eliminarPrerequisito();

                    case 8 ->
                            mostrarGrafo();

                    case 9 ->
                            validarPensum();

                    case 10 ->
                            generarOrdenAcademico();

                    case 11 ->
                            mostrarMetricasPensum();

                    case 12 ->
                            mostrarCatalogoElectivas();

                    case 0 -> {

                        sesionActiva =
                                false;

                        System.out.println();
                        System.out.println(
                                "Sesión cerrada.");
                    }

                    default ->
                            System.out.println(
                                    "Opción no válida.");
                }

            } catch (IllegalArgumentException ex) {

                System.out.println();
                System.out.println(
                        "ERROR: "
                        + ex.getMessage());
            }
        }
    }


    /// MENÚ ESTUDIANTE
    
    private static void menuEstudiante() {

        boolean sesionActiva =
                true;

        while (sesionActiva) {

            System.out.println();
            System.out.println(
                    "==============================================");

            System.out.println(
                    "              MENÚ ESTUDIANTE");

            System.out.println(
                    "==============================================");

            System.out.println(
                    "1. Mostrar pensum");

            System.out.println(
                    "2. Listar materias");

            System.out.println(
                    "3. Buscar materia");

            System.out.println(
                    "4. Marcar materia aprobada");

            System.out.println(
                    "5. Desmarcar materia aprobada");

            System.out.println(
                    "6. Consultar materias disponibles");

            System.out.println(
                    "7. Consultar materias bloqueadas");

            System.out.println(
                    "8. Consultar prerrequisitos pendientes");

            System.out.println(
                    "9. Generar orden académico");

            System.out.println(
                    "10. Ver progreso académico");

            System.out.println(
                    "11. Simular próximo ciclo");

            System.out.println(
                    "12. Consultar electivas");

            System.out.println(
                    "13. Seleccionar electiva");

            System.out.println(
                    "14. Ver electiva seleccionada");

            System.out.println(
                    "15. Limpiar electiva seleccionada");

            System.out.println(
                    "0. Cerrar sesión");

            int opcion =
                    leerEntero(
                            "Seleccione una opción: ");

            try {

                switch (opcion) {

                    case 1 ->
                            mostrarPensum();

                    case 2 ->
                            listarMaterias();

                    case 3 ->
                            buscarMateria();

                    case 4 ->
                            marcarMateriaAprobada();

                    case 5 ->
                            desmarcarMateriaAprobada();

                    case 6 ->
                            mostrarMateriasDisponibles();

                    case 7 ->
                            mostrarMateriasBloqueadas();

                    case 8 ->
                            mostrarPrerequisitosPendientes();

                    case 9 ->
                            generarOrdenAcademico();

                    case 10 ->
                            mostrarProgresoAcademico();

                    case 11 ->
                            simularProximoCiclo();

                    case 12 ->
                            mostrarCatalogoElectivas();

                    case 13 ->
                            seleccionarElectiva();

                    case 14 ->
                            mostrarElectivaSeleccionada();

                    case 15 ->
                            limpiarElectivaSeleccionada();

                    case 0 -> {

                        sesionActiva =
                                false;

                        System.out.println();
                        System.out.println(
                                "Sesión cerrada.");
                    }

                    default ->
                            System.out.println(
                                    "Opción no válida.");
                }

            } catch (IllegalArgumentException ex) {

                System.out.println();
                System.out.println(
                        "ERROR: "
                        + ex.getMessage());
            }
        }
    }


    /// MOSTRAR PENSUM
    
    private static void mostrarPensum() {

        System.out.println();
        System.out.println(
                "========== PENSUM ICC ==========");

        System.out.println(
                planificador
                        .mostrarPensumJerarquico());
    }


    /// LISTAR MATERIAS
    
    private static void listarMaterias() {

        System.out.println();
        System.out.println(
                "========== MATERIAS DEL PENSUM ==========");

        ListaEnlazada<Materia> materias =
                planificador
                        .listarMaterias();

        mostrarListaMaterias(
                materias);
    }


    /// BUSCAR MATERIA
    
    private static void buscarMateria() {

        String codigo =
                leerTexto(
                        "Código de la materia: ");

        Materia materia =
                planificador
                        .buscarMateria(
                                codigo);

        if (materia == null) {

            System.out.println(
                    "Materia no encontrada.");

            return;
        }

        System.out.println();
        System.out.println(
                "========== MATERIA ENCONTRADA ==========");

        mostrarDetalleMateria(
                materia);
    }


    /// REGISTRAR MATERIA
    
    private static void registrarMateria() {

        System.out.println();
        System.out.println(
                "========== REGISTRAR MATERIA ==========");

        String codigo =
                leerTexto(
                        "Código: ");

        String nombre =
                leerTexto(
                        "Nombre: ");

        int uv =
                leerEntero(
                        "Unidades valorativas: ");

        int ciclo =
                leerEntero(
                        "Ciclo: ");

        String area =
                leerTexto(
                        "Área académica: ");

        boolean obligatoria =
                leerSiNo(
                        "¿Es obligatoria? (S/N): ");

        Materia materia =
                new Materia(
                        codigo,
                        nombre,
                        uv,
                        ciclo,
                        area,
                        obligatoria);

        planificador
                .registrarMateria(
                        materia);

        System.out.println();
        System.out.println(
                "Materia registrada correctamente.");
    }


    /// MODIFICAR MATERIA
    
    private static void modificarMateria() {

        System.out.println();
        System.out.println(
                "========== MODIFICAR MATERIA ==========");

        String codigo =
                leerTexto(
                        "Código de la materia: ");

        Materia existente =
                planificador
                        .buscarMateria(
                                codigo);

        if (existente == null) {

            System.out.println(
                    "La materia no existe.");

            return;
        }

        System.out.println();
        System.out.println(
                "Datos actuales:");

        mostrarDetalleMateria(
                existente);

        System.out.println();
        System.out.println(
                "Ingrese los nuevos datos.");

        String nombre =
                leerTexto(
                        "Nombre: ");

        int uv =
                leerEntero(
                        "Unidades valorativas: ");

        int ciclo =
                leerEntero(
                        "Ciclo: ");

        String area =
                leerTexto(
                        "Área académica: ");

        boolean obligatoria =
                leerSiNo(
                        "¿Es obligatoria? (S/N): ");

        planificador
                .modificarMateria(
                        codigo,
                        nombre,
                        uv,
                        ciclo,
                        area,
                        obligatoria);

        System.out.println();
        System.out.println(
                "Materia modificada correctamente.");
    }


    /// AGREGAR PRERREQUISITO
    
    private static void agregarPrerequisito() {

        System.out.println();
        System.out.println(
                "========== AGREGAR PRERREQUISITO ==========");

        String prerequisito =
                leerTexto(
                        "Código del prerrequisito: ");

        String materia =
                leerTexto(
                        "Código de la materia dependiente: ");

        planificador
                .agregarPrerequisito(
                        prerequisito,
                        materia);

        System.out.println();
        System.out.println(
                "Prerrequisito agregado correctamente.");
    }


    /// ELIMINAR PRERREQUISITO
    
    private static void eliminarPrerequisito() {

        System.out.println();
        System.out.println(
                "========== ELIMINAR PRERREQUISITO ==========");

        String prerequisito =
                leerTexto(
                        "Código del prerrequisito: ");

        String materia =
                leerTexto(
                        "Código de la materia dependiente: ");

        boolean eliminado =
                planificador
                        .eliminarPrerequisito(
                                prerequisito,
                                materia);

        if (eliminado) {

            System.out.println(
                    "Prerrequisito eliminado correctamente.");

        } else {

            System.out.println(
                    "La relación indicada no existe.");
        }
    }


    /// MOSTRAR GRAFO
    
    private static void mostrarGrafo() {

        System.out.println();
        System.out.println(
                "========== GRAFO ACADÉMICO ==========");

        System.out.println(
                planificador
                        .mostrarGrafo());
    }


    /// VALIDAR PENSUM
    
    private static void validarPensum() {

        System.out.println();
        System.out.println(
                "==============================================");

        System.out.println(
                "       VALIDACIÓN INTEGRAL DEL PENSUM");

        System.out.println(
                "==============================================");

        System.out.println(
                "Materias registradas: "
                + planificador.obtenerTotalMaterias()
                + " / "
                + PlanificadorAcademico
                        .TOTAL_MATERIAS_PENSUM);

        System.out.println(
                "Unidades valorativas: "
                + planificador.obtenerTotalUV()
                + " / "
                + PlanificadorAcademico
                        .TOTAL_UV_PENSUM);

        System.out.println(
                "Grafo sin ciclos: "
                + (
                planificador.validarPensum()
                        ? "SÍ"
                        : "NO"));

        System.out.println();

        if (planificador
                .validarPensumOficialICC()) {

            System.out.println(
                    "RESULTADO: PENSUM ICC VÁLIDO");

        } else {

            System.out.println(
                    "RESULTADO: EL PENSUM REQUIERE REVISIÓN");
        }
    }


    /// ORDEN ACADÉMICO
    
    private static void generarOrdenAcademico() {

        System.out.println();
        System.out.println(
                "========== ORDEN ACADÉMICO ==========");

        ListaEnlazada<Materia> orden =
                planificador
                        .generarOrdenAcademico();

        if (orden.estaVacia()) {

            System.out.println(
                    "No fue posible generar el orden.");

            return;
        }

        for (int i = 0;
             i < orden.tamanio();
             i++) {

            Materia materia =
                    orden.obtener(i);

            System.out.printf(
                    "%2d. %-8s | %s%n",
                    i + 1,
                    materia.getCodigo(),
                    materia.getNombre());
        }
    }


    /// MÉTRICAS DEL PENSUM
    
    private static void mostrarMetricasPensum() {

        System.out.println();
        System.out.println(
                "========== MÉTRICAS DEL PENSUM ==========");

        System.out.println(
                "Total de materias: "
                + planificador
                        .obtenerTotalMaterias());

        System.out.println(
                "Total de UV: "
                + planificador
                        .obtenerTotalUV());

        System.out.println(
                "Electivas disponibles: "
                + planificador
                        .listarElectivas()
                        .tamanio());

        System.out.println(
                "Pensum válido: "
                + (
                planificador
                        .validarPensumOficialICC()
                        ? "SÍ"
                        : "NO"));
    }


    /// MARCAR MATERIA APROBADA
    
    private static void marcarMateriaAprobada() {

        String codigo =
                leerTexto(
                        "Código de la materia aprobada: ");

        boolean resultado =
                planificador
                        .marcarMateriaAprobada(
                                codigo);

        if (resultado) {

            System.out.println(
                    "Materia marcada como aprobada.");

        } else {

            System.out.println(
                    "La materia ya estaba aprobada.");
        }
    }


    /// DESMARCAR MATERIA
    
    private static void desmarcarMateriaAprobada() {

        String codigo =
                leerTexto(
                        "Código de la materia: ");

        boolean resultado =
                planificador
                        .desmarcarMateriaAprobada(
                                codigo);

        if (resultado) {

            System.out.println(
                    "La materia dejó de estar marcada "
                    + "como aprobada.");

        } else {

            System.out.println(
                    "La materia no estaba marcada "
                    + "como aprobada.");
        }
    }


    /// MATERIAS DISPONIBLES
    
    private static void mostrarMateriasDisponibles() {

        System.out.println();
        System.out.println(
                "========== MATERIAS DISPONIBLES ==========");

        ListaEnlazada<Materia> disponibles =
                planificador
                        .obtenerMateriasDisponibles();

        mostrarListaMaterias(
                disponibles);
    }


    /// MATERIAS BLOQUEADAS
    
    private static void mostrarMateriasBloqueadas() {

        System.out.println();
        System.out.println(
                "========== MATERIAS BLOQUEADAS ==========");

        ListaEnlazada<Materia> bloqueadas =
                planificador
                        .obtenerMateriasBloqueadas();

        if (bloqueadas.estaVacia()) {

            System.out.println(
                    "No existen materias bloqueadas.");

            return;
        }

        for (int i = 0;
             i < bloqueadas.tamanio();
             i++) {

            Materia materia =
                    bloqueadas.obtener(i);

            System.out.println();
            System.out.println(
                    materia.getCodigo()
                    + " - "
                    + materia.getNombre());

            ListaEnlazada<Materia> pendientes =
                    planificador
                            .obtenerPrerequisitosPendientes(
                                    materia.getCodigo());

            System.out.print(
                    "   Pendientes: ");

            for (int j = 0;
                 j < pendientes.tamanio();
                 j++) {

                System.out.print(
                        pendientes
                                .obtener(j)
                                .getCodigo());

                if (j
                        < pendientes.tamanio() - 1) {

                    System.out.print(
                            ", ");
                }
            }

            System.out.println();
        }
    }


    /// PRERREQUISITOS PENDIENTES
    
    private static void mostrarPrerequisitosPendientes() {

        String codigo =
                leerTexto(
                        "Código de la materia: ");

        ListaEnlazada<Materia> pendientes =
                planificador
                        .obtenerPrerequisitosPendientes(
                                codigo);

        System.out.println();

        if (pendientes.estaVacia()) {

            System.out.println(
                    "La materia no tiene "
                    + "prerrequisitos pendientes.");

            return;
        }

        System.out.println(
                "Prerrequisitos pendientes:");

        mostrarListaMaterias(
                pendientes);
    }


    /// PROGRESO ACADÉMICO
    
    private static void mostrarProgresoAcademico() {

        System.out.println();
        System.out.println(
                planificador
                        .obtenerResumenProgreso());
    }


    /// SIMULADOR DEL PRÓXIMO CICLO
    
    private static void simularProximoCiclo() {

        System.out.println();
        System.out.println(
                "==============================================");

        System.out.println(
                "          SIMULADOR DEL PRÓXIMO CICLO");

        System.out.println(
                "==============================================");

        int ciclo =
                planificador
                        .obtenerSiguienteCicloAcademico();

        if (ciclo == 0) {

            System.out.println(
                    "¡Felicidades!");

            System.out.println(
                    "Has completado todas las materias "
                    + "del plan de estudios.");

            return;
        }

        System.out.println(
                "Siguiente ciclo pendiente: "
                + ciclo);

        ListaEnlazada<Materia> materias =
                planificador
                        .simularProximoCiclo();

        System.out.println();

        if (materias.estaVacia()) {

            System.out.println(
                    "No hay materias habilitadas "
                    + "para este ciclo.");

            System.out.println(
                    "Revisa los prerrequisitos pendientes.");

            return;
        }

        System.out.println(
                "Materias que puedes cursar:");

        mostrarListaMaterias(
                materias);
    }


    /// CATÁLOGO DE ELECTIVAS
    
    private static void mostrarCatalogoElectivas() {

        System.out.println();
        System.out.println(
                "========== CATÁLOGO DE ELECTIVAS ==========");

        ListaEnlazada<Materia> electivas =
                planificador
                        .listarElectivas();

        mostrarListaMaterias(
                electivas);

        System.out.println();
        System.out.println(
                "Requisito para las opciones electivas: "
                + "NTI104.");
    }


    /// SELECCIONAR ELECTIVA
    
    private static void seleccionarElectiva() {

        mostrarCatalogoElectivas();

        System.out.println();

        String codigo =
                leerTexto(
                        "Código de la electiva seleccionada: ");

        planificador
                .seleccionarElectiva(
                        codigo);

        Materia electiva =
                planificador
                        .obtenerElectivaSeleccionada();

        System.out.println();
        System.out.println(
                "Electiva seleccionada:");

        mostrarDetalleMateria(
                electiva);

        if (planificador
                .puedeCursarElectivaSeleccionada()) {

            System.out.println(
                    "Estado: HABILITADA PARA CURSAR");

        } else {

            System.out.println(
                    "Estado: BLOQUEADA");

            System.out.println(
                    "Debe aprobar NTI104.");
        }
    }


    /// VER ELECTIVA SELECCIONADA
    
    private static void mostrarElectivaSeleccionada() {

        Materia electiva =
                planificador
                        .obtenerElectivaSeleccionada();

        System.out.println();
        System.out.println(
                "========== ELECTIVA SELECCIONADA ==========");

        if (electiva == null) {

            System.out.println(
                    "Todavía no se ha seleccionado "
                    + "una electiva.");

            return;
        }

        mostrarDetalleMateria(
                electiva);

        System.out.println();

        if (planificador
                .puedeCursarElectivaSeleccionada()) {

            System.out.println(
                    "Estado: HABILITADA");

        } else {

            System.out.println(
                    "Estado: BLOQUEADA POR NTI104");
        }
    }


    /// LIMPIAR ELECTIVA
    
    private static void limpiarElectivaSeleccionada() {

        Materia electiva =
                planificador
                        .obtenerElectivaSeleccionada();

        if (electiva == null) {

            System.out.println(
                    "No existe una electiva seleccionada.");

            return;
        }

        planificador
                .limpiarElectivaSeleccionada();

        System.out.println(
                "Selección de electiva eliminada.");
    }


    /// MOSTRAR LISTA DE MATERIAS
    
    private static void mostrarListaMaterias(
            ListaEnlazada<Materia> materias) {

        if (materias == null
                || materias.estaVacia()) {

            System.out.println(
                    "No hay materias para mostrar.");

            return;
        }

        for (int i = 0;
             i < materias.tamanio();
             i++) {

            Materia materia =
                    materias.obtener(i);

            System.out.printf(
                    "%-8s | %-65s | Ciclo: %2d | UV: %d | %s%n",
                    materia.getCodigo(),
                    materia.getNombre(),
                    materia.getCiclo(),
                    materia.getCreditos(),
                    materia.isAprobada()
                            ? "APROBADA"
                            : "PENDIENTE");
        }
    }


    /// MOSTRAR DETALLE DE MATERIA
    
    private static void mostrarDetalleMateria(
            Materia materia) {

        System.out.println(
                "Código: "
                + materia.getCodigo());

        System.out.println(
                "Nombre: "
                + materia.getNombre());

        System.out.println(
                "UV: "
                + materia.getCreditos());

        System.out.println(
                "Ciclo: "
                + materia.getCiclo());

        System.out.println(
                "Área: "
                + materia.getArea());

        System.out.println(
                "Obligatoria: "
                + (
                materia.isObligatoria()
                        ? "Sí"
                        : "No"));

        System.out.println(
                "Aprobada: "
                + (
                materia.isAprobada()
                        ? "Sí"
                        : "No"));
    }


    /// LEER TEXTO
    
    private static String leerTexto(
            String mensaje) {

        System.out.print(
                mensaje);

        return scanner
                .nextLine()
                .trim();
    }


    /// LEER ENTERO
    
    private static int leerEntero(
            String mensaje) {

        while (true) {

            System.out.print(
                    mensaje);

            String entrada =
                    scanner
                            .nextLine()
                            .trim();

            try {

                return Integer.parseInt(
                        entrada);

            } catch (NumberFormatException ex) {

                System.out.println(
                        "Ingrese un número válido.");
            }
        }
    }


    /// LEER SÍ / NO
    
    private static boolean leerSiNo(
            String mensaje) {

        while (true) {

            String respuesta =
                    leerTexto(
                            mensaje);

            if (respuesta
                    .equalsIgnoreCase(
                            "S")) {

                return true;
            }

            if (respuesta
                    .equalsIgnoreCase(
                            "N")) {

                return false;
            }

            System.out.println(
                    "Ingrese S para Sí o N para No.");
        }
    }
}
