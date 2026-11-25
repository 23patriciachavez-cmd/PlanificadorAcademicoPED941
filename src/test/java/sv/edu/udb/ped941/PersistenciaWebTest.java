package sv.edu.udb.ped941;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import sv.edu.udb.ped941.persistencia.ProgresoRepository;
import sv.edu.udb.ped941.web.PlanificadorWebService;

import static org.junit.jupiter.api.Assertions.*;

class PersistenciaWebTest {

    @TempDir
    Path carpetaTemporal;

    private String url;

    private JdbcTemplate jdbc;

    private PlanificadorWebService servicio;

    /// CONFIGURACION DE BASE DE DATOS DE PRUEBA
    
    @BeforeEach
    void prepararBaseDeDatos() {

        String ruta = carpetaTemporal
                .resolve("planificador_test")
                .toAbsolutePath()
                .toString()
                .replace('\\', '/');

        url = "jdbc:h2:file:" + ruta;

        DriverManagerDataSource datasource =
                crearDataSource();

        ResourceDatabasePopulator inicializador =
                new ResourceDatabasePopulator(
                        new ClassPathResource("schema.sql"));

        inicializador.execute(datasource);

        jdbc = new JdbcTemplate(datasource);

        servicio = crearServicio();
    }

    private DriverManagerDataSource crearDataSource() {

        DriverManagerDataSource datasource =
                new DriverManagerDataSource();

        datasource.setDriverClassName("org.h2.Driver");

        datasource.setUrl(url);

        datasource.setUsername("sa");

        datasource.setPassword("");

        return datasource;
    }

    private PlanificadorWebService crearServicio() {

        JdbcTemplate nuevaConexion =
                new JdbcTemplate(crearDataSource());

        ProgresoRepository repositorio =
                new ProgresoRepository(nuevaConexion);

        PlanificadorWebService nuevo =
                new PlanificadorWebService(
                        repositorio,
                        nuevaConexion);

        nuevo.recuperarPersistencia();

        return nuevo;
    }

    /// PRUEBA 1: PERSISTENCIA DEL PROGRESO
    
    @Test
    void progresoAcademicoPersisteEnBaseDeDatos() {

        servicio.alternarMateria("CAD501");

        servicio.alternarMateria("QUG501");

        PlanificadorWebService recuperado =
                crearServicio();

        assertTrue(
                recuperado.buscarMateria("CAD501")
                        .isAprobada());

        assertTrue(
                recuperado.buscarMateria("QUG501")
                        .isAprobada());

        assertEquals(
                2,
                recuperado.materiasAprobadas());

        assertEquals(
                8,
                recuperado.uvAprobadas());
    }

    /// PRUEBA 2: PERSISTENCIA DE ELECTIVA
    
    @Test
    void electivaSeleccionadaPersiste() {

        servicio.seleccionarElectiva("ACD104");

        PlanificadorWebService recuperado =
                crearServicio();

        assertNotNull(
                recuperado.electivaSeleccionada());

        assertEquals(
                "ACD104",
                recuperado.electivaSeleccionada()
                        .getCodigo());

        assertEquals(
                40,
                recuperado.totalMaterias());

        assertEquals(
                161,
                recuperado.totalUV());
    }

    /// PRUEBA 3: MATERIAS DEPENDIENTES - RF12
    
    @Test
    void consultarMateriasDependientes() {

        var dependientes =
                servicio.materiasDependientesDe(
                        "PRE104");

        assertTrue(
                dependientes.stream()
                        .anyMatch(m ->
                                m.getCodigo()
                                        .equals("POO104")));

        assertTrue(
                dependientes.stream()
                        .anyMatch(m ->
                                m.getCodigo()
                                        .equals("MDB104")));
    }

    /// PRUEBA 4: CADENA COMPLETA - RF13
    
    @Test
    void consultarCadenaCompletaDePrerequisitos() {

        var cadena =
                servicio.cadenaCompletaPrerequisitos(
                        "DES104");

        var codigos = cadena.stream()
                .map(m -> m.getCodigo())
                .toList();

        assertEquals(
                6,
                codigos.size());

        assertEquals("PRE104", codigos.get(0));

        assertEquals("MDB104", codigos.get(1));

        assertEquals("DMD104", codigos.get(2));

        assertEquals("LIC104", codigos.get(3));

        assertEquals("LIS104", codigos.get(4));

        assertEquals("DPS104", codigos.get(5));
    }

    /// PRUEBA 5: RECHAZO DE DEPENDENCIAS CIRCULARES
    
    @Test
    void dependenciaCircularNoSeGuardaEnBaseDeDatos() {

        assertThrows(
                IllegalArgumentException.class,
                () -> servicio.agregarPrerequisito(
                        "DES104",
                        "PRE104"));

        assertTrue(
                servicio.grafoSinCiclos());

        assertTrue(
                servicio.pensumValido());

        Integer cambios = jdbc.queryForObject(
                "SELECT COUNT(*) FROM cambios_pensum",
                Integer.class);

        assertEquals(
                0,
                cambios.intValue());

        PlanificadorWebService recuperado =
                crearServicio();

        assertTrue(
                recuperado.grafoSinCiclos());
    }

    /// PRUEBA 6: PERSISTENCIA DE CAMBIOS ADMINISTRATIVOS
    
    @Test
    void relacionAdministrativaPersiste() {

        // Relacion adicional valida utilizada
        // exclusivamente en la base de datos de prueba.

        servicio.agregarPrerequisito(
                "CAD501",
                "POO104");

        PlanificadorWebService recuperado =
                crearServicio();

        assertTrue(
                recuperado.prerrequisitosDe("POO104")
                        .stream()
                        .anyMatch(m ->
                                m.getCodigo()
                                        .equals("CAD501")));

        assertTrue(
                recuperado.grafoSinCiclos());

        assertEquals(
                40,
                recuperado.totalMaterias());

        assertEquals(
                161,
                recuperado.totalUV());
    }
}
