
# Planificador Académico con Prerrequisitos

## Universidad Don Bosco

**Asignatura:** Programación con Estructuras de Datos (PED941)  
**Proyecto:** Proyecto de Cátedra  
**Etapa:** III - Entrega final  
**Carrera:** Ingeniería en Ciencias de la Computación  
**Plan de estudios:** ICC 2024-2027  
**Año:** 2026  

---

## 1. Descripción del proyecto

El Planificador Académico con Prerrequisitos es una aplicación desarrollada en Java que permite gestionar, consultar y analizar un plan de estudios universitario mediante estructuras de datos y algoritmos especializados.

El sistema utiliza como referencia el pensum de Ingeniería en Ciencias de la Computación de la Universidad Don Bosco, compuesto por 40 espacios curriculares, 161 unidades valorativas, 10 ciclos académicos y nueve opciones de asignaturas electivas.

La aplicación representa las relaciones de prerrequisitos mediante un grafo dirigido acíclico (DAG), permitiendo identificar dependencias académicas, detectar ciclos, determinar materias habilitadas y generar un orden académico válido.

La versión final incorpora una interfaz de consola, una aplicación web, autenticación por roles, persistencia mediante una base de datos H2 y herramientas de evaluación experimental del rendimiento.

---

## 2. Objetivo general

Desarrollar un sistema de planificación académica que permita gestionar y analizar las dependencias entre asignaturas universitarias mediante la implementación de estructuras de datos propias y algoritmos de recorrido y ordenamiento de grafos.

---

## 3. Funcionalidades principales

### Perfil estudiante

- Inicio y cierre de sesión.
- Consulta del pensum completo.
- Búsqueda de asignaturas por código.
- Registro de materias aprobadas.
- Corrección del historial académico.
- Consulta de materias disponibles.
- Consulta de materias bloqueadas.
- Identificación de prerrequisitos directos.
- Consulta de materias dependientes.
- Consulta de cadenas completas de prerrequisitos.
- Cálculo del porcentaje de avance por materias.
- Cálculo del porcentaje de avance por unidades valorativas.
- Simulación del próximo ciclo académico.
- Consulta del catálogo de nueve electivas.
- Selección de una asignatura electiva.
- Persistencia del progreso académico en H2.

### Perfil administrador

- Inicio y cierre de sesión.
- Registro de nuevas materias.
- Modificación de asignaturas existentes.
- Administración de relaciones de prerrequisitos.
- Rechazo de relaciones duplicadas.
- Prevención de autorreferencias.
- Detección de dependencias circulares.
- Validación estructural del pensum.
- Visualización del grafo académico.
- Visualización del árbol curricular.
- Generación del ordenamiento topológico.
- Consulta de indicadores del plan de estudios.
- Persistencia de modificaciones administrativas.

---

## 4. Tecnologías utilizadas

| Tecnología | Aplicación |
|---|---|
| Java | Lenguaje principal |
| Maven | Gestión de dependencias y compilación |
| Apache NetBeans | Entorno de desarrollo |
| Spring Boot 4.2.0 | Aplicación web |
| Spring Security | Autenticación y autorización |
| Spring JDBC | Acceso a base de datos |
| Thymeleaf | Generación de vistas HTML |
| HTML5 | Estructura de la interfaz |
| CSS3 | Diseño y presentación |
| JavaScript | Interactividad y filtrado |
| H2 Database | Persistencia de información |
| JUnit 5 | Pruebas automatizadas |
| Git | Control de versiones |
| GitHub | Repositorio colaborativo |

El proyecto utiliza compatibilidad de compilación con Java 17 y fue validado experimentalmente con JDK 25.0.3.

---

## 5. Arquitectura del sistema

El proyecto utiliza una arquitectura modular que separa la lógica académica, las estructuras de datos, la autenticación, la presentación y la persistencia.

```text
Interfaz de consola
         |
         |             Interfaz web
         |                   |
         |           Spring Security
         |                   |
         |              Controladores
         |                   |
         |             Servicio web
         |                   |
         +-------------------+
                             |
                  PlanificadorAcademico
                             |
        +---------+----------+----------+
        |         |          |          |
      Grafo     Árbol    Tabla hash    Listas
        |
     DFS y Kahn

Servicio web --> Spring JDBC --> H2 Database
```

