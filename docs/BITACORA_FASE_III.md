
# Bitácora de desarrollo - Fase III

## Proyecto: Planificador Académico con Prerrequisitos

Universidad Don Bosco  
Programación con Estructuras de Datos - PED941  
Noviembre de 2026

---

## Incidencia 1. Sustitución del conjunto de datos de prueba

**Problema:** La versión anterior utilizaba siete materias de ejemplo, insuficientes para representar el pensum universitario completo.

**Solución:** Se incorporaron 40 espacios curriculares, 161 unidades valorativas y las relaciones de prerrequisitos correspondientes al plan ICC 2024-2027.

**Resultado:** Mayor representatividad del problema académico.



## Incidencia 2. Incompatibilidad de pruebas anteriores

**Problema:** Las pruebas automatizadas utilizaban códigos de materias pertenecientes al conjunto de datos de ejemplo.

**Solución:** Se actualizaron los casos de prueba para utilizar los códigos reales del pensum.

**Resultado:** Se recuperó la ejecución satisfactoria de las pruebas.



## Incidencia 3. Representación de las asignaturas electivas

**Problema:** Incorporar las nueve opciones electivas como asignaturas independientes alteraba la cantidad oficial de espacios curriculares y unidades valorativas.

**Solución:** Se creó un catálogo separado y se conservó ELEC38 como espacio curricular interno.

**Resultado:** El sistema mantiene 40 espacios curriculares y 161 UV.



## Incidencia 4. Incorporación de la arquitectura web

**Problema:** La versión inicial dependía exclusivamente de la consola Java.

**Solución:** Se incorporaron Spring Boot, Thymeleaf, controladores web y una capa de servicios.

**Resultado:** Se desarrolló una interfaz web sin sustituir las estructuras propias.



## Incidencia 5. Creación incorrecta de application.properties

**Problema:** El archivo de configuración fue creado inicialmente como una clase Java, generando errores de compilación.

**Solución:** Se eliminó el archivo incorrecto y se creó application.properties dentro de src/main/resources.

**Resultado:** Maven compiló correctamente el proyecto.



## Incidencia 6. Duplicación de WebApplication.java

**Problema:** Existían dos archivos que declaraban la misma clase de inicio de Spring Boot.

**Solución:** Se eliminó el archivo duplicado y se mantuvo la clase ubicada en el paquete raíz.

**Resultado:** Se corrigió el error de clase duplicada.



## Incidencia 7. Bloqueo del archivo de base de datos

**Problema:** Una ejecución anterior de Spring Boot mantenía abierto el archivo planificador.mv.db.

**Solución:** Se identificó el proceso activo mediante netstat y se detuvo antes de reiniciar el servidor.

**Resultado:** Se restableció la conexión con H2.



## Incidencia 8. Interfaz web sin contenido

**Problema:** El servidor respondía correctamente con HTTP 200, pero el navegador mostraba una página vacía.

**Solución:** Se verificaron los recursos web y se identificó que index.html no contenía la implementación HTML correspondiente.

**Resultado:** Se incorporaron el dashboard y la hoja de estilos, permitiendo visualizar correctamente el sistema.



## Incidencia 9. Separación de perfiles

**Problema:** La primera versión web no diferenciaba los permisos administrativos y estudiantiles.

**Solución:** Se incorporó Spring Security y controladores independientes para ambos perfiles.

**Resultado:** Se implementó la autenticación web y la autorización por roles.



## Incidencia 10. Prevención de dependencias circulares

**Problema:** Una modificación administrativa podría introducir ciclos que invalidaran las relaciones académicas.

**Solución:** Se verificaron las operaciones mediante el núcleo del grafo antes de persistir los cambios administrativos.

**Resultado:** La aplicación rechazó la incorporación de DES104 como prerrequisito de PRE104.



## Incidencia 11. Evaluación del rendimiento

**Problema:** La versión anterior no contaba con mediciones experimentales de ejecución.

**Solución:** Se implementó MedicionRendimiento.java, utilizando System.nanoTime(), calentamiento y repeticiones.

**Resultado:** Se obtuvieron tiempos experimentales para doce operaciones y se generó rendimiento.csv.



## Incidencia 12. Validación final de persistencia

**Problema:** Era necesario verificar que el estado académico y los cambios administrativos pudieran recuperarse desde la base de datos.

**Solución:** Se incorporaron seis pruebas automatizadas utilizando bases H2 temporales e independientes.

**Resultado:** La suite final alcanzó 41 pruebas superadas, sin fallos ni errores.

---

## Resultado final

Fecha de validación: 24 de noviembre de 2026.

Tests run: 41  
Failures: 0  
Errors: 0  
Skipped: 0  

BUILD SUCCESS

El sistema cuenta con una versión de consola, una interfaz web, autenticación por roles, persistencia mediante H2, estructuras de datos propias, algoritmos de grafos y mediciones experimentales de rendimiento.

## Lecciones aprendidas

- La organización modular facilita incorporar nuevas tecnologías.
- Las pruebas automatizadas permiten detectar regresiones.
- La validación de ciclos es indispensable para proteger un grafo de prerrequisitos.
- La persistencia debe mantenerse separada de la lógica académica.
- La complejidad teórica y el rendimiento experimental son elementos complementarios.
- El control de versiones permite gestionar los cambios realizados durante las diferentes etapas.
