package sv.edu.udb.ped941.modelo;

import java.util.Objects;

public class Materia {

    private final String codigo;

    private String nombre;
    private int creditos;
    private int ciclo;
    private String area;
    private boolean obligatoria;
    private boolean aprobada;

    public Materia(
            String codigo,
            String nombre,
            int creditos,
            int ciclo,
            String area,
            boolean obligatoria) {

        if (codigo == null
                || codigo.isBlank()) {

            throw new IllegalArgumentException(
                    "El código de la materia "
                    + "es obligatorio.");
        }

        this.codigo =
                codigo.trim().toUpperCase();

        setNombre(nombre);
        setCreditos(creditos);
        setCiclo(ciclo);
        setArea(area);

        this.obligatoria =
                obligatoria;

        this.aprobada =
                false;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCreditos() {
        return creditos;
    }

    public int getCiclo() {
        return ciclo;
    }

    public String getArea() {
        return area;
    }

    public boolean isObligatoria() {
        return obligatoria;
    }

    public boolean isAprobada() {
        return aprobada;
    }

    public void setNombre(
            String nombre) {

        if (nombre == null
                || nombre.isBlank()) {

            throw new IllegalArgumentException(
                    "El nombre de la materia "
                    + "es obligatorio.");
        }

        this.nombre =
                nombre.trim();
    }

    public void setCreditos(
            int creditos) {

        if (creditos <= 0) {

            throw new IllegalArgumentException(
                    "Los créditos deben ser "
                    + "mayores que cero.");
        }

        this.creditos =
                creditos;
    }

    public void setCiclo(
            int ciclo) {

        if (ciclo <= 0) {

            throw new IllegalArgumentException(
                    "El ciclo debe ser "
                    + "mayor que cero.");
        }

        this.ciclo =
                ciclo;
    }

    public void setArea(
            String area) {

        this.area =
                area == null
                ? ""
                : area.trim();
    }

    public void setObligatoria(
            boolean obligatoria) {

        this.obligatoria =
                obligatoria;
    }

    public void setAprobada(
            boolean aprobada) {

        this.aprobada =
                aprobada;
    }

    @Override
    public String toString() {

        return codigo
                + " - "
                + nombre
                + " | Ciclo "
                + ciclo
                + (aprobada
                   ? " | APROBADA"
                   : "");
    }

    @Override
    public boolean equals(
            Object objeto) {

        if (this == objeto) {
            return true;
        }

        if (!(objeto instanceof Materia materia)) {
            return false;
        }

        return codigo.equals(
                materia.codigo);
    }

    @Override
    public int hashCode() {

        return Objects.hash(
                codigo);
    }
}
