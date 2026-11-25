package sv.edu.udb.ped941.app;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Locale;
import java.util.function.Supplier;

import sv.edu.udb.ped941.servicio.PlanificadorAcademico;

public class MedicionRendimiento {

    private static final int CALENTAMIENTO = 100;

    private static final int ITERACIONES = 200;

    private static final int REPETICIONES = 5;

    private static volatile Object resultado;

    private static final StringBuilder csv =
            new StringBuilder();

    /// PROGRAMA PRINCIPAL
    
    public static void main(String[] args)
            throws IOException {

        PlanificadorAcademico planificador =
                new PlanificadorAcademico();

        planificador.cargarPensumICC2024();

        // Escenario de prueba: estudiante con primer ciclo aprobado.

        planificador.marcarMateriaAprobada("CAD501");

        planificador.marcarMateriaAprobada("QUG501");

        planificador.marcarMateriaAprobada("ANF231");

        planificador.marcarMateriaAprobada("PRE104");

        System.out.println(
                "==================================================");

        System.out.println(
                "       MEDICION DE RENDIMIENTO PED941");

        System.out.println(
                "==================================================");

        System.out.println(
                "Fecha: " + LocalDateTime.now());

        System.out.println(
                "Java: "
                + System.getProperty("java.version"));

        System.out.println(
                "Sistema operativo: "
                + System.getProperty("os.name"));

        System.out.println(
                "Procesadores disponibles: "
                + Runtime.getRuntime()
                        .availableProcessors());

        System.out.println(
                "Materias: "
                + planificador.obtenerTotalMaterias());

        System.out.println(
                "UV: "
                + planificador.obtenerTotalUV());

        System.out.println(
                "Iteraciones por repeticion: "
                + ITERACIONES);

        System.out.println(
                "Repeticiones: "
                + REPETICIONES);

        System.out.println();

        System.out.printf(
                "%-34s %14s %14s%n",
                "OPERACION",
                "MEDIA (us)",
                "MEDIANA (us)");

        System.out.println(
                "----------------------------------------------------------------");

        csv.append(
                "operacion,iteraciones_por_repeticion,"
                + "repeticiones,media_microsegundos,"
                + "mediana_microsegundos\n");

        /// BUSQUEDA MEDIANTE TABLA HASH
        
        medir(
                "Busqueda de materia existente",
                () -> planificador.buscarMateria("DES104"));

        medir(
                "Busqueda de materia inexistente",
                () -> planificador.buscarMateria("XXX999"));

        /// LISTADO Y CONSULTAS DEL GRAFO
        
        medir(
                "Listado completo del pensum",
                planificador::listarMaterias);

        medir(
                "Consulta de prerrequisitos",
                () -> planificador.obtenerPrerequisitos(
                        "DES104"));

        medir(
                "Materias disponibles",
                planificador::obtenerMateriasDisponibles);

        medir(
                "Materias bloqueadas",
                planificador::obtenerMateriasBloqueadas);

        /// ALGORITMOS DEL GRAFO
        
        medir(
                "Deteccion de ciclos DFS",
                planificador::validarPensum);

        medir(
                "Ordenamiento topologico Kahn",
                planificador::generarOrdenAcademico);

        /// ARBOL CURRICULAR
        
        medir(
                "Arbol curricular",
                planificador::mostrarPensumJerarquico);

        /// PROGRESO ACADEMICO
        
        medir(
                "Calculo del progreso",
                planificador::obtenerResumenProgreso);

        medir(
                "Simulador del proximo ciclo",
                planificador::simularProximoCiclo);

        medir(
                "Busqueda de electiva",
                () -> planificador.buscarElectiva("ACD104"));

        /// EXPORTACION DE RESULTADOS
        
        Path carpeta = Path.of("docs");

        Files.createDirectories(carpeta);

        Path archivo = carpeta.resolve(
                "rendimiento.csv");

        Files.writeString(
                archivo,
                csv.toString(),
                StandardCharsets.UTF_8);

        System.out.println();

        System.out.println(
                "==================================================");

        System.out.println(
                "MEDICION FINALIZADA");

        System.out.println(
                "Resultados guardados en:");

        System.out.println(
                archivo.toAbsolutePath());

        System.out.println(
                "==================================================");
    }

    /// METODO DE MEDICION
    
    private static void medir(
            String nombre,
            Supplier<?> operacion) {

        // Calentamiento de la maquina virtual Java.

        for (int i = 0; i < CALENTAMIENTO; i++) {

            resultado = operacion.get();
        }

        double[] tiempos =
                new double[REPETICIONES];

        for (int r = 0; r < REPETICIONES; r++) {

            long inicio = System.nanoTime();

            for (int i = 0; i < ITERACIONES; i++) {

                resultado = operacion.get();
            }

            long fin = System.nanoTime();

            double promedioNanosegundos =
                    (fin - inicio)
                    / (double) ITERACIONES;

            tiempos[r] =
                    promedioNanosegundos / 1000.0;
        }

        double media = Arrays.stream(tiempos)
                .average()
                .orElse(0.0);

        Arrays.sort(tiempos);

        double mediana =
                tiempos[REPETICIONES / 2];

        System.out.printf(
                Locale.US,
                "%-34s %14.3f %14.3f%n",
                nombre,
                media,
                mediana);

        csv.append(nombre)
                .append(",")
                .append(ITERACIONES)
                .append(",")
                .append(REPETICIONES)
                .append(",")
                .append(String.format(
                        Locale.US,
                        "%.6f",
                        media))
                .append(",")
                .append(String.format(
                        Locale.US,
                        "%.6f",
                        mediana))
                .append("\n");
    }
}
