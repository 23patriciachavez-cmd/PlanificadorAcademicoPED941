package sv.edu.udb.ped941.estructuras;

public class TablaHash<K, V> {

    private static class Entrada<K, V> {

        K clave;
        V valor;
        Entrada<K, V> siguiente;

        Entrada(
                K clave,
                V valor,
                Entrada<K, V> siguiente) {

            this.clave =
                    clave;

            this.valor =
                    valor;

            this.siguiente =
                    siguiente;
        }
    }

    private final Entrada<K, V>[] buckets;

    private int tamanio;

    @SuppressWarnings("unchecked")
    public TablaHash(
            int capacidad) {

        if (capacidad <= 0) {

            throw new IllegalArgumentException(
                    "La capacidad debe ser "
                    + "mayor que cero.");
        }

        buckets =
                (Entrada<K, V>[])
                new Entrada[capacidad];
    }

    private int indice(
            K clave) {

        if (clave == null) {

            throw new IllegalArgumentException(
                    "La clave no puede ser nula.");
        }

        return Math.floorMod(
                clave.hashCode(),
                buckets.length);
    }

    public boolean poner(
            K clave,
            V valor) {

        int posicion =
                indice(clave);

        Entrada<K, V> actual =
                buckets[posicion];

        while (actual != null) {

            if (actual.clave
                    .equals(clave)) {

                return false;
            }

            actual =
                    actual.siguiente;
        }

        buckets[posicion] =
                new Entrada<>(
                        clave,
                        valor,
                        buckets[posicion]);

        tamanio++;

        return true;
    }

    public boolean actualizar(
            K clave,
            V valor) {

        int posicion =
                indice(clave);

        Entrada<K, V> actual =
                buckets[posicion];

        while (actual != null) {

            if (actual.clave
                    .equals(clave)) {

                actual.valor =
                        valor;

                return true;
            }

            actual =
                    actual.siguiente;
        }

        return false;
    }

    public V obtener(
            K clave) {

        int posicion =
                indice(clave);

        Entrada<K, V> actual =
                buckets[posicion];

        while (actual != null) {

            if (actual.clave
                    .equals(clave)) {

                return actual.valor;
            }

            actual =
                    actual.siguiente;
        }

        return null;
    }

    public boolean contieneClave(
            K clave) {

        return obtener(clave)
                != null;
    }

    public boolean eliminar(
            K clave) {

        int posicion =
                indice(clave);

        Entrada<K, V> actual =
                buckets[posicion];

        Entrada<K, V> anterior =
                null;

        while (actual != null) {

            if (actual.clave
                    .equals(clave)) {

                if (anterior == null) {

                    buckets[posicion] =
                            actual.siguiente;

                } else {

                    anterior.siguiente =
                            actual.siguiente;
                }

                tamanio--;

                return true;
            }

            anterior =
                    actual;

            actual =
                    actual.siguiente;
        }

        return false;
    }

    public ListaEnlazada<K> claves() {

        ListaEnlazada<K> lista =
                new ListaEnlazada<>();

        for (Entrada<K, V> bucket
                : buckets) {

            Entrada<K, V> actual =
                    bucket;

            while (actual != null) {

                lista.agregar(
                        actual.clave);

                actual =
                        actual.siguiente;
            }
        }

        return lista;
    }

    public ListaEnlazada<V> valores() {

        ListaEnlazada<V> lista =
                new ListaEnlazada<>();

        for (Entrada<K, V> bucket
                : buckets) {

            Entrada<K, V> actual =
                    bucket;

            while (actual != null) {

                lista.agregar(
                        actual.valor);

                actual =
                        actual.siguiente;
            }
        }

        return lista;
    }

    public int tamanio() {

        return tamanio;
    }
}
