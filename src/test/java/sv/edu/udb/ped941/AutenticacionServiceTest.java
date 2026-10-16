package sv.edu.udb.ped941;

import org.junit.jupiter.api.Test;

import sv.edu.udb.ped941.modelo.Rol;
import sv.edu.udb.ped941.modelo.Usuario;
import sv.edu.udb.ped941.servicio.AutenticacionService;

import static org.junit.jupiter.api.Assertions.*;

class AutenticacionServiceTest {

    @Test
    void iniciarSesionAdministrador() {

        AutenticacionService servicio =
                new AutenticacionService();

        Usuario usuario =
                servicio.iniciarSesion(
                        "admin",
                        "Admin123");

        assertNotNull(
                usuario);

        assertEquals(
                Rol.ADMINISTRADOR,
                usuario.getRol());
    }

    @Test
    void iniciarSesionEstudiante() {

        AutenticacionService servicio =
                new AutenticacionService();

        Usuario usuario =
                servicio.iniciarSesion(
                        "estudiante",
                        "Est123");

        assertNotNull(
                usuario);

        assertEquals(
                Rol.ESTUDIANTE,
                usuario.getRol());
    }

    @Test
    void rechazarPasswordIncorrecto() {

        AutenticacionService servicio =
                new AutenticacionService();

        Usuario usuario =
                servicio.iniciarSesion(
                        "admin",
                        "incorrecta");

        assertNull(
                usuario);
    }

    @Test
    void rechazarUsuarioInexistente() {

        AutenticacionService servicio =
                new AutenticacionService();

        assertNull(
                servicio.iniciarSesion(
                        "otro",
                        "123"));
    }
}
