# CASOS DE PRUEBA – Planificador Académico PED941

## 1. OBJETIVO
El propósito de estos casos de prueba es verificar el correcto funcionamiento de las principales funcionalidades del Planificador Académico, así como validar las estructuras de datos implementadas y el sistema de autenticación por roles.

Las pruebas automatizadas fueron desarrolladas utilizando JUnit 5 y se encuentran distribuidas en tres clases:
- `AutenticacionServiceTest`
- `PlanificadorAcademicoTest`
- `EstructurasTest`

La ejecución completa de la suite produjo el siguiente resultado:
- Pruebas ejecutadas: 18
- Fallos: 0
- Errores: 0
- Pruebas omitidas: 0
- Resultado general: BUILD SUCCESS

---

# 2. PRUEBAS DE AUTENTICACION

## CP01 – Inicio de sesión del administrador
**Objetivo:**  
Comprobar que un usuario con credenciales válidas de administrador pueda iniciar sesión correctamente.

**Entrada:**
Usuario: `admin`  
Contraseña: `Admin123`

**Resultado esperado:**  
El sistema devuelve un usuario válido con rol `ADMINISTRADOR`.

**Resultado obtenido:**  
El usuario fue autenticado correctamente y se identificó su rol como administrador.

**Estado:** APROBADO

---

## CP02 – Inicio de sesión del estudiante
**Objetivo:**  
Comprobar que un estudiante pueda iniciar sesión utilizando credenciales válidas.

**Entrada:**
Usuario: `estudiante`  
Contraseña: `Est123`

**Resultado esperado:**  
El sistema devuelve un usuario válido con rol `ESTUDIANTE`.

**Resultado obtenido:**  
El usuario fue autenticado correctamente y se identificó su rol como estudiante.

**Estado:** APROBADO

---

## CP03 – Contraseña incorrecta
**Objetivo:**  
Comprobar que el sistema rechace el inicio de sesión cuando la contraseña no corresponde al usuario.

**Entrada:**
Usuario: `admin`  
Contraseña: `incorrecta`

**Resultado esperado:**  
El sistema no autentica al usuario.

**Resultado obtenido:**  
El método de autenticación devolvió un valor nulo y el acceso fue rechazado.

**Estado:** APROBADO

---

## CP04 – Usuario inexistente
**Objetivo:**  
Comprobar el comportamiento del sistema ante un nombre de usuario que no se encuentra registrado.

**Entrada:**
Usuario: `otro`  
Contraseña: `123`

**Resultado esperado:**  
El sistema no permite iniciar sesión.

**Resultado obtenido:**  
El usuario no fue encontrado y el método devolvió un valor nulo.

**Estado:** APROBADO

---

# 3. PRUEBAS DEL PLANIFICADOR ACADEMICO

## CP05 – Buscar materia existente
**Objetivo:**  
Verificar que una materia registrada pueda localizarse mediante su código.

**Entrada:**
Código: `PRG01`

**Resultado esperado:**  
La búsqueda devuelve la materia correspondiente.

**Resultado obtenido:**  
La materia fue encontrada correctamente.

**Estado:** APROBADO

---

## CP06 – Buscar materia inexistente
**Objetivo:**  
Comprobar el comportamiento del sistema cuando se consulta un código que no existe.

**Entrada:**
Código: `XXX99`

**Resultado esperado:**  
La búsqueda no debe devolver ninguna materia.

**Resultado obtenido:**  
El sistema devolvió un valor nulo.

**Estado:** APROBADO

---

## CP07 – Registro de código duplicado
**Objetivo:**  
Comprobar que no puedan existir dos materias con el mismo código.

**Entrada:**
Código existente: `PRG01`

**Resultado esperado:**  
El sistema debe rechazar el registro mediante una excepción controlada.

**Resultado obtenido:**  
Se generó correctamente una `IllegalArgumentException`.

**Estado:** APROBADO

---

## CP08 – Materias disponibles inicialmente
**Objetivo:**  
Verificar las materias disponibles cuando el estudiante todavía no ha registrado asignaturas aprobadas.

**Resultado esperado:**
- `PRG01`
- `MAT01`

Cantidad esperada: 2 materias.

**Resultado obtenido:**  
El sistema identificó correctamente las dos materias que no poseen prerrequisitos pendientes.

**Estado:** APROBADO

---

## CP09 – Materias disponibles después de aprobar el primer ciclo
**Objetivo:**  
Comprobar que el sistema habilite nuevas materias cuando sus prerrequisitos han sido aprobados.

**Materias marcadas como aprobadas:**
- `PRG01`
- `MAT01`

**Resultado esperado:**
Deben aparecer disponibles:
- `PRG02`
- `BDT02`

