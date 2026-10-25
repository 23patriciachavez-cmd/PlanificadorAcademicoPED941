# Planificador Académico con Prerrequisitos
Proyecto desarrollado para la asignatura **Programación con Estructuras de Datos – PED941** de la Universidad Don Bosco.

## Descripción
El proyecto consiste en un sistema de planificación académica que representa una malla curricular mediante un **grafo dirigido acíclico (DAG)**.

En el modelo:
- Cada materia se representa como un nodo.
- Cada relación de prerrequisito se representa mediante una arista dirigida.
- El sistema permite determinar qué materias están disponibles para un estudiante.
- Se identifican prerrequisitos pendientes.
- Se detectan relaciones académicas inválidas.
- Se genera un orden académico válido mediante ordenamiento topológico.

La segunda etapa del proyecto se concentra en demostrar un avance funcional estable, integrando estructuras de datos, reglas de negocio, autenticación, pruebas unitarias y control de versiones.

---

## Objetivo
Desarrollar un sistema funcional que permita administrar y consultar relaciones académicas entre materias utilizando estructuras de datos apropiadas y algoritmos sobre grafos.

---

## Roles del sistema

### Estudiante
El estudiante puede:
- Iniciar sesión.
- Consultar el pensum.
- Listar materias.
- Buscar una materia por código.
- Registrar materias aprobadas.
- Consultar materias disponibles.
- Consultar materias bloqueadas.
- Consultar prerrequisitos pendientes.
- Generar un orden académico válido.

### Administrador
El administrador puede:
- Iniciar sesión.
- Consultar el pensum.
- Listar materias.
- Buscar materias.
- Registrar nuevas materias.
- Modificar materias.
- Agregar relaciones de prerrequisito.
- Eliminar relaciones de prerrequisito.
- Visualizar el grafo académico.
- Validar el pensum.
- Generar el orden académico.

---

## Estructuras de datos implementadas

### Lista enlazada
Se utiliza para almacenar colecciones dinámicas de elementos, como materias, prerrequisitos y resultados de consultas.

### Cola
Se utiliza en el algoritmo de ordenamiento topológico de Kahn.

La cola respeta el principio FIFO:
**First In, First Out.**

### Pila
Se utiliza como apoyo durante recorridos del grafo y para el control de rutas durante la detección de ciclos.

La pila respeta el principio LIFO:
**Last In, First Out.**

### Tabla Hash
Permite almacenar y localizar información mediante claves, principalmente códigos de materias y nombres de usuario.

La implementación utiliza manejo de colisiones mediante encadenamiento.

### Grafo dirigido
Es la estructura principal del sistema.

Cada materia es un vértice y cada prerrequisito constituye una arista dirigida.

### Árbol
Se utiliza para representar jerárquicamente la organización del plan de estudios por años y ciclos académicos.

---

## Algoritmos principales

### Ordenamiento topológico de Kahn
Se utiliza para generar un orden académico válido respetando las relaciones de prerrequisito.

El algoritmo:
1. Calcula el grado de entrada de cada materia.
2. Agrega a una cola las materias sin prerrequisitos pendientes.
3. Extrae materias progresivamente.
4. Reduce los grados de entrada de sus materias dependientes.
5. Incorpora nuevas materias a la cola cuando su grado llega a cero.
6. Genera un orden topológico válido.

### Detección de ciclos mediante DFS
El sistema utiliza un recorrido en profundidad para verificar que una nueva relación de prerrequisito no genere una dependencia circular.

De esta manera, el grafo se mantiene como un DAG.

---

## Reglas principales del sistema
El sistema controla, entre otras, las siguientes situaciones:
- No se permiten códigos de materia duplicados.
- Una materia no puede ser prerrequisito de sí misma.
- No se permiten relaciones de prerrequisito duplicadas.
- Las materias relacionadas deben existir.
- No se permiten dependencias circulares.
- Una materia solo se considera disponible si todos sus prerrequisitos han sido aprobados.
- Las materias aprobadas no aparecen nuevamente como disponibles.

---

## Tecnologías utilizadas
- Java 17
- Apache NetBeans
- Maven
- JUnit 5
- Git
- GitHub
- GitHub Desktop

---

## Estructura del proyecto

```text
PlanificadorAcademicoPED941
│
├── src
│   │
│   ├── main
│   │   └── java
│   │       └── sv.edu.udb.ped941
│   │           ├── app
│   │           ├── arbol
│   │           ├── estructuras
│   │           ├── grafo
│   │           ├── modelo
│   │           └── servicio
│   │
│   └── test
│       └── java
│           └── sv.edu.udb.ped941
│               ├── AutenticacionServiceTest.java
│               ├── EstructurasTest.java
│               └── PlanificadorAcademicoTest.java
│
├── docs
│   ├── bitacora.md
│   └── casos-prueba.md
│
├── .gitignore
├── pom.xml
└── README.md
```

---

## Credenciales de demostración

### Administrador
Usuario:
```text
admin
```

Contraseña:
```text
Admin123
```

### Estudiante
Usuario:
```text
estudiante
```

Contraseña:
```text
Est123
```

Estas credenciales se utilizan únicamente con fines académicos y de demostración.

