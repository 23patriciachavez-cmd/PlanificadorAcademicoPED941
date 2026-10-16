package sv.edu.udb.ped941.servicio;

import sv.edu.udb.ped941.estructuras.TablaHash;
import sv.edu.udb.ped941.modelo.Rol;
import sv.edu.udb.ped941.modelo.Usuario;

public class AutenticacionService {

    private final TablaHash<String, Usuario>
            usuarios;

    public AutenticacionService() {

        usuarios =
                new TablaHash<>(17);

        cargarUsuariosIniciales();
    }

    private void cargarUsuariosIniciales() {

        registrarUsuario(
                new Usuario(
                        "admin",
                        "Admin123",
                        Rol.ADMINISTRADOR));

        registrarUsuario(
                new Usuario(
                        "estudiante",
                        "Est123",
                        Rol.ESTUDIANTE));
    }

    public boolean registrarUsuario(
            Usuario usuario) {

        return usuarios.poner(
                usuario
                        .getNombreUsuario(),
                usuario);
    }

    public Usuario iniciarSesion(
            String nombreUsuario,
            String password) {

        if (nombreUsuario == null
                || nombreUsuario.isBlank()) {

            return null;
        }

        String usuarioNormalizado =
                nombreUsuario
                        .trim()
                        .toLowerCase();

        Usuario usuario =
                usuarios.obtener(
                        usuarioNormalizado);

        if (usuario == null) {

            return null;
        }

        if (!usuario
                .validarPassword(
                        password)) {

            return null;
        }

        return usuario;
    }
}