**Resultado obtenido:**  
El sistema habilitó correctamente ambas materias.

**Estado:** APROBADO

---

## CP10 – Detección de prerrequisito pendiente
**Objetivo:**  
Verificar que el sistema identifique los prerrequisitos que todavía impiden cursar una materia.

**Materia consultada:**  
`PED03`

**Resultado esperado:**  
Debe identificarse `PRG02` como prerrequisito pendiente.

**Resultado obtenido:**  
El sistema identificó correctamente `PRG02`.

**Estado:** APROBADO

---

## CP11 – Rechazo de autorreferencia
**Objetivo:**  
Evitar que una materia pueda convertirse en prerrequisito de sí misma.

**Entrada:**
`PRG01 → PRG01`

**Resultado esperado:**  
La relación debe ser rechazada.

**Resultado obtenido:**  
El sistema generó correctamente una `IllegalArgumentException`.

**Estado:** APROBADO

---

## CP12 – Rechazo de relación duplicada
**Objetivo:**  
Comprobar que una relación de prerrequisito existente no pueda registrarse nuevamente.

**Relación existente:**
`PRG01 → PRG02`

**Resultado esperado:**  
La operación debe rechazarse.

**Resultado obtenido:**  
El sistema generó correctamente una excepción controlada.

**Estado:** APROBADO

---

## CP13 – Rechazo de dependencia circular
**Objetivo:**  
Comprobar que el sistema conserve el grafo académico como un grafo dirigido acíclico.

**Relación intentada:**
`PRO04 → PRG01`

**Resultado esperado:**  
La relación debe rechazarse porque genera un ciclo en el grafo.

**Resultado obtenido:**  
El sistema detectó la dependencia circular y rechazó la relación.

**Estado:** APROBADO

---

## CP14 – Ordenamiento topológico
**Objetivo:**  
Comprobar que el algoritmo de ordenamiento topológico respete las relaciones de prerrequisito.

**Validaciones realizadas:**
- `PRG01` debe aparecer antes de `PRG02`.
- `PRG02` debe aparecer antes de `PED03`.
- `PED03` debe aparecer antes de `PRO04`.

**Resultado esperado:**  
Las materias deben respetar todas las relaciones de precedencia.

**Resultado obtenido:**  
El orden generado cumplió correctamente las relaciones evaluadas.

**Estado:** APROBADO

---

# 4. PRUEBAS DE ESTRUCTURAS DE DATOS

## CP15 – Lista enlazada
**Objetivo:**  
Verificar las operaciones básicas de inserción y eliminación.

**Acciones:**
1. Agregar `"A"`.
2. Agregar `"B"`.
3. Verificar tamaño igual a 2.
4. Eliminar `"A"`.
5. Verificar tamaño igual a 1.

**Resultado esperado:**  
La lista debe actualizar correctamente su contenido y tamaño.

**Resultado obtenido:**  
La estructura respondió correctamente.

**Estado:** APROBADO

---

## CP16 – Cola FIFO
**Objetivo:**  
Comprobar que la cola respete el principio FIFO, primero en entrar, primero en salir.

**Acciones:**
1. Encolar `"A"`.
2. Encolar `"B"`.
3. Desencolar.

**Resultado esperado:**
Primer elemento: `"A"`  
Segundo elemento: `"B"`

**Resultado obtenido:**  
Los elementos fueron retirados en el orden correcto.

**Estado:** APROBADO

---

## CP17 – Pila LIFO
**Objetivo:**  
Comprobar que la pila respete el principio LIFO, último en entrar, primero en salir.

**Acciones:**
1. Insertar `"A"`.
2. Insertar `"B"`.
3. Extraer elementos.

**Resultado esperado:**
Primer elemento extraído: `"B"`  
Segundo elemento extraído: `"A"`

**Resultado obtenido:**  
La pila funcionó correctamente.

**Estado:** APROBADO

---

## CP18 – Tabla Hash
**Objetivo:**  
Comprobar que la tabla hash permita almacenar y recuperar información mediante una clave.

**Entrada:**
Clave: `PRG01`  
Valor: `Programación I`

**Resultado esperado:**  
Al buscar `PRG01`, debe devolverse `Programación I`.

**Resultado obtenido:**  
El valor almacenado fue recuperado correctamente.

**Estado:** APROBADO

---

# 5. RESULTADO GENERAL
La suite automatizada fue ejecutada completamente mediante JUnit 5.

Resultado obtenido:

`Tests run: 18, Failures: 0, Errors: 0, Skipped: 0`

`BUILD SUCCESS`

Los resultados demuestran que las funcionalidades principales desarrolladas para la segunda etapa operan correctamente en los escenarios evaluados, incluyendo casos normales, entradas incorrectas y situaciones límite relacionadas con las reglas del negocio.
