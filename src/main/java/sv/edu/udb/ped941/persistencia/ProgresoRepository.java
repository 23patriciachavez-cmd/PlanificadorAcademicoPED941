package sv.edu.udb.ped941.persistencia;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ProgresoRepository {

    private final JdbcTemplate jdbcTemplate;


    public ProgresoRepository(
            JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate =
                jdbcTemplate;
    }


    /// MATERIAS APROBADAS
    
    public List<String>
    obtenerMateriasAprobadas() {

        String sql =
                """
                SELECT codigo_materia
                FROM progreso_estudiante
                WHERE aprobada = TRUE
                ORDER BY codigo_materia
                """;

        return jdbcTemplate.query(
                sql,
                (rs, fila) ->
                        rs.getString(
                                "codigo_materia"));
    }


    public void guardarEstadoMateria(
            String codigoMateria,
            boolean aprobada) {

        String sql =
                """
                MERGE INTO progreso_estudiante
                (
                    codigo_materia,
                    aprobada,
                    fecha_actualizacion
                )
                KEY (codigo_materia)
                VALUES
                (
                    ?,
                    ?,
                    CURRENT_TIMESTAMP
                )
                """;

        jdbcTemplate.update(
                sql,
                codigoMateria,
                aprobada);
    }


    public void limpiarProgreso() {

        jdbcTemplate.update(
                "DELETE FROM progreso_estudiante");
    }


    /// ELECTIVA
    
    public Optional<String>
    obtenerElectivaSeleccionada() {

        String sql =
                """
                SELECT codigo_electiva
                FROM seleccion_electiva
                WHERE id = 1
                """;

        List<String> resultados =
                jdbcTemplate.query(
                        sql,
                        (rs, fila) ->
                                rs.getString(
                                        "codigo_electiva"));

        if (resultados.isEmpty()) {

            return Optional.empty();
        }

        return Optional.ofNullable(
                resultados.get(0));
    }


    public void guardarElectivaSeleccionada(
            String codigo) {

        String sql =
                """
                MERGE INTO seleccion_electiva
                (
                    id,
                    codigo_electiva,
                    fecha_actualizacion
                )
                KEY (id)
                VALUES
                (
                    1,
                    ?,
                    CURRENT_TIMESTAMP
                )
                """;

        jdbcTemplate.update(
                sql,
                codigo);
    }


    public void limpiarElectiva() {

        jdbcTemplate.update(
                """
                DELETE FROM seleccion_electiva
                WHERE id = 1
                """);
    }
}
