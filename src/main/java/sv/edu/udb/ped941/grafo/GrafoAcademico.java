package sv.edu.udb.ped941.grafo;

import sv.edu.udb.ped941.estructuras.Cola;
import sv.edu.udb.ped941.estructuras.ListaEnlazada;
import sv.edu.udb.ped941.estructuras.Pila;
import sv.edu.udb.ped941.estructuras.TablaHash;
import sv.edu.udb.ped941.modelo.Materia;

public class GrafoAcademico {

    private final TablaHash<String, Materia>
            materias;

    private final TablaHash<
            String,
            ListaEnlazada<String>>
            adyacencia;

    public GrafoAcademico(
            int capacidad) {

        materias =
                new TablaHash<>(
                        capacidad);

        adyacencia =
                new TablaHash<>(
                        capacidad);
    }

    public boolean agregarMateria(
            Materia materia) {

        if (!materias.poner(
                materia.getCodigo(),
                materia)) {

            return false;
        }

        adyacencia.poner(
                materia.getCodigo(),
                new ListaEnlazada<>());

        return true;
    }

    public Materia buscarMateria(
            String codigo) {

        return materias.obtener(
                normalizar(codigo));
    }

    public boolean existeMateria(
            String codigo) {

        return materias.contieneClave(
                normalizar(codigo));
    }

    public ListaEnlazada<Materia>
    obtenerMaterias() {

        return materias.valores();
    }

    public boolean existeRelacion(
            String prerequisito,
            String materia) {

        String pre =
                normalizar(
                        prerequisito);

        String mat =
                normalizar(
                        materia);

        ListaEnlazada<String> lista =
                adyacencia.obtener(pre);

        return lista != null
                && lista.contiene(mat);
    }

    public void agregarPrerequisito(
            String prerequisito,
            String materia) {

        String pre =
                normalizar(
                        prerequisito);

        String mat =
                normalizar(
                        materia);

        if (!existeMateria(pre)
                || !existeMateria(mat)) {

            throw new IllegalArgumentException(
                    "Una de las materias "
                    + "no existe.");
        }

        if (pre.equals(mat)) {

            throw new IllegalArgumentException(
                    "Una materia no puede "
                    + "ser prerrequisito "
                    + "de sí misma.");
        }

        if (existeRelacion(
                pre,
                mat)) {

            throw new IllegalArgumentException(
                    "La relación ya existe.");
        }

        adyacencia
                .obtener(pre)
                .agregar(mat);

        if (detectarCiclo()) {

            adyacencia
                    .obtener(pre)
                    .eliminar(mat);

            throw new IllegalArgumentException(
                    "No se puede agregar "
                    + "la relación porque "
                    + "generaría una "
                    + "dependencia circular.");
        }
    }

    public boolean eliminarPrerequisito(
            String prerequisito,
            String materia) {

        String pre =
                normalizar(
                        prerequisito);

        String mat =
                normalizar(
                        materia);

        ListaEnlazada<String> lista =
                adyacencia.obtener(pre);

        if (lista == null) {
            return false;
        }

        return lista.eliminar(mat);
    }

    public boolean eliminarMateria(
            String codigo) {

        String objetivo =
                normalizar(
                        codigo);

        if (!existeMateria(
                objetivo)) {

            return false;
        }

        ListaEnlazada<String> codigos =
                adyacencia.claves();

        for (int i = 0;
             i < codigos.tamanio();
             i++) {

            String codigoActual =
                    codigos.obtener(i);

            ListaEnlazada<String> lista =
                    adyacencia.obtener(
                            codigoActual);

            lista.eliminar(
                    objetivo);
        }

        adyacencia.eliminar(
                objetivo);

        materias.eliminar(
                objetivo);

        return true;
    }

    public ListaEnlazada<Materia>
    obtenerPrerequisitos(
            String codigoMateria) {

        String objetivo =
                normalizar(
                        codigoMateria);

        if (!existeMateria(
                objetivo)) {

            throw new IllegalArgumentException(
                    "La materia no existe.");
        }

        ListaEnlazada<Materia> resultado =
                new ListaEnlazada<>();

        ListaEnlazada<String> codigos =
                adyacencia.claves();

        for (int i = 0;
             i < codigos.tamanio();
             i++) {

            String origen =
                    codigos.obtener(i);

            ListaEnlazada<String> destinos =
                    adyacencia.obtener(
                            origen);

            if (destinos.contiene(
                    objetivo)) {

                resultado.agregar(
                        materias.obtener(
                                origen));
            }
        }

        return resultado;
    }

    public ListaEnlazada<Materia>
    obtenerDependientes(
            String codigo) {

        String origen =
                normalizar(
                        codigo);

        if (!existeMateria(
                origen)) {

            throw new IllegalArgumentException(
                    "La materia no existe.");
        }

        ListaEnlazada<Materia> resultado =
                new ListaEnlazada<>();

        ListaEnlazada<String> destinos =
                adyacencia.obtener(
                        origen);

        for (int i = 0;
             i < destinos.tamanio();
             i++) {

            resultado.agregar(
                    materias.obtener(
                            destinos.obtener(i)));
        }

        return resultado;
    }

