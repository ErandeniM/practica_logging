# Práctica: Logging en Java con `java.util.logging`

**Nombre:** Erandeni Mendivil Morales
**Expediente:** 213204857
**Aplicación:** `ProcesadorCalificaciones`

## Descripción

Aplicación de consola que lee un archivo de calificaciones, valida cada registro y calcula el promedio de los valores válidos. Todo el diagnóstico se realiza con `java.util.logging` (no se usa `System.out.println()` como mecanismo de diagnóstico).

## Cómo ejecutar

```bash
javac ProcesadorCalificaciones.java
java ProcesadorCalificaciones calificaciones.txt
```

Prueba de error:

```bash
java ProcesadorCalificaciones inexistente.txt
```

## Configuración de logging usada

| Elemento | Nivel | Formato | Destino |
|---|---|---|---|
| `Logger` | `ALL` | — | — |
| `ConsoleHandler` | `INFO` | `SimpleFormatter` (predeterminado) | consola (`System.err`) |
| `FileHandler` | `ALL` | `SimpleFormatter` | `procesador.log` |

Se usa `LOG.setUseParentHandlers(false)` para evitar mensajes duplicados por el handler heredado del logger raíz.

## Eventos registrados

| Evento | Nivel |
|---|---|
| Inicio de la aplicación | INFO |
| Nombre del archivo recibido | CONFIG |
| Apertura correcta del archivo | FINE |
| Cada línea procesada | FINER |
| Calificación válida | FINE |
| Valor no numérico | WARNING |
| Valor fuera de rango | WARNING |
| Archivo inexistente | SEVERE (con la excepción) |
| Número total de registros | INFO |
| Número de registros inválidos | INFO |
| Promedio calculado | INFO |
| Fin del procesamiento | INFO |

## Actividad guiada 1: `System.out` vs `Logger`

1. **¿Qué información adicional muestra el Logger?** Fecha y hora del evento, nombre del logger (la clase que lo declara), la clase y el método donde se generó el mensaje, y el nivel de importancia. `System.out.println()` solo imprime el texto.
2. **¿Aparece la fecha/hora?** Sí, en la primera línea del registro.
3. **¿Aparece el nombre de la clase?** Sí, junto con el método (`ProcesadorCalificaciones main`).
4. **¿Aparece el nivel del mensaje?** Sí (`INFO`, `WARNING`, `SEVERE`, etc.).
5. **¿Cuál proporciona más información para diagnosticar?** El `Logger`: además del contexto, permite filtrar por nivel, enviar la salida a varios destinos y desactivarse sin borrar código.

*Nota:* `ConsoleHandler` escribe en `System.err`, por eso en IntelliJ los mensajes del logger aparecen en rojo y pueden salir intercalados con los de `System.out`.

## Actividad de aprendizaje: clasificar mensajes

| Situación | Nivel propuesto |
|---|---|
| La aplicación inicia correctamente | INFO |
| No existe el archivo solicitado | SEVERE |
| Se procesaron 250 registros | INFO |
| Se encontró una calificación inválida | WARNING |
| Se cargó `application.properties` | CONFIG |
| Se desea conocer el valor de una variable durante depuración | FINE |
| La aplicación no puede continuar | SEVERE |

## Actividad guiada 2: filtrado por niveles

Mensajes que se muestran en cada configuración (con los handlers en el mismo nivel o inferior):

| Configuración | SEVERE | WARNING | INFO | FINE |
|---|---|---|---|---|
| `SEVERE` | Sí | No | No | No |
| `WARNING` | Sí | Sí | No | No |
| `INFO` | Sí | Sí | Sí | No |
| `FINE` | Sí | Sí | Sí | Sí |
| `ALL` | Sí | Sí | Sí | Sí |

Un nivel deja pasar los mensajes de ese nivel y de todos los superiores. El filtrado se aplica dos veces: primero en el `Logger` y después en cada `Handler`; si cualquiera de los dos es más restrictivo, el mensaje no aparece.

## Comparación XML vs texto plano

| Característica | XML | Texto |
|---|---|---|
| Fácil de leer por una persona | No | Sí |
| Fácil de procesar automáticamente | Sí | No |
| Estructura explícita | Sí | No |
| Tamaño más compacto | No | Sí |
| Adecuado para inspección rápida | No | Sí |

`FileHandler` usa `XMLFormatter` de forma predeterminada; para obtener texto plano hay que asignarle explícitamente un `SimpleFormatter`.

## Diferencia entre `Logger`, `Handler`, `Level` y `Formatter`

- **`Logger`**: es el punto de entrada. La aplicación le envía los mensajes; él decide, según su nivel, si los descarta o los pasa a sus handlers. Se obtiene con `Logger.getLogger(nombre)` y normalmente se declara como constante por clase.
- **`Level`**: representa la importancia de un mensaje (`SEVERE`, `WARNING`, `INFO`, `CONFIG`, `FINE`, `FINER`, `FINEST`). Funciona además como umbral de filtrado tanto en el `Logger` como en cada `Handler`.
- **`Handler`**: es el destino del registro. `ConsoleHandler` escribe en consola, `FileHandler` en un archivo y `SocketHandler` en la red. Un mismo `Logger` puede tener varios handlers con niveles distintos.
- **`Formatter`**: define cómo se ve cada registro en la salida. `SimpleFormatter` produce texto legible y `XMLFormatter` produce un documento XML estructurado.

En resumen: el `Logger` decide **si** se registra, el `Level` define **con qué importancia**, el `Handler` decide **a dónde** va y el `Formatter` **cómo se ve**.

## Archivos del repositorio

- `ProcesadorCalificaciones.java` — código fuente instrumentado
- `calificaciones.txt` — archivo de datos de prueba (incluye valores válidos, no numéricos y fuera de rango)
- `procesador.log` — bitácora generada durante la ejecución
- `README.md` — tablas completadas y respuestas
