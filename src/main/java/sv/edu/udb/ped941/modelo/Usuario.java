package sv.edu.udb.ped941.modelo;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public class Usuario {

    private final String nombreUsuario;
    private final String passwordHash;
    private final Rol rol;

    public Usuario(
            String nombreUsuario,
            String password,
            Rol rol) {

        if (nombreUsuario == null
                || nombreUsuario.isBlank()) {

            throw new IllegalArgumentException(
                    "El nombre de usuario es obligatorio.");
        }

        if (password == null
                || password.isBlank()) {

            throw new IllegalArgumentException(
                    "La contraseña es obligatoria.");
        }

        if (rol == null) {

            throw new IllegalArgumentException(
                    "El rol es obligatorio.");
        }

        this.nombreUsuario =
                nombreUsuario.trim().toLowerCase();

        this.passwordHash =
                generarHash(password);

        this.rol = rol;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public Rol getRol() {
        return rol;
    }

    public boolean validarPassword(
            String password) {

        if (password == null) {
            return false;
        }

        return passwordHash.equals(
                generarHash(password));
    }

    private static String generarHash(
            String texto) {

        try {

            MessageDigest digest =
                    MessageDigest.getInstance(
                            "SHA-256");

            byte[] hash =
                    digest.digest(
                            texto.getBytes(
                                    StandardCharsets.UTF_8));

            return HexFormat.of()
                    .formatHex(hash);

        } catch (NoSuchAlgorithmException e) {

            throw new IllegalStateException(
                    "No fue posible procesar "
                    + "la contraseña.",
                    e);
        }
    }

    @Override
    public String toString() {

        return nombreUsuario
                + " - "
                + rol;
    }
}