    public boolean detectarCiclo() {

        int capacidad =
                Math.max(
                        17,
                        materias.tamanio()
                                * 2
                                + 1);

        TablaHash<String, Integer> estado =
                new TablaHash<>(
                        capacidad);

        ListaEnlazada<String> codigos =
                materias.claves();

        for (int i = 0;
             i < codigos.tamanio();
             i++) {

            estado.poner(
                    codigos.obtener(i),
                    0);
        }

        Pila<String> ruta =
                new Pila<>();

        for (int i = 0;
             i < codigos.tamanio();
             i++) {

            String codigo =
                    codigos.obtener(i);

            if (estado.obtener(
                    codigo) == 0
                    && dfsCiclo(
                            codigo,
                            estado,
                            ruta)) {

                return true;
            }
        }

        return false;
    }

    private boolean dfsCiclo(
            String actual,
            TablaHash<String, Integer> estado,
            Pila<String> ruta) {

        estado.actualizar(
                actual,
                1);

        ruta.push(
                actual);

        ListaEnlazada<String> vecinos =
                adyacencia.obtener(
                        actual);

        for (int i = 0;
             i < vecinos.tamanio();
             i++) {

            String vecino =
                    vecinos.obtener(i);

            int estadoVecino =
                    estado.obtener(
                            vecino);

            if (estadoVecino == 1) {

                return true;
            }

            if (estadoVecino == 0
                    && dfsCiclo(
                            vecino,
                            estado,
                            ruta)) {

                return true;
            }
        }

        ruta.pop();

        estado.actualizar(
                actual,
                2);

        return false;
    }

    public ListaEnlazada<Materia>
    generarOrdenTopologico() {

        int capacidad =
                Math.max(
                        17,
                        materias.tamanio()
                                * 2
                                + 1);

        TablaHash<String, Integer>
                gradoEntrada =
                new TablaHash<>(
                        capacidad);

        ListaEnlazada<String> codigos =
                materias.claves();

        for (int i = 0;
             i < codigos.tamanio();
             i++) {

            gradoEntrada.poner(
                    codigos.obtener(i),
                    0);
        }

        for (int i = 0;
             i < codigos.tamanio();
             i++) {

            String origen =
                    codigos.obtener(i);

            ListaEnlazada<String> vecinos =
                    adyacencia.obtener(
                            origen);

            for (int j = 0;
                 j < vecinos.tamanio();
                 j++) {

                String destino =
                        vecinos.obtener(j);

                int valor =
                        gradoEntrada.obtener(
                                destino);

                gradoEntrada.actualizar(
                        destino,
                        valor + 1);
            }
        }

        Cola<String> cola =
                new Cola<>();

        for (int i = 0;
             i < codigos.tamanio();
             i++) {

            String codigo =
                    codigos.obtener(i);

            if (gradoEntrada.obtener(
                    codigo) == 0) {

                cola.encolar(
                        codigo);
            }
        }

        ListaEnlazada<Materia> orden =
                new ListaEnlazada<>();

        while (!cola.estaVacia()) {

            String actual =
                    cola.desencolar();

            orden.agregar(
                    materias.obtener(
                            actual));

            ListaEnlazada<String> vecinos =
                    adyacencia.obtener(
                            actual);

            for (int i = 0;
                 i < vecinos.tamanio();
                 i++) {

                String vecino =
                        vecinos.obtener(i);

                int nuevoGrado =
                        gradoEntrada.obtener(
                                vecino)
                        - 1;

                gradoEntrada.actualizar(
                        vecino,
                        nuevoGrado);

                if (nuevoGrado == 0) {

                    cola.encolar(
                            vecino);
                }
            }
        }

        if (orden.tamanio()
                != materias.tamanio()) {

            throw new IllegalStateException(
                    "No se puede generar "
                    + "el orden académico: "
                    + "existen dependencias "
                    + "circulares.");
        }

        return orden;
    }

    public String mostrarGrafo() {

        StringBuilder texto =
                new StringBuilder();

        ListaEnlazada<String> codigos =
                adyacencia.claves();

        for (int i = 0;
             i < codigos.tamanio();
             i++) {

            String codigo =
                    codigos.obtener(i);

            texto.append(
                    codigo);

            texto.append(" -> ");

            texto.append(
                    adyacencia.obtener(
                            codigo));

            texto.append(
                    System.lineSeparator());
        }

        return texto.toString();
    }

    private String normalizar(
            String codigo) {

        if (codigo == null
                || codigo.isBlank()) {

            throw new IllegalArgumentException(
                    "Debe indicar "
                    + "un código válido.");
        }

        return codigo
                .trim()
                .toUpperCase();
    }
}
