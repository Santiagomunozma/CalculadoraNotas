# Briefing para diseñar la ponencia — Calculadora de notas

Documento de contexto para Claude (o cualquier asistente) que vaya a redactar o diseñar la presentación. Todo lo que está marcado como **dato real** sale del código y del README del proyecto. Lo marcado como **pendiente** no existe en el repositorio: no lo inventes; deja un hueco o pregunta al autor.

Fecha de este briefing: 7 de noviembre de 2026.
Idioma de la app y de la ponencia: español.

---

## 1. La app

| Campo | Contenido |
|---|---|
| Nombre visible | **Calculadora de notas** |
| Título dentro de la pantalla | Calificaciones del curso |
| Subtítulo | «Registre varios estudiantes con sus tres calificaciones» |
| Paquete Android | `com.example.calculadoranotas` |
| Versión | 1.0 (`versionCode` 1) |
| Tipo | Aplicación Android nativa, una sola pantalla |

**Qué hace.** Permite registrar estudiantes de un curso, cada uno con exactamente tres calificaciones en escala de 0.0 a 5.0. Calcula el promedio de cada uno, indica si aprobó o reprobó, y muestra un resumen del grupo: total, aprobados, reprobados, promedio general y el estudiante con mejor promedio. También se puede buscar por nombre, ordenar y borrar. Los datos se guardan en el teléfono y vuelven a aparecer al abrir la app.

**Qué problema resuelve.** En un curso con escala colombiana (0 a 5, aprobación desde 3.0), armar el corte a mano o en una hoja de cálculo obliga a repetir fórmulas, vigilar decimales con coma y contar a ojo quién pasó. Esta app concentra registro, promedio, estado y panorama del grupo en un solo lugar, sin cuenta ni internet.

**Promesa en una frase (para la diapositiva de apertura).** «Registra el curso, calcula quién aprueba y ves el panorama del grupo en el teléfono, con la escala de 0 a 5.»

---

## 2. Usuarios

**A quién va dirigida (inferido del producto, no de una investigación de usuarios).**

- Profesor o monitor que lleva las tres notas de un solo curso y quiere ver rápido cuántos aprueban, el promedio del grupo y quién va mejor.
- Estudiante que quiere simular su propio resultado o el de un grupo pequeño, con la misma escala del colegio o la universidad en Colombia y países que usan 0–5 y nota de aprobación 3.0.

**No está pensada para:**

- Una institución completa (varios cursos, varios periodos, roles de rectoría).
- Padres que consultan boletines en línea.
- Sistemas oficiales de notas (no reemplaza el SIA, el SIGA ni la plataforma del colegio).

**Contexto de uso.** Una persona, un dispositivo, un curso, sin conexión. El nombre del estudiante se normaliza («  ana garcía  » queda como «Ana García»). No se permiten dos estudiantes con el mismo nombre, sin distinguir mayúsculas.

---

## 3. Funciones principales

Estas cuatro son las que merecen diapositiva o demo. El resto es detalle de apoyo.

### 1. Registro con validación

- Nombre y tres calificaciones.
- Acepta punto o coma decimal (`4.5` y `4,5`).
- Rechaza campos vacíos, texto no numérico, notas fuera de 0.0–5.0 y nombres duplicados.
- Botón «Limpiar formulario» vacía los campos sin tocar la lista.
- Mensajes de error en español, en una tarjeta visible.

### 2. Resultado académico inmediato

- Promedio aritmético simple de las tres notas (mismo peso cada una).
- **Aprobado** si el promedio es mayor o igual a **3.0**. Si no, **Reprobado**.
- El estado se ve con color: verde si aprobó, rojo si reprobó.
- Cada tarjeta muestra nombre, las tres notas, el promedio y un botón Eliminar.

### 3. Resumen del curso en vivo

Se actualiza al agregar o eliminar:

- Total de estudiantes
- Aprobados
- Reprobados
- Promedio general del grupo
- Mejor promedio (nombre y nota)

Si no hay estudiantes, el promedio general y el mejor estudiante se muestran como «—».

### 4. Buscar, ordenar y no perder los datos

- Búsqueda parcial por nombre, sin distinguir mayúsculas.
- Orden por nombre (A–Z) o por promedio (de mayor a menor).
- Persistencia local automática: al agregar o eliminar se guarda; al abrir la app se carga.
- Si el archivo guardado está dañado, la app abre con lista vacía en lugar de cerrarse.

**Lo que la app no hace (decirlo evita preguntas incómodas):**

- No edita un estudiante ya guardado: hay que eliminarlo y volver a crearlo.
- No hay porcentajes distintos por nota (no es promedio ponderado).
- No hay varios cursos, periodos, materias ni usuarios.
- No exporta a Excel, PDF ni correo.
- No hay nube, login ni sincronización entre teléfonos.
- No calcula «qué nota necesita en el tercer corte para pasar».

