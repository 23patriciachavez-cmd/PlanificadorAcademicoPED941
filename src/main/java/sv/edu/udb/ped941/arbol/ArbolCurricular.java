package sv.edu.udb.ped941.arbol;

import sv.edu.udb.ped941.estructuras.ListaEnlazada;
import sv.edu.udb.ped941.modelo.Materia;

public class ArbolCurricular {

    private NodoArbol raiz;

    public ArbolCurricular(
            String carrera) {

        raiz =
                new NodoArbol(
                        carrera);
    }

    public void reconstruir(
            ListaEnlazada<Materia> materias) {

        raiz =
                new NodoArbol(
                        "Plan de Estudios");

        int maxCiclo =
                0;

        for (int i = 0;
             i < materias.tamanio();
             i++) {

            maxCiclo =
                    Math.max(
                            maxCiclo,
                            materias
                                    .obtener(i)
                                    .getCiclo());
        }

        int maxAnio =
                (maxCiclo + 1)
                / 2;

        for (int anio = 1;
             anio <= maxAnio;
             anio++) {

            NodoArbol nodoAnio =
                    new NodoArbol(
                            "Año "
                            + anio);

            boolean tieneMaterias =
                    false;

            for (int ciclo = anio * 2 - 1;
                 ciclo <= anio * 2;
                 ciclo++) {

                NodoArbol nodoCiclo =
                        new NodoArbol(
                                "Ciclo "
                                + ciclo);

                boolean cicloTieneMaterias =
                        false;

                for (int i = 0;
                     i < materias.tamanio();
                     i++) {

                    Materia materia =
                            materias.obtener(i);

                    if (materia.getCiclo()
                            == ciclo) {

                        nodoCiclo.agregarHijo(
                                new NodoArbol(
                                        materia.getCodigo()
                                        + " - "
                                        + materia.getNombre()));

                        cicloTieneMaterias =
                                true;

                        tieneMaterias =
                                true;
                    }
                }

                if (cicloTieneMaterias) {

                    nodoAnio.agregarHijo(
                            nodoCiclo);
                }
            }

            if (tieneMaterias) {

                raiz.agregarHijo(
                        nodoAnio);
            }
        }
    }

    public String mostrarJerarquia() {

        StringBuilder resultado =
                new StringBuilder();

        recorrer(
                raiz,
                0,
                resultado);

        return resultado.toString();
    }

    private void recorrer(
            NodoArbol nodo,
            int nivel,
            StringBuilder resultado) {

        resultado
                .append(
                        "  ".repeat(
                                nivel))
                .append("- ")
                .append(
                        nodo.getNombre())
                .append(
                        System.lineSeparator());

        for (int i = 0;
             i < nodo
                     .getHijos()
                     .tamanio();
             i++) {

            recorrer(
                    nodo
                            .getHijos()
                            .obtener(i),
                    nivel + 1,
                    resultado);
        }
    }
}
