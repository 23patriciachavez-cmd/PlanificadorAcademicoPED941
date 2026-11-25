package sv.edu.udb.ped941.servicio;

import sv.edu.udb.ped941.arbol.ArbolCurricular;
import sv.edu.udb.ped941.estructuras.ListaEnlazada;
import sv.edu.udb.ped941.grafo.GrafoAcademico;
import sv.edu.udb.ped941.modelo.Materia;

public class PlanificadorAcademico {

    /// DATOS OFICIALES DEL PLAN DE ESTUDIOS ICC 2024-2027
    
    public static final int TOTAL_MATERIAS_PENSUM = 40;
    public static final int TOTAL_UV_PENSUM = 161;

    private final GrafoAcademico grafo;
    private final ArbolCurricular arbol;

    private final ListaEnlazada<Materia> catalogoElectivas;

    private Materia electivaSeleccionada;


    /// CONSTRUCTOR
    
    public PlanificadorAcademico() {

        grafo =
                new GrafoAcademico(
                        53);

        arbol =
                new ArbolCurricular(
                        "Plan de Estudios");

        catalogoElectivas =
                new ListaEnlazada<>();

        electivaSeleccionada =
                null;
    }


    /// GESTIÓN DE MATERIAS
    
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


    /// GESTIÓN DE PRERREQUISITOS
    
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


    /// PROGRESO DEL ESTUDIANTE
   
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


    public boolean desmarcarMateriaAprobada(
            String codigo) {

        Materia materia =
                buscarMateria(
                        codigo);

        if (materia == null) {

            throw new IllegalArgumentException(
                    "La materia no existe.");
        }

        if (!materia.isAprobada()) {

            return false;
        }

        materia.setAprobada(
                false);

        return true;
    }


    /// MATERIAS DISPONIBLES
    