---

## 4. Tecnología y estado de publicación

| Pieza | Detalle |
|---|---|
| Lenguaje | Kotlin |
| Interfaz | Jetpack Compose, Material 3 |
| Arquitectura | MVVM: `MainActivity` (UI), `CursoViewModel` (estado), `CalculadoraNotas` (reglas), `CursoRepositorio` (datos) |
| Persistencia | SharedPreferences, lista serializada a JSON. Archivo de preferencias `curso_calificaciones`, clave `estudiantes` |
| Diseño visual | Tarjetas y botones con efecto neumórfico; paleta teal académica; modo claro y oscuro según el sistema |
| SDK | `minSdk` 24 (Android 7.0), `compileSdk` / `targetSdk` 37, JDK 11 |
| Pruebas | JUnit sobre la lógica pura (validación, aprobación, coma decimal, duplicados, estadísticas, filtro y orden). También hay pruebas instrumentadas de ejemplo |
| Herramienta | Android Studio |

**Estado: prototipo funcional, no publicada.**

- `versionName` 1.0, pero el `applicationId` sigue siendo `com.example.calculadoranotas` (paquete de plantilla, no de una app en tienda).
- No hay ficha de Google Play, enlace de descarga, firma de release activada ni backend.
- Se ejecuta desde Android Studio en emulador o teléfono físico (API 24 o superior).

**Cómo decirlo en la ponencia.** «Prototipo funcional de Android. La lógica académica está cubierta con pruebas unitarias. Todavía no está en una tienda.»

---

## 5. Evidencia

**Dato real.** En el repositorio hay pruebas unitarias que cubren:

- Tres notas válidas no producen error.
- Promedio exactamente 3.0 aprueba.
- Promedio menor que 3.0 reprueba.
- Una nota mayor que 5.0 produce error.
- La coma funciona como separador decimal.
- Un nombre duplicado produce error, también si cambia solo las mayúsculas.
- Las estadísticas del curso (total, aprobados, reprobados, promedio, mejor) se calculan bien.
- La búsqueda parcial y el orden por promedio funcionan.

**Pendiente — no inventar cifras.** El repositorio no registra:

- Usuarios reales
- Pruebas con profesores o estudiantes
- Descargas, retención o tiempo de uso
- Comparación antes/después contra Excel
- Nombre del autor, materia, universidad o nota del proyecto

Si el autor ya la mostró en clase o a un profesor, debe completar esta tabla antes de la ponencia:

| Pregunta | Respuesta del autor |
|---|---|
| ¿Quién la probó? | |
| ¿Cuántas personas? | |
| ¿En qué contexto (clase, monitor, familia)? | |
| ¿Qué dijeron o qué falló? | |
| ¿Autor y programa académico? | |

Hasta llenar esa tabla, la evidencia defendible es técnica: reglas de negocio aisladas de la interfaz y verificadas con tests, no tracción de usuarios.

---

## 6. Competencia y diferencia

No citar estas apps como si se hubiera hecho un estudio de mercado formal. Son el marco habitual con el que un jurado va a comparar.

| Alternativa | Qué cubre | Dónde se queda corta para este caso |
|---|---|---|
| Excel o Google Sheets | Flexible, exportable, conocido | Hay que armar fórmulas, formatos y el conteo de aprobados. La coma decimal y los rangos fallan fácil si alguien no sabe hojas de cálculo |
| Calculadoras de notas genéricas (Play Store) | Muchas calculan «cuánto necesito para pasar» | Casi todas están pensadas para un solo estudiante, escalas 0–10 o 0–100, o porcentajes de tareas. Pocas llevan el listado de un curso en escala 0–5 |
| Google Classroom, Moodle, plataformas del colegio | Entrega de trabajos y libro de notas institucional | Dependen de que el colegio las use, piden cuenta y no están hechas para un corte rápido de tres notas en el teléfono del profesor |
| Apps de libro de notas (Additio, iDoceo, TeacherKit) | Cursos, asistencia, rúbricas, informes | Son productos grandes, de pago o con curva de aprendizaje. Esta app es un corte simple: tres notas, promedio y estado |

**Diferencia real, sin exagerar:**

1. Escala y regla local: 0.0–5.0 y aprobación en 3.0, con coma o punto.
2. Vista de curso, no solo de un alumno: aprobados, reprobados, promedio del grupo y mejor estudiante.
3. Funciona sin internet, sin cuenta y sin instalar una suite docente.
4. Interfaz en español, pensada para registrar varios nombres en la misma sesión.
5. La lógica está separada de la pantalla y tiene pruebas, lo cual importa si el público es académico.

