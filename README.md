# Planificador Académico con Prerrequisitos

Proyecto de la asignatura Programación con Estructuras de Datos
PED941 - Universidad Don Bosco.

## Descripción

El sistema representa una malla curricular mediante un grafo
dirigido acíclico, donde las materias son nodos y las relaciones
de prerrequisito son aristas.

El sistema permite determinar qué materias puede cursar un
estudiante, consultar prerrequisitos pendientes y generar un
orden académico válido.

## Roles

### Estudiante
- Consultar pensum
- Buscar materias
- Registrar materias aprobadas
- Consultar materias disponibles
- Consultar materias bloqueadas
- Consultar prerrequisitos pendientes
- Generar orden académico

### Administrador
- Consultar pensum
- Registrar materias
- Modificar materias
- Agregar prerrequisitos
- Eliminar prerrequisitos
- Mostrar el grafo
- Validar el pensum
- Generar orden académico

## Estructuras de datos

- Lista enlazada
- Cola
- Pila
- Tabla Hash
- Grafo dirigido
- Árbol

## Algoritmos principales

- Ordenamiento topológico de Kahn
- DFS para detección de ciclos

## Tecnologías

- Java
- Maven
- JUnit 5
- Apache NetBeans
- Git
- GitHub

## Usuarios de demostración

Administrador:
- Usuario: admin
- Contraseña: Admin123

Estudiante:
- Usuario: estudiante
- Contraseña: Est123

## Ejecución

Ejecutar la clase:

sv.edu.udb.ped941.app.Main

## Pruebas

Ejecutar:

mvn test

## Estado

Etapa II - Avance funcional.