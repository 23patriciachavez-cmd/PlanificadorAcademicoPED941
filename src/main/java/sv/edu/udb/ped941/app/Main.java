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

    private static final AutenticacionService
            autenticacion =
            new AutenticacionService();

    private static final PlanificadorAcademico
            planificador =
            new PlanificadorAcademico();

    public static void main(
            String[] args) {

        planificador
                .cargarDatosEjemplo();

        boolean salir =
                false;

        while (!salir) {

            System.out.println();
            System.out.println(
                    "======================================");

            System.out.println(
                    "      PLANIFICADOR ACADÉMICO");

            System.out.println(
                    "======================================");

            System.out.println(
                    "Escriba 0 como usuario para salir.");

            String nombreUsuario =
                    leerTexto(
                            "Usuario: ");

            if (nombreUsuario
                    .equals("0")) {

                salir = true;
                continue;
            }

            String password =
                    leerTexto(
                            "Contraseña: ");

            Usuario usuario =
                    autenticacion
                            .iniciarSesion(
                                    nombreUsuario,
                                    password);

            if (usuario == null) {

                System.out.println(
                        "\nUsuario o contraseña incorrectos.");

                continue;
            }

            System.out.println(
                    "\nBienvenido(a), "
                    + usuario.getNombreUsuario());

            System.out.println(
                    "Rol: "
                    + usuario.getRol());

            if (usuario.getRol()
                    == Rol.ADMINISTRADOR) {

                menuAdministrador();

            } else {

                menuEstudiante();
            }
        }

        System.out.println(
                "\nPrograma finalizado.");
    }

    private static void menuEstudiante() {

        int opcion;

        do {

            System.out.println();
            System.out.println(
                    "========== MENÚ ESTUDIANTE ==========");

            System.out.println(
                    "1. Mostrar pensum");

            System.out.println(
                    "2. Listar materias");

            System.out.println(
                    "3. Buscar materia");

            System.out.println(
                    "4. Registrar materia aprobada");

            System.out.println(
                    "5. Consultar materias disponibles");

            System.out.println(
                    "6. Consultar materias bloqueadas");

            System.out.println(
                    "7. Consultar prerrequisitos pendientes");

            System.out.println(
                    "8. Generar orden académico");

            System.out.println(
                    "0. Cerrar sesión");

            opcion =
                    leerEntero(
                            "Seleccione una opción: ");

            try {

                switch (opcion) {

                    case 1 ->
                            System.out.println(
                                    planificador
                                            .mostrarPensumJerarquico());

                    case 2 ->
                            mostrarLista(
                                    "MATERIAS",
                                    planificador
                                            .listarMaterias());

                    case 3 ->
                            buscarMateria();

                    case 4 ->
                            marcarAprobada();

                    case 5 ->
                            mostrarLista(
                                    "MATERIAS DISPONIBLES",
                                    planificador
                                            .obtenerMateriasDisponibles());

                    case 6 ->
                            mostrarLista(
                                    "MATERIAS BLOQUEADAS",
                                    planificador
                                            .obtenerMateriasBloqueadas());

                    case 7 ->
                            consultarPendientes();

                    case 8 ->
                            mostrarLista(
                                    "ORDEN ACADÉMICO",
                                    planificador
                                            .generarOrdenAcademico());

                    case 0 ->
                            System.out.println(
                                    "Sesión cerrada.");

                    default ->
                            System.out.println(
                                    "Opción inválida.");
                }

            } catch (
                    IllegalArgumentException
                    | IllegalStateException e) {

                System.out.println(
                        "ERROR: "
                        + e.getMessage());
            }

        } while (opcion != 0);
    }

    private static void menuAdministrador() {

        int opcion;

        do {

            System.out.println();
            System.out.println(
                    "======= MENÚ ADMINISTRADOR =======");

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
                    "0. Cerrar sesión");

            opcion =
                    leerEntero(
                            "Seleccione una opción: ");

            try {

                switch (opcion) {

                    case 1 ->
                            System.out.println(
                                    planificador
                                            .mostrarPensumJerarquico());

                    case 2 ->
                            mostrarLista(
                                    "MATERIAS",
                                    planificador
                                            .listarMaterias());

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
                            System.out.println(
                                    planificador
                                            .mostrarGrafo());

                    case 9 ->
                            System.out.println(
                                    planificador
                                            .validarPensum()
                                    ? "El pensum es válido."
                                    : "El pensum contiene "
                                      + "dependencias circulares.");

                    case 10 ->
                            mostrarLista(
                                    "ORDEN ACADÉMICO",
                                    planificador
                                            .generarOrdenAcademico());

                    case 0 ->
                            System.out.println(
                                    "Sesión cerrada.");

                    default ->
                            System.out.println(
                                    "Opción inválida.");
                }

            } catch (
                    IllegalArgumentException
                    | IllegalStateException e) {

                System.out.println(
                        "ERROR: "
                        + e.getMessage());
            }

        } while (opcion != 0);
    }

    private static void buscarMateria() {

        String codigo =
                leerTexto(
                        "Código: ");

        Materia materia =
                planificador
                        .buscarMateria(
                                codigo);

        if (materia == null) {

            System.out.println(
                    "Materia no encontrada.");

            return;
        }

        System.out.println(
                "\nCódigo: "
                + materia.getCodigo());

        System.out.println(
                "Nombre: "
                + materia.getNombre());

        System.out.println(
                "Créditos: "
                + materia.getCreditos());

        System.out.println(
                "Ciclo: "
                + materia.getCiclo());

        System.out.println(
                "Área: "
                + materia.getArea());

        System.out.println(
                "Tipo: "
                + (materia.isObligatoria()
                   ? "Obligatoria"
                   : "Optativa"));

        System.out.println(
                "Estado: "
                + (materia.isAprobada()
                   ? "Aprobada"
                   : "Pendiente"));
    }

    private static void registrarMateria() {

        String codigo =
                leerTexto(
                        "Código: ");

        String nombre =
                leerTexto(
                        "Nombre: ");

        int creditos =
                leerEntero(
                        "Créditos: ");

        int ciclo =
                leerEntero(
                        "Ciclo: ");

        String area =
                leerTexto(
                        "Área: ");

        String tipo =
                leerTexto(
                        "¿Es obligatoria? (S/N): ");

        boolean obligatoria =
                tipo.equalsIgnoreCase(
                        "S");

        Materia materia =
                new Materia(
                        codigo,
                        nombre,
                        creditos,
                        ciclo,
                        area,
                        obligatoria);

        planificador
                .registrarMateria(
                        materia);

        System.out.println(
                "Materia registrada correctamente.");
    }

    private static void modificarMateria() {

        String codigo =
                leerTexto(
                        "Código de la materia: ");

        Materia actual =
                planificador
                        .buscarMateria(
                                codigo);

        if (actual == null) {

            System.out.println(
                    "Materia no encontrada.");

            return;
        }

        String nombre =
                leerTexto(
                        "Nuevo nombre: ");

        int creditos =
                leerEntero(
                        "Créditos: ");

        int ciclo =
                leerEntero(
                        "Ciclo: ");

        String area =
                leerTexto(
                        "Área: ");

        String tipo =
                leerTexto(
                        "¿Es obligatoria? (S/N): ");

        planificador
                .modificarMateria(
                        codigo,
                        nombre,
                        creditos,
                        ciclo,
                        area,
                        tipo.equalsIgnoreCase(
                                "S"));

        System.out.println(
                "Materia modificada correctamente.");
    }

    private static void agregarPrerequisito() {

        String pre =
                leerTexto(
                        "Código del prerrequisito: ");

        String materia =
                leerTexto(
                        "Código de la materia dependiente: ");

        planificador
                .agregarPrerequisito(
                        pre,
                        materia);

        System.out.println(
                "Prerrequisito agregado correctamente.");
    }

    private static void eliminarPrerequisito() {

        String pre =
                leerTexto(
                        "Código del prerrequisito: ");

        String materia =
                leerTexto(
                        "Código de la materia dependiente: ");

        boolean eliminado =
                planificador
                        .eliminarPrerequisito(
                                pre,
                                materia);

        System.out.println(
                eliminado
                ? "Relación eliminada correctamente."
                : "La relación no existe.");
    }

    private static void marcarAprobada() {

        String codigo =
                leerTexto(
                        "Código de materia aprobada: ");

        boolean resultado =
                planificador
                        .marcarMateriaAprobada(
                                codigo);

        System.out.println(
                resultado
                ? "Materia registrada como aprobada."
                : "La materia ya estaba aprobada.");
    }

    private static void consultarPendientes() {

        String codigo =
                leerTexto(
                        "Código de la materia: ");

        ListaEnlazada<Materia> pendientes =
                planificador
                        .obtenerPrerequisitosPendientes(
                                codigo);

        if (pendientes.estaVacia()) {

            System.out.println(
                    "La materia no tiene "
                    + "prerrequisitos pendientes.");

            return;
        }

        mostrarLista(
                "PRERREQUISITOS PENDIENTES",
                pendientes);
    }

    private static void mostrarLista(
            String titulo,
            ListaEnlazada<Materia> lista) {

        System.out.println();
        System.out.println(
                "=== "
                + titulo
                + " ===");

        if (lista.estaVacia()) {

            System.out.println(
                    "No hay elementos para mostrar.");

            return;
        }

        for (int i = 0;
             i < lista.tamanio();
             i++) {

            System.out.println(
                    (i + 1)
                    + ". "
                    + lista.obtener(i));
        }
    }

    private static String leerTexto(
            String mensaje) {

        System.out.print(
                mensaje);

        return scanner
                .nextLine()
                .trim();
    }

    private static int leerEntero(
            String mensaje) {

        while (true) {

            try {

                return Integer.parseInt(
                        leerTexto(
                                mensaje));

            } catch (
                    NumberFormatException e) {

                System.out.println(
                        "Ingrese un número válido.");
            }
        }
    }
}