**No afirmar** que «no existe nada parecido» ni que «reemplaza al sistema de la universidad».

---

## 7. La ponencia

**Pendiente — el autor debe completar esto.** Sin estos datos, Claude no debe asumir duración, jurado ni rúbrica.

| Pregunta | Respuesta del autor |
|---|---|
| ¿Cuántos minutos tienes? | |
| ¿Hay tiempo de preguntas? | |
| ¿Quién escucha? (profesores, jurado, compañeros, inversionistas, otro) | |
| ¿Materia o evento? | |
| ¿Hay plantilla, número de diapositivas o rúbrica? | |
| ¿Debes mostrar la app en vivo, un video o solo diapositivas? | |
| ¿El entregable es solo oral, o también informe PDF? | |

**Mientras no haya rúbrica, una estructura segura para una exposición académica corta (5–8 minutos):**

1. Problema: llevar tres notas de un curso en escala 0–5 sin perderse en la hoja de cálculo (45–60 s).
2. Qué es la app, en una frase, con captura de la pantalla principal (30 s).
3. Demo o recorrido de las cuatro funciones: registrar, ver aprobado/reprobado, resumen del curso, buscar y guardar (2–3 min).
4. Cómo está hecha, en una diapositiva: Kotlin, Compose, MVVM, datos en el teléfono, pruebas (45 s).
5. Qué no es: prototipo, un curso, tres notas del mismo peso (30 s).
6. Diferencia frente a Excel y a una calculadora de un solo estudiante (45 s).
7. Cierre: siguiente paso concreto (publicar, ponderar notas, varios cursos o exportar) (30 s).

Si el público fueran inversionistas, esta app todavía no tiene modelo de negocio, usuarios ni mercado medido. No armes un pitch de startup con cifras inventadas. El relato sólido hoy es de proyecto académico o prototipo de producto.

---

## 8. Material disponible para diseño y contenido

### Lo que sí está en el proyecto

| Material | Dónde | Nota para el diseño |
|---|---|---|
| Este briefing | `CONTEXTO_PONENCIA.md` | Fuente principal para la ponencia |
| Documentación técnica | `README.md` | Arquitectura, reglas, flujo de datos y cómo ejecutar. Úsalo como anexo, no lo leas completo en la exposición |
| Textos de la interfaz | `app/src/main/res/values/strings.xml` | Copiar los nombres reales de botones y secciones |
| Código de reglas | `app/src/main/java/com/example/calculadoranotas/logic/CalculadoraNotas.kt` | Si piden «cómo calcula» |
| Pruebas | `app/src/test/java/com/example/calculadoranotas/CalculadoraNotasTest.kt` | Evidencia técnica |
| Icono | `app/src/main/res/mipmap-anydpi-v26/ic_launcher.xml` y `drawable/ic_launcher_foreground.xml` | Es el icono de robot de la plantilla de Android (fondo del launcher por defecto). No es un logo de marca. No lo presentes como identidad final |

### Lo que no está y haría falta para un diseño cuidado

- Capturas de pantalla (claro y oscuro, lista vacía, lista con aprobados y reprobados, mensaje de error).
- Logo propio en PNG o SVG.
- Video corto de la demo.
- Enlace de Play Store, APK publicado o repositorio remoto.
- Nombre del autor, foto, universidad o asignatura.
- Plantilla de diapositivas del curso.

**Pendiente de capturas.** Si el autor las toma, nombrarlas así para que el diseño las encuentre:

- `01-inicio-vacio.png` — pantalla sin estudiantes
- `02-formulario-error.png` — nota inválida o nombre duplicado
- `03-resumen-y-lista.png` — resumen del curso con al menos un aprobado y un reprobado
- `04-busqueda.png` — filtro por nombre
- `05-modo-oscuro.png` — el mismo estado en tema oscuro

### Identidad visual actual (para que las diapositivas no choquen con la app)

- Fondos de pantalla: claro `#E6E9EF`, oscuro `#1A202C`.
- Acento teal: primario claro `#006874`, primario oscuro `#82D3E0`.
- Aprobado: verde `#0F5132` sobre `#D1E7DD` (claro) o `#95D5B2` sobre `#1B4332` (oscuro).
- Reprobado: `#842029` sobre `#F8D7DA` (claro) o `#F5C2C7` sobre `#3D1515` (oscuro).
- Tarjetas con esquinas amplias (28 dp) y relieve suave, no un dashboard plano de corporativo azul.
- Tono de la interfaz: sobrio, académico, en español, sin ilustraciones ni mascota.