La base de datos complementa las estructuras de datos propias y no sustituye la lógica del grafo académico.

---

## 6. Estructura del repositorio

```text
PlanificadorAcademicoPED941/
|
|-- docs/
|   `-- rendimiento.csv
|
|-- src/
|   |-- main/
|   |   |-- java/
|   |   |   `-- sv/edu/udb/ped941/
|   |   |       |-- WebApplication.java
|   |   |       |-- app/
|   |   |       |   |-- Main.java
|   |   |       |   `-- MedicionRendimiento.java
|   |   |       |-- arbol/
|   |   |       |-- estructuras/
|   |   |       |-- grafo/
|   |   |       |-- modelo/
|   |   |       |-- persistencia/
|   |   |       |-- seguridad/
|   |   |       |-- servicio/
|   |   |       `-- web/
|   |   |
|   |   `-- resources/
|   |       |-- application.properties
|   |       |-- schema.sql
|   |       |-- static/
|   |       |   `-- css/
|   |       |       `-- estilos.css
|   |       `-- templates/
|   |           |-- index.html
|   |           |-- admin.html
|   |           `-- login.html
|   |
|   `-- test/
|       `-- java/sv/edu/udb/ped941/
|           |-- AutenticacionServiceTest.java
|           |-- EstructurasTest.java
|           |-- PlanificadorAcademicoTest.java
|           `-- PersistenciaWebTest.java
|
|-- .gitignore
|-- pom.xml
`-- README.md
```

---

## 7. Estructuras de datos implementadas

| Estructura | Justificación |
|---|---|
| Lista enlazada | Almacenamiento dinámico de materias, relaciones y resultados académicos |
| Tabla hash | Búsqueda eficiente de materias y usuarios mediante claves |
| Cola | Procesamiento FIFO requerido por el algoritmo de Kahn |
| Pila | Soporte de recorridos y operaciones relacionadas con DFS |
| Grafo dirigido | Representación de asignaturas y relaciones de prerrequisitos |
| Árbol curricular | Organización jerárquica del plan de estudios |

Las estructuras fueron desarrolladas como componentes propios del proyecto para aplicar los conocimientos adquiridos durante la asignatura.

Las colecciones estándar de Java utilizadas en la capa web permiten adaptar los resultados para Thymeleaf, realizar ordenamientos visuales y facilitar la integración con los componentes de Spring.

---

## 8. Algoritmos implementados

### Búsqueda mediante tabla hash

Permite recuperar materias a partir de su código académico.

Complejidad esperada:

- Caso promedio: O(1), bajo condiciones adecuadas de distribución de claves.
- Peor caso: O(V), ante acumulación de colisiones.

### DFS - Depth First Search

Permite detectar dependencias circulares dentro del grafo académico.

Su complejidad teórica es O(V + E) cuando los vértices y las listas de adyacencia se recorren eficientemente.

### Algoritmo de Kahn

Genera un ordenamiento topológico mediante el procesamiento de vértices con grado de entrada cero.

Su complejidad teórica es O(V + E) bajo una implementación eficiente de las listas de adyacencia y la cola.

### Simulador académico

Identifica el ciclo curricular más bajo que posee materias pendientes y selecciona aquellas que cumplen sus prerrequisitos directos.

### Cadena completa de prerrequisitos

Realiza un recorrido de las dependencias anteriores de una asignatura, utilizando una estructura de seguimiento de vértices visitados para evitar duplicaciones.

### Consideraciones de complejidad

La complejidad teórica de los algoritmos debe distinguirse de la complejidad efectiva de su implementación.

El acceso repetido mediante índices sobre listas enlazadas puede producir recorridos cuadráticos. Esta característica representa una oportunidad de optimización futura mediante iteradores o recorridos directos por nodos.

---

## 9. Base de datos

El sistema utiliza H2 en modalidad persistente mediante archivos locales.

La estructura se inicializa automáticamente mediante:

```text
src/main/resources/schema.sql
```

### Tablas implementadas

| Tabla | Propósito |
|---|---|
| progreso_estudiante | Registro de asignaturas aprobadas |
| seleccion_electiva | Almacenamiento de la electiva seleccionada |
| cambios_pensum | Registro de modificaciones administrativas |

El archivo de datos se genera automáticamente en:

```text
data/planificador.mv.db
```

No es necesario instalar un servidor de base de datos independiente.

El archivo operativo de la base de datos no se incluye en GitHub. El esquema SQL permite reproducir su estructura.

---

## 10. Instalación y ejecución

### Requisitos

- JDK 17 o superior.
- Maven.
- NetBeans u otro entorno compatible con Java y Maven.
- Navegador web.
- Conexión a Internet para la descarga inicial de dependencias.

### Clonar el repositorio

```bash
git clone https://github.com/23patriciachavez-cmd/PlanificadorAcademicoPED941.git
```

Abrir la carpeta del proyecto.

### Compilar el proyecto

```bash
mvn clean package
```

### Ejecutar las pruebas

```bash
mvn test
```

### Ejecutar la aplicación web

```bash
mvn spring-boot:run
```

Abrir en el navegador:

http://127.0.0.1:8080/

El servidor debe permanecer activo mientras se utiliza la aplicación web.

### Ejecutar mediante NetBeans

1. Abrir el proyecto Maven.
2. Ejecutar Clean and Build.
3. Hacer clic derecho sobre el proyecto.
4. Seleccionar Run Maven.
5. Seleccionar Goals.
6. Introducir spring-boot:run.
7. Abrir la dirección local en el navegador.

### Ejecutar la versión de consola

Desde NetBeans, ejecutar la clase:

```text
sv.edu.udb.ped941.app.Main
```

También puede ejecutarse desde la terminal, después de compilar:

```bash
java -cp target/classes sv.edu.udb.ped941.app.Main
```

---

## 11. Usuarios de demostración

| Perfil | Usuario | Contraseña |
|---|---|---|
| Administrador | admin | Admin123 |
| Estudiante | estudiante | Est123 |

Estas credenciales corresponden exclusivamente al entorno académico de demostración local.

No deben utilizarse en ambientes de producción.

---

## 12. Pruebas automatizadas

La versión final cuenta con 41 pruebas automatizadas ejecutadas mediante JUnit y Maven Surefire.

| Clase de prueba | Pruebas superadas |
|---|---:|
| AutenticacionServiceTest | 4 |
| EstructurasTest | 4 |
| PlanificadorAcademicoTest | 27 |
| PersistenciaWebTest | 6 |
| Total | 41 |

Resultado registrado el 24 de noviembre de 2026:

```text
Tests run: 41
Failures: 0
Errors: 0
Skipped: 0

