# Bitácora de dificultades

## Dificultad 1 - Organización del repositorio Git
Fecha:
15/10/2026

Problema:
Era necesario mantener un historial claro de los avances del proyecto y evitar que todo el desarrollo quedara registrado en un único cambio.

Causa:
Al inicio del proyecto no se tenía experiencia suficiente con el uso de commits, ramas y repositorios remotos.

Solución:
Se configuró Git como sistema de control de versiones, se creó un repositorio remoto en GitHub y se utilizó la rama develop para registrar progresivamente las funcionalidades implementadas.

Decisión:
Utilizar mensajes descriptivos en los commits para identificar claramente el propósito de cada modificación.

Aprendizaje:
El control de versiones permite conocer la evolución del proyecto, identificar cambios específicos y recuperar versiones anteriores cuando sea necesario.




## Dificultad 2 - Creación incorrecta del paquete de pruebas
Fecha:
15/10/2026

Problema:
Durante la configuración de las pruebas unitarias se creó inicialmente una clase denominada ped941.java en lugar del paquete sv.edu.udb.ped941 dentro de Test Packages.

Causa:
Se seleccionó la opción Java Class en lugar de Java Package en NetBeans.

Solución:
Se eliminó la clase creada incorrectamente y posteriormente se creó el paquete sv.edu.udb.ped941 dentro de Test Packages.

Decisión:
Verificar previamente el tipo de elemento que se necesita crear antes de agregar nuevas clases, paquetes o archivos al proyecto.

Aprendizaje:
La correcta organización de paquetes facilita la ejecución de pruebas y mantiene una estructura clara del proyecto.




## Dificultad 3 - Implementación de estructuras de datos propias
Fecha:
15/10/2026

Problema:
Se debía decidir si utilizar directamente las estructuras proporcionadas por Java o implementar estructuras propias.

Causa:
Aunque Java ofrece estructuras como ArrayList, Stack, Queue y HashMap, el objetivo académico del proyecto es demostrar la comprensión e implementación de las estructuras de datos estudiadas.

Solución:
Se desarrollaron implementaciones propias de ListaEnlazada, Cola, Pila y TablaHash.

Decisión:
Utilizar estas estructuras propias dentro del funcionamiento del planificador académico en lugar de depender únicamente de colecciones predefinidas.

Aprendizaje:
La implementación manual permitió comprender mejor el comportamiento interno de las estructuras, sus operaciones y las situaciones en las que cada una resulta más adecuada.




## Dificultad 4 - Protección del grafo frente a dependencias circulares
Fecha:
15/10/2026

Problema:
Al agregar nuevas relaciones de prerrequisitos existía la posibilidad de crear dependencias circulares entre las materias.

Causa:
Una relación ingresada sin validación puede provocar que el grafo deje de ser un grafo dirigido acíclico (DAG).

Solución:
Se incorporó una validación basada en recorrido DFS y una pila para detectar ciclos antes de aceptar definitivamente una nueva relación de prerrequisito.

Decisión:
Toda nueva relación debe ser validada antes de incorporarse de forma permanente al grafo académico.

Aprendizaje:
Las reglas de negocio deben ser controladas por el propio sistema para conservar la integridad de las estructuras de datos y no depender únicamente de que el usuario ingrese información correcta.




## Dificultad 5 - Definición del alcance de la segunda etapa
Fecha:
24/10/2026

Problema:
Antes de la entrega se evaluó ampliar el sistema incorporando el pensum completo de Ingeniería en Ciencias de la Computación y convertir el planificador en una aplicación web.

Causa:
Estas mejoras aportan valor al producto final, pero requieren modificaciones importantes y nuevas tecnologías a pocas horas de la entrega de la segunda etapa.

Solución:
Se decidió conservar para la siguiente etapa la versión funcional y estable desarrollada en Java, priorizando las pruebas unitarias, la documentación, GitHub y la demostración del funcionamiento.

Decisión:
La incorporación del pensum completo y el desarrollo de una interfaz web se realizarán en la tercera etapa del proyecto.

Aprendizaje:
En el desarrollo de software es necesario controlar el alcance y priorizar estabilidad, pruebas y cumplimiento de requisitos antes de agregar nuevas funcionalidades.




## Dificultad 6 - Validación integral mediante pruebas unitarias
Fecha:  
24/10/2026

Problema:  
Era necesario comprobar que las diferentes partes del sistema funcionaran de manera integrada y que las modificaciones realizadas durante el desarrollo no afectaran otras funcionalidades.

Causa:  
El sistema combina autenticación, estructuras de datos, reglas académicas y algoritmos sobre grafos, por lo que una revisión manual no era suficiente para garantizar que todas las funciones continuaran operando correctamente.

Solución:  
Se implementó y ejecutó una suite automatizada de pruebas mediante JUnit 5, organizada en tres clases:
- AutenticacionServiceTest
- PlanificadorAcademicoTest
- EstructurasTest

La suite completa ejecutó 18 pruebas relacionadas con autenticación, estructuras de datos, búsqueda de materias, disponibilidad académica, prerrequisitos, validaciones de relaciones, detección de ciclos y ordenamiento topológico.

Resultado:  
Las 18 pruebas finalizaron correctamente, obteniéndose:

- 18 pruebas ejecutadas.
- 0 fallos.
- 0 errores.
- 0 pruebas omitidas.
- BUILD SUCCESS.

Decisión:  
Mantener las pruebas automatizadas como mecanismo de validación para futuras modificaciones del proyecto, especialmente durante la tercera etapa.

Aprendizaje:  
Las pruebas unitarias permiten detectar errores de forma temprana y proporcionan una manera repetible de comprobar que las funcionalidades existentes continúan operando correctamente después de realizar cambios en el sistema.