Sugerencia de estilo para las diapositivas: fondo claro, títulos en teal `#006874`, un solo verde y un solo rojo para aprobado/reprobado, mucho espacio y pocas palabras por lámina. Evitar morado genérico de «app de startup» y evitar el robot de Android como logo.

---

## 9. Reglas de negocio (para no equivocar un ejemplo en una diapositiva)

| Regla | Valor |
|---|---|
| Notas por estudiante | Exactamente 3 |
| Rango | 0.0 a 5.0 inclusive |
| Cálculo | Promedio aritmético simple |
| Aprueba | Promedio ≥ 3.0 |
| Reprueba | Promedio < 3.0 |
| Decimales | Punto o coma |
| Nombres | Obligatorios, únicos sin importar mayúsculas, cada palabra con inicial mayúscula |
| Orden por defecto | Nombre, A–Z |
| Orden alterno | Promedio, de mayor a menor |
| Búsqueda | Contiene el texto, sin distinguir mayúsculas |
| Persistencia | Local, JSON en SharedPreferences |

**Ejemplo correcto para una diapositiva o la demo:**

| Estudiante | Notas | Promedio | Estado |
|---|---|---|---|
| Ana García | 4.5, 3.0, 5.0 | 4.17 | Aprobado |
| Luis Pérez | 2.0, 2.5, 1.5 | 2.00 | Reprobado |

Con esos dos registros el resumen debe decir: 2 estudiantes, 1 aprobado, 1 reprobado, promedio general 3.08, mejor promedio Ana García (4.17). Los promedios se muestran con dos decimales.

Otro caso que conviene mencionar en preguntas: 3.0, 3.0 y 3.0 aprueba. 2.9, 3.0 y 3.0 da 2.97 y reprueba.

---

## 10. Arquitectura en una sola diapositiva

```
Pantalla (Jetpack Compose, MainActivity)
        │
        ▼
Estado (CursoViewModel)
        │
        ├─ CalculadoraNotas  → validar, promediar, estadísticas, filtrar, ordenar
        │
        └─ CursoRepositorio  → guardar y cargar JSON en el teléfono
```

Frase para decirla en voz alta: «La pantalla no calcula. Pide el resultado a una capa de reglas que se puede probar sin abrir la app, y otra capa se encarga de que los datos sigan ahí al cerrarla.»

Eso es lo más defendible del proyecto frente a un profesor de software: separación de interfaz, estado, reglas y almacenamiento.

---

## 11. Guion corto de demo (si hay teléfono o emulador)

1. Abrir la app en la lista vacía. Mostrar el resumen con «—».
2. Agregar a Ana García con 4,5 / 3 / 5. Señalar que la coma funciona y que el nombre queda bien escrito.
3. Agregar a Luis Pérez con 2 / 2,5 / 1,5. Mostrar verde y rojo, y el resumen (1 aprobado, 1 reprobado, mejor promedio).
4. Intentar agregar otra vez a «ana garcía» y mostrar el error de duplicado.
5. Buscar «lu» y luego ordenar por promedio.
6. Cerrar y volver a abrir para mostrar que los datos siguen (solo si hay tiempo).

Si la demo puede fallar, llevar las capturas de la sección 8 en el mismo orden.

---

## 12. Límites y siguiente paso (cierre honesto)

Ideas reales de evolución, en orden de sentido para este producto. Elige una; no prometas las cuatro.

1. Editar un estudiante sin borrarlo.
2. Elegir el peso de cada nota (corte 30 %, 30 %, 40 %).
3. Más de un curso en el mismo teléfono.
4. Exportar el listado a CSV o PDF.
5. Sustituir el icono de plantilla y publicar un APK o una ficha de prueba interna.

**Frase de cierre sugerida.** «Hoy es un prototipo para un curso y tres notas en escala de 0 a 5. Ya calcula, valida y guarda en el teléfono. El paso siguiente es [completar una sola mejora], no convertirla todavía en el sistema de una institución.»

---

## 13. Instrucciones para quien redacte las diapositivas

- Escribe en español, con frases cortas. El público las lee mientras alguien habla.
- Usa los nombres reales de la interfaz: «Agregar estudiante», «Resumen del curso», «Aprobado», «Reprobado».
- No inventes usuarios, premios, descargas, alianzas ni un modelo de negocio.
- No digas que está en Google Play.
- No uses el robot de Android como logo de la marca.
- Trata Excel como la alternativa cotidiana, y a las apps de un solo estudiante como la otra. La diferencia es el listado del curso en escala 0–5, sin internet.
- Si falta la duración o la rúbrica, usa la estructura de 7 bloques de la sección 7 y marca la portada con «[MINUTOS]» y «[PÚBLICO]».
- Un ejemplo numérico mal calculado daña la ponencia. Usa la tabla de Ana y Luis de la sección 9.