---

## Cómo ejecutar el proyecto

### Requisitos
Es necesario tener instalado:
- JDK 17 o superior.
- Apache NetBeans.
- Maven.
- Git, si se desea trabajar con control de versiones.

### Ejecución desde NetBeans
1. Abrir Apache NetBeans.
2. Seleccionar **File → Open Project**.
3. Abrir la carpeta `PlanificadorAcademicoPED941`.
4. Esperar a que Maven cargue las dependencias.
5. Hacer clic derecho sobre el proyecto.
6. Seleccionar **Clean and Build**.
7. Verificar que aparezca `BUILD SUCCESS`.
8. Seleccionar **Run Project**.

La clase principal es:

```text
sv.edu.udb.ped941.app.Main
```

---

## Cómo ejecutar las pruebas

Las pruebas automatizadas se encuentran en `Test Packages`.

Para ejecutar toda la suite:
1. Hacer clic derecho sobre el proyecto.
2. Seleccionar **Test**.
3. Maven ejecutará las pruebas mediante JUnit 5.

La suite contiene:
- 4 pruebas de autenticación.
- 4 pruebas de estructuras de datos.
- 10 pruebas del planificador académico.

### Resultado actual

```text
Tests run: 18
Failures: 0
Errors: 0
Skipped: 0

BUILD SUCCESS
```

---

## Clases de prueba

### AutenticacionServiceTest
Valida:
- Login correcto del administrador.
- Login correcto del estudiante.
- Contraseña incorrecta.
- Usuario inexistente.

### EstructurasTest
Valida:
- Inserción y eliminación en lista enlazada.
- Comportamiento FIFO de la cola.
- Comportamiento LIFO de la pila.
- Inserción y búsqueda en tabla hash.

### PlanificadorAcademicoTest
Valida:
- Búsqueda de materias.
- Materias inexistentes.
- Códigos duplicados.
- Materias disponibles.
- Prerrequisitos pendientes.
- Autorreferencias.
- Relaciones duplicadas.
- Dependencias circulares.
- Ordenamiento topológico.

---

## Casos de prueba documentados

Los casos de prueba se encuentran en:

```text
docs/casos-prueba.md
```

Actualmente se encuentran documentados 18 casos de prueba correspondientes a la suite automatizada.

---

## Bitácora de dificultades

La bitácora se encuentra en:

```text
docs/bitacora.md
```

En ella se documentan dificultades encontradas durante el desarrollo, incluyendo:
- Organización inicial del proyecto.
- Uso de Git y GitHub.
- Configuración del paquete de pruebas.
- Implementación de estructuras propias.
- Control de dependencias circulares.
- Definición del alcance de la segunda etapa.
- Validación integral mediante pruebas unitarias.

Cada registro describe el problema, causa, solución, decisión técnica y aprendizaje obtenido.

---

## Control de versiones

El proyecto utiliza Git para el control de versiones y GitHub como repositorio remoto.

Se utilizan commits descriptivos, por ejemplo:

```text
chore: crear estructura inicial del proyecto
feat: agregar modelo inicial y ajustar gitignore
feat: implementar usuarios, login y estructuras lineales
feat: implementar tabla hash
feat: implementar grafo academico
test: agregar pruebas de autenticacion
test: agregar pruebas del planificador academico
test: agregar pruebas de estructuras de datos
docs: crear README del proyecto
docs: agregar casos de prueba y bitacora
```

La rama utilizada para el desarrollo actual es:

```text
develop
```

---

## Estado actual del proyecto

**Etapa II – Avance funcional**

El proyecto actualmente:
- Compila y ejecuta correctamente.
- Implementa las estructuras de datos definidas para esta etapa.
- Permite autenticación por roles.
- Gestiona relaciones de prerrequisitos.
- Detecta dependencias circulares.
- Genera un orden académico mediante Kahn.
- Incluye una suite de 18 pruebas automatizadas.
- Cuenta con documentación de casos de prueba.
- Cuenta con bitácora de dificultades.
- Utiliza Git y GitHub para control de versiones.

---

## Mejoras previstas para la Etapa III

Para la siguiente etapa se propone:
- Incorporar el pensum completo de Ingeniería en Ciencias de la Computación.
- Utilizar las materias y prerrequisitos reales del plan de estudios.
- Desarrollar una interfaz web.
- Migrar la aplicación a una arquitectura web basada en Java.
- Mejorar la experiencia visual del estudiante y administrador.
- Incorporar persistencia de información mediante una base de datos.
- Mantener y ampliar la suite de pruebas automatizadas.
- Realizar análisis de complejidad Big-O de las principales operaciones.

---

## Conclusión
La segunda etapa permitió transformar el diseño conceptual del Planificador Académico en un prototipo funcional que integra varias estructuras de datos dentro de un mismo sistema.

Las pruebas automatizadas confirman que las funcionalidades implementadas se comportan correctamente en los casos evaluados, mientras que el uso de Git y GitHub permite mantener evidencia de la evolución del desarrollo.

El proyecto queda preparado para continuar su evolución durante la tercera etapa, donde se incorporarán funcionalidades adicionales y una interfaz orientada al producto final.