BUILD SUCCESS
```

Las pruebas incluyen:

- Autenticación válida e inválida.
- Funcionamiento de estructuras propias.
- Búsqueda de materias.
- Validación del pensum.
- Identificación de códigos duplicados.
- Detección de dependencias circulares.
- Ordenamiento topológico.
- Progreso académico.
- Simulación de ciclos.
- Selección de electivas.
- Persistencia del progreso.
- Persistencia de modificaciones administrativas.
- Consulta de cadenas de prerrequisitos.

Las pruebas de persistencia utilizan una base de datos temporal independiente.

---

## 13. Evaluación experimental del rendimiento

El proyecto incluye la clase:

```text
sv.edu.udb.ped941.app.MedicionRendimiento
```

Esta permite medir experimentalmente las principales operaciones del núcleo académico mediante System.nanoTime().

### Metodología

- Sistema operativo: Windows 11.
- Versión de Java: 25.0.3.
- Procesadores lógicos disponibles: 8.
- Tamaño del pensum: 40 materias.
- Unidades valorativas: 161.
- Operaciones evaluadas: 12.
- Calentamiento por operación: 100 ejecuciones.
- Repeticiones: 5.
- Iteraciones por repetición: 200.
- Ejecuciones medidas por operación: 1,000.
- Ejecuciones medidas totales: 12,000.

### Resultados

| Operación | Media (microsegundos) | Mediana (microsegundos) |
|---|---:|---:|
| Búsqueda existente | 0.804 | 0.768 |
| Búsqueda inexistente | 0.198 | 0.181 |
| Listado del pensum | 1.036 | 0.837 |
| Consulta de prerrequisitos | 3.589 | 3.024 |
| Materias disponibles | 55.563 | 55.784 |
| Materias bloqueadas | 42.898 | 41.170 |
| Detección de ciclos DFS | 7.337 | 7.993 |
| Ordenamiento topológico Kahn | 9.466 | 8.048 |
| Árbol curricular | 34.952 | 24.713 |
| Cálculo del progreso | 22.082 | 15.430 |
| Simulador del próximo ciclo | 9.634 | 7.641 |
| Búsqueda de electiva | 1.372 | 1.186 |

Los resultados originales se almacenan en:

```text
docs/rendimiento.csv
```

Todas las operaciones medidas presentaron tiempos medios inferiores a un milisegundo en el escenario evaluado.

Las mediciones corresponden al núcleo Java en memoria y no incluyen latencia HTTP, renderizado HTML ni operaciones de persistencia en H2.

Al utilizar un único tamaño de pensum, estos resultados no constituyen una demostración experimental de la complejidad asintótica.

---

## 14. Casos límite considerados

- Búsqueda de materias inexistentes.
- Registro de códigos duplicados.
- Prerrequisitos inexistentes o duplicados.
- Autorreferencias.
- Dependencias circulares.
- Estudiante sin materias aprobadas.
- Estudiante con pensum completado.
- Electiva inexistente.
- Ciclos sin materias habilitadas.
- Recuperación del progreso desde la base de datos.
- Rechazo de cambios administrativos inválidos.

Un caso de demostración consiste en intentar agregar DES104 como prerrequisito de PRE104. El sistema rechaza esta operación porque generaría una dependencia circular.

---

## 15. Evolución del proyecto

### Etapa I

- Identificación del problema.
- Definición de requerimientos.
- Diseño conceptual.
- Selección de estructuras de datos.
- Organización del equipo.

### Etapa II

- Implementación del núcleo Java.
- Desarrollo de estructuras propias.
- Implementación del grafo académico.
- Desarrollo de DFS y Kahn.
- Autenticación y roles mediante consola.
- Validación mediante 18 pruebas automatizadas.

### Etapa III

- Incorporación del pensum ICC completo.
- Ampliación de funcionalidades académicas.
- Simulador y cálculo de progreso.
- Catálogo de electivas.
- Desarrollo de la interfaz web.
- Implementación de Spring Security.
- Persistencia mediante H2.
- Panel administrativo.
- Ampliación a 41 pruebas automatizadas.
- Evaluación experimental del rendimiento.

---

## 16. Limitaciones y mejoras futuras

La aplicación constituye un prototipo académico funcional de ejecución local.

Entre las mejoras futuras se consideran:

- Administración de múltiples estudiantes.
- Registro y gestión dinámica de usuarios.
- Persistencia compartida entre la consola y la aplicación web.
- Gestión de contraseñas mediante algoritmos adaptativos como BCrypt.
- Publicación segura en un servidor externo.
- Integración con sistemas académicos institucionales.
- Incorporación de horarios y límites de inscripción.
- Simulación de escenarios académicos alternativos.
- Optimización de recorridos sobre listas enlazadas.
- Pruebas de rendimiento con diferentes tamaños de grafos.
- Pruebas automatizadas HTTP y de autorización por roles.

El sistema no sustituye los mecanismos oficiales de inscripción ni certificación académica de la universidad.

---

## 17. Repositorio

GitHub:

https://github.com/23patriciachavez-cmd/PlanificadorAcademicoPED941

---

## 18. Conclusión

El Planificador Académico con Prerrequisitos demuestra la aplicación práctica de estructuras de datos y algoritmos en la resolución de un problema universitario real.

La combinación de grafos dirigidos, tablas hash, listas enlazadas, colas, pilas y árboles permite representar las dependencias académicas y facilitar la planificación del estudiante.

La incorporación de una interfaz web, autenticación por roles, persistencia de información, pruebas automatizadas y mediciones de rendimiento amplía la propuesta inicial y fortalece su funcionalidad, mantenibilidad y capacidad de evolución.

---

**Universidad Don Bosco - PED941 - 2026**  
**Proyecto de Cátedra - Etapa III**