    public ListaEnlazada<Materia>
    obtenerMateriasDisponibles() {

        ListaEnlazada<Materia> disponibles =
                new ListaEnlazada<>();

        ListaEnlazada<Materia> materias =
                grafo.obtenerMaterias();

        for (int i = 0;
             i < materias.tamanio();
             i++) {

            Materia materia =
                    materias.obtener(i);

            if (materia.isAprobada()) {

                continue;
            }

            ListaEnlazada<Materia> requisitos =
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


    /// PRERREQUISITOS PENDIENTES
    
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

        ListaEnlazada<Materia> pendientes =
                new ListaEnlazada<>();

        ListaEnlazada<Materia> requisitos =
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


    /// MATERIAS BLOQUEADAS
    
    public ListaEnlazada<Materia>
    obtenerMateriasBloqueadas() {

        ListaEnlazada<Materia> bloqueadas =
                new ListaEnlazada<>();

        ListaEnlazada<Materia> materias =
                grafo.obtenerMaterias();

        for (int i = 0;
             i < materias.tamanio();
             i++) {

            Materia materia =
                    materias.obtener(i);

            if (materia.isAprobada()) {

                continue;
            }

            ListaEnlazada<Materia> pendientes =
                    obtenerPrerequisitosPendientes(
                            materia.getCodigo());

            if (!pendientes.estaVacia()) {

                bloqueadas.agregar(
                        materia);
            }
        }

        return bloqueadas;
    }


    /// CONSULTAS DEL GRAFO
    
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


    /// MÉTRICAS DEL PENSUM
    
    public int obtenerTotalMaterias() {

        return listarMaterias()
                .tamanio();
    }


    public int obtenerTotalUV() {

        int total =
                0;

        ListaEnlazada<Materia> materias =
                listarMaterias();

        for (int i = 0;
             i < materias.tamanio();
             i++) {

            total +=
                    materias
                            .obtener(i)
                            .getCreditos();
        }

        return total;
    }


    public boolean validarPensumOficialICC() {

        return validarPensum()
                && obtenerTotalMaterias()
                == TOTAL_MATERIAS_PENSUM
                && obtenerTotalUV()
                == TOTAL_UV_PENSUM;
    }


    /// MÉTRICAS DE PROGRESO ACADÉMICO
    
    public int obtenerTotalMateriasAprobadas() {

        int total =
                0;

        ListaEnlazada<Materia> materias =
                listarMaterias();

        for (int i = 0;
             i < materias.tamanio();
             i++) {

            if (materias
                    .obtener(i)
                    .isAprobada()) {

                total++;
            }
        }

        return total;
    }


    public int obtenerUVAprobadas() {

        int total =
                0;

        ListaEnlazada<Materia> materias =
                listarMaterias();

        for (int i = 0;
             i < materias.tamanio();
             i++) {

            Materia materia =
                    materias.obtener(i);

            if (materia.isAprobada()) {

                total +=
                        materia.getCreditos();
            }
        }

        return total;
    }


    public double obtenerPorcentajeAvanceMaterias() {

        if (TOTAL_MATERIAS_PENSUM == 0) {

            return 0.0;
        }

        return (
                obtenerTotalMateriasAprobadas()
                        * 100.0)
                / TOTAL_MATERIAS_PENSUM;
    }


    public double obtenerPorcentajeAvanceUV() {

        if (TOTAL_UV_PENSUM == 0) {

            return 0.0;
        }

        return (
                obtenerUVAprobadas()
                        * 100.0)
                / TOTAL_UV_PENSUM;
    }


    /// SIGUIENTE CICLO ACADÉMICO
    
    public int obtenerSiguienteCicloAcademico() {

        ListaEnlazada<Materia> materias =
                listarMaterias();

        int siguienteCiclo =
                Integer.MAX_VALUE;

        for (int i = 0;
             i < materias.tamanio();
             i++) {

            Materia materia =
                    materias.obtener(i);

            if (!materia.isAprobada()
                    && materia.getCiclo()
                    < siguienteCiclo) {

                siguienteCiclo =
                        materia.getCiclo();
            }
        }

        if (siguienteCiclo
                == Integer.MAX_VALUE) {

            return 0;
        }

        return siguienteCiclo;
    }


    /// SIMULADOR DEL PRÓXIMO CICLO
    
    public ListaEnlazada<Materia>
    simularProximoCiclo() {

        ListaEnlazada<Materia> resultado =
                new ListaEnlazada<>();

        int siguienteCiclo =
                obtenerSiguienteCicloAcademico();

        if (siguienteCiclo == 0) {

            return resultado;
        }

        ListaEnlazada<Materia> materias =
                listarMaterias();

        for (int i = 0;
             i < materias.tamanio();
             i++) {

            Materia materia =
                    materias.obtener(i);

            if (materia.getCiclo()
                    != siguienteCiclo) {

                continue;
            }

            if (materia.isAprobada()) {

                continue;
            }

            ListaEnlazada<Materia> requisitos =
                    obtenerPrerequisitos(
                            materia.getCodigo());

            boolean habilitada =
                    true;

            for (int j = 0;
                 j < requisitos.tamanio();
                 j++) {

                Materia requisito =
                        requisitos.obtener(j);

                if (!requisito.isAprobada()) {

                    habilitada =
                            false;

                    break;
                }
            }

            if (habilitada) {

                resultado.agregar(
                        materia);
            }
        }

        return resultado;
    }


    /// RESUMEN DEL PROGRESO
    
    public String obtenerResumenProgreso() {

        StringBuilder resumen =
                new StringBuilder();

        resumen.append(
                "===== PROGRESO ACADÉMICO =====\n");

        resumen.append(
                "Materias aprobadas: ");

        resumen.append(
                obtenerTotalMateriasAprobadas());

        resumen.append(
                " / ");

        resumen.append(
                TOTAL_MATERIAS_PENSUM);

        resumen.append(
                "\n");

        resumen.append(
                "UV aprobadas: ");

        resumen.append(
                obtenerUVAprobadas());

        resumen.append(
                " / ");

        resumen.append(
                TOTAL_UV_PENSUM);

        resumen.append(
                "\n");

        resumen.append(
                String.format(
                        "Avance por materias: %.2f%%\n",
                        obtenerPorcentajeAvanceMaterias()));

        resumen.append(
                String.format(
                        "Avance por UV: %.2f%%\n",
                        obtenerPorcentajeAvanceUV()));

        int siguienteCiclo =
                obtenerSiguienteCicloAcademico();

        if (siguienteCiclo == 0) {

            resumen.append(
                    "Estado: PLAN DE ESTUDIOS COMPLETADO\n");

        } else {

            resumen.append(
                    "Siguiente ciclo pendiente: ");

            resumen.append(
                    siguienteCiclo);

            resumen.append(
                    "\n");
        }

        return resumen.toString();
    }


    /// CARGA DEL PENSUM REAL ICC 2024-2027
    
    public void cargarPensumICC2024() {

        if (!listarMaterias()
                .estaVacia()) {

            return;
        }

        cargarMateriasPensumICC();

        cargarPrerequisitosPensumICC();

        cargarCatalogoElectivas();
    }


    /// CARGA DE LAS 40 MATERIAS
    
    private void cargarMateriasPensumICC() {

        /// CICLO I
        
        registrarMateriaPensum(
                "CAD501",
                "Cálculo Diferencial",
                4,
                1);

        registrarMateriaPensum(
                "QUG501",
                "Química General",
                4,
                1);

        registrarMateriaPensum(
                "ANF231",
                "Antropología Filosófica",
                3,
                1);

        registrarMateriaPensum(
                "PRE104",
                "Programación Estructurada",
                4,
                1);


        /// CICLO II
        
        registrarMateriaPensum(
                "ALG501",
                "Álgebra Vectorial y Matrices",
                4,
                2);

        registrarMateriaPensum(
                "CAI501",
                "Cálculo Integral",
                4,
                2);

        registrarMateriaPensum(
                "MDB104",
                "Modelamiento y Diseño de Base de Datos",
                4,
                2);

        registrarMateriaPensum(
                "POO104",
                "Programación Orientada a Objetos",
                4,
                2);


        /// CICLO III
        
        registrarMateriaPensum(
                "CVV501",
                "Cálculo de Varias Variables",
                4,
                3);

        registrarMateriaPensum(
                "CDP501",
                "Cinemática y Dinámica de Partículas",
                4,
                3);

        registrarMateriaPensum(
                "ADS104",
                "Análisis y Diseño de Sistemas Informáticos",
                4,
                3);

        registrarMateriaPensum(
                "PRD104",
                "Programación con Estructuras de Datos",
                4,
                3);


        /// CICLO IV
        
        registrarMateriaPensum(
                "EDI501",
                "Ecuaciones Diferenciales",
                4,
                4);

        registrarMateriaPensum(
                "EYM501",
                "Electricidad y Magnetismo",
                4,
                4);

        registrarMateriaPensum(
                "DMD104",
                "Datawarehouse y Minería de Datos",
                4,
                4);

        registrarMateriaPensum(
                "ESA501",
                "Estadística Aplicada",
                4,
                4);


        /// CICLO V
        
        registrarMateriaPensum(
                "ACE102",
                "Análisis de Circuitos Eléctricos",
                4,
                5);

        registrarMateriaPensum(
                "GEA106",
                "Gestión Ambiental",
                4,
                5);

        registrarMateriaPensum(
                "AEE106",
                "Análisis y Evaluación Económica",
                4,
                5);

        registrarMateriaPensum(
                "OFC501",
                "Oscilaciones, Fluidos y Calor",
                4,
                5);


        /// CICLO VI
        
        registrarMateriaPensum(
                "DDP106",
                "Dirección de Proyectos",
                4,
                6);

        registrarMateriaPensum(
                "ACO101",
                "Arquitectura de Computadoras",
                4,
                6);

        registrarMateriaPensum(
                "LIC104",
                "Lenguajes Interpretados en el Cliente",
                4,
                6);

        registrarMateriaPensum(
                "DRD101",
                "Diseño de Redes de Datos",
                4,
                6);


        /// CICLO VII
        
        registrarMateriaPensum(
                "APM501",
                "Aplicación de Métodos Numéricos",
                4,
                7);

        registrarMateriaPensum(
                "SIO104",
                "Sistemas Operativos",
                4,
                7);

        registrarMateriaPensum(
                "LIS104",
                "Lenguajes Interpretados en el Servidor",
                4,
                7);

        registrarMateriaPensum(
                "IRD101",
                "Interconexión de Redes de Datos",
                4,
                7);


        /// CICLO VIII
        
        registrarMateriaPensum(
                "PSC231",
                "Pensamiento Social Cristiano",
                3,
                8);

        registrarMateriaPensum(
                "INS104",
                "Ingeniería de Software",
                5,
                8);

        registrarMateriaPensum(
                "DPS104",
                "Diseño y Programación de Software Multiplataforma",
                4,
                8);

        registrarMateriaPensum(
                "DSS101",
                "Diseño de Sistemas de Seguridad para Redes de Datos",
                4,
                8);


        /// CICLO IX
        
        registrarMateriaPensum(
                "NTI104",
                "Normalización de Tecnologías de la Información",
                4,
                9);

        registrarMateriaPensum(
                "CDS104",
                "Gestión de la Calidad del Software",
                5,
                9);

        registrarMateriaPensum(
                "DSM104",
                "Desarrollo de Software para Móviles",
                4,
                9);

        registrarMateriaPensum(
                "ASR104",
                "Administración e Implementación de Redes con Sistemas Operativos Propietarios",
                4,
                9);


        /// CICLO X
        
        registrarMateriaPensum(
                "AUS104",
                "Auditoría de Sistemas",
                4,
                10);

        registrarMateria(
                new Materia(
                        "ELEC38",
                        "Electiva",
                        4,
                        10,
                        "Electiva",
                        false));

        registrarMateriaPensum(
                "DES104",
                "Desarrollo de Software Empresarial",
                5,
                10);

        registrarMateriaPensum(
                "ASI104",
                "Administración e Implementación de Redes con Sistemas Operativos Open Source",
                4,
                10);
    }


    /// MÉTODO AUXILIAR PARA REGISTRAR MATERIAS
    
    private void registrarMateriaPensum(
            String codigo,
            String nombre,
            int uv,
            int ciclo) {

        registrarMateria(
                new Materia(
                        codigo,
                        nombre,
                        uv,
                        ciclo,
                        "ICC",
                        true));
    }


    /// CARGA DE PRERREQUISITOS REALES
    
    private void cargarPrerequisitosPensumICC() {

        // CICLO II

        agregarPrerequisito(
                "CAD501",
                "CAI501");

        agregarPrerequisito(
                "PRE104",
                "MDB104");

        agregarPrerequisito(
                "PRE104",
                "POO104");


        // CICLO III

        agregarPrerequisito(
                "CAI501",
                "CVV501");

        agregarPrerequisito(
                "CAD501",
                "CDP501");

        agregarPrerequisito(
                "MDB104",
                "ADS104");

        agregarPrerequisito(
                "POO104",
                "PRD104");


        // CICLO IV

        agregarPrerequisito(
                "CVV501",
                "EDI501");

        agregarPrerequisito(
                "CDP501",
                "EYM501");

        agregarPrerequisito(
                "MDB104",
                "DMD104");

        agregarPrerequisito(
                "CAI501",
                "ESA501");


        // CICLO V

        agregarPrerequisito(
                "EYM501",
                "ACE102");

        agregarPrerequisito(
                "QUG501",
                "GEA106");

        agregarPrerequisito(
                "ESA501",
                "AEE106");

        agregarPrerequisito(
                "CDP501",
                "OFC501");


        // CICLO VI

        agregarPrerequisito(
                "AEE106",
                "DDP106");

        agregarPrerequisito(
                "ACE102",
                "ACO101");

        agregarPrerequisito(
                "DMD104",
                "LIC104");


        // CICLO VII

        agregarPrerequisito(
                "EDI501",
                "APM501");

        agregarPrerequisito(
                "ACO101",
                "SIO104");

        agregarPrerequisito(
                "LIC104",
                "LIS104");

        agregarPrerequisito(
                "DRD101",
                "IRD101");


        // CICLO VIII

        agregarPrerequisito(
                "DDP106",
                "INS104");

        agregarPrerequisito(
                "LIS104",
                "DPS104");

        agregarPrerequisito(
                "IRD101",
                "DSS101");


        // CICLO IX

        agregarPrerequisito(
                "INS104",
                "NTI104");

        agregarPrerequisito(
                "INS104",
                "CDS104");

        agregarPrerequisito(
                "DPS104",
                "DSM104");

        agregarPrerequisito(
                "IRD101",
                "ASR104");


        // CICLO X

        agregarPrerequisito(
                "INS104",
                "AUS104");

        agregarPrerequisito(
                "DPS104",
                "DES104");

        agregarPrerequisito(
                "IRD101",
                "ASI104");

        agregarPrerequisito(
                "NTI104",
                "ELEC38");
    }


    /// CATÁLOGO DE ELECTIVAS
    
    private void cargarCatalogoElectivas() {

        if (!catalogoElectivas
                .estaVacia()) {

            return;
        }

        catalogoElectivas.agregar(
                new Materia(
                        "GEN102",
                        "Gestión Energética (ISO50001)",
                        4,
                        10,
                        "Electiva",
                        false));

        catalogoElectivas.agregar(
                new Materia(
                        "EAL102",
                        "Energía y Ambiente (LEED)",
                        4,
                        10,
                        "Electiva",
                        false));

        catalogoElectivas.agregar(
                new Materia(
                        "ALI103",
                        "Aplicación de Lubricantes en la Industria",
                        4,
                        10,
                        "Electiva",
                        false));

        catalogoElectivas.agregar(
                new Materia(
                        "ACD104",
                        "Aprendizaje Automático y Ciencia de Datos",
                        4,
                        10,
                        "Electiva",
                        false));

        catalogoElectivas.agregar(
                new Materia(
                        "IAR105",
                        "Inteligencia Artificial Aplicada a la Salud",
                        4,
                        10,
                        "Electiva",
                        false));

        catalogoElectivas.agregar(
                new Materia(
                        "SLI106",
                        "Sistemas Lean y Six Sigma",
                        4,
                        10,
                        "Electiva",
                        false));

        catalogoElectivas.agregar(
                new Materia(
                        "ROB109",
                        "Robótica",
                        4,
                        10,
                        "Electiva",
                        false));

        catalogoElectivas.agregar(
                new Materia(
                        "DVC110",
                        "Diseño de Sistemas de Visión por Computador",
                        4,
                        10,
                        "Electiva",
                        false));

        catalogoElectivas.agregar(
                new Materia(
                        "GET111",
                        "Gerencia de Empresas Tecnológicas",
                        4,
                        10,
                        "Electiva",
                        false));
    }


    /// CONSULTA DE ELECTIVAS
    
    public ListaEnlazada<Materia>
    listarElectivas() {

        return catalogoElectivas;
    }


    public Materia buscarElectiva(
            String codigo) {

        for (int i = 0;
             i < catalogoElectivas.tamanio();
             i++) {

            Materia electiva =
                    catalogoElectivas.obtener(i);

            if (electiva
                    .getCodigo()
                    .equalsIgnoreCase(
                            codigo)) {

                return electiva;
            }
        }

        return null;
    }


    /// SELECCIÓN DE ELECTIVA
    
    public void seleccionarElectiva(
            String codigo) {

        Materia electiva =
                buscarElectiva(
                        codigo);

        if (electiva == null) {

            throw new IllegalArgumentException(
                    "La electiva seleccionada no existe.");
        }

        electivaSeleccionada =
                electiva;
    }


    public Materia obtenerElectivaSeleccionada() {

        return electivaSeleccionada;
    }


    public boolean puedeCursarElectivaSeleccionada() {

        if (electivaSeleccionada == null) {

            return false;
        }

        Materia requisito =
                buscarMateria(
                        "NTI104");

        return requisito != null
                && requisito.isAprobada();
    }


    public void limpiarElectivaSeleccionada() {

        electivaSeleccionada =
                null;
    }


    /// COMPATIBILIDAD CON ETAPA II
    
    public void cargarDatosEjemplo() {

        cargarPensumICC2024();
    }
}
