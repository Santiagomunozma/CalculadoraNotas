# Calculadora de Notas

App Android nativa con **Kotlin** y **Jetpack Compose** para registrar estudiantes con tres calificaciones, calcular promedios, ver estadísticas del curso, buscar, ordenar y guardar los datos localmente.

---

## Tabla de contenidos

1. [Arquitectura general](#arquitectura-general)
2. [Estructura del proyecto](#estructura-del-proyecto)
3. [Componentes uno a uno](#componentes-uno-a-uno)
4. [Funcionalidades de la app](#funcionalidades-de-la-app)
5. [Flujo de datos](#flujo-de-datos)
6. [Reglas de negocio](#reglas-de-negocio)
7. [Cómo ejecutar y probar](#cómo-ejecutar-y-probar)

---

## Arquitectura general

La app sigue una estructura en capas:

```
┌─────────────────────────────────────┐
│  MainActivity.kt  (UI - Compose)    │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│  CursoViewModel.kt  (Estado)        │
└──────┬───────────────────┬──────────┘
       │                   │
┌──────▼──────────┐  ┌─────▼────────────┐
│ CalculadoraNotas│  │ CursoRepositorio │
│ (Lógica)        │  │ (Persistencia)   │
└──────┬──────────┘  └─────┬────────────┘
       │                   │
       └─────────┬─────────┘
                 │
       ┌─────────▼─────────┐
       │  Modelos (data)   │
       └───────────────────┘
```

| Capa | Archivo | Responsabilidad |
|------|---------|-----------------|
| UI | `MainActivity.kt` | Pantallas y componentes visuales |
| ViewModel | `CursoViewModel.kt` | Estado reactivo y acciones del usuario |
| Lógica | `CalculadoraNotas.kt` | Validación, cálculos, filtro y orden |
| Datos | `CursoRepositorio.kt` | Guardar/cargar en SharedPreferences |
| Modelos | `model/*.kt` | Estructuras de datos del dominio |

---

## Estructura del proyecto

```
CalculadoraNotas/
├── app/src/main/java/com/example/calculadoranotas/
│   ├── MainActivity.kt              → UI completa (Jetpack Compose)
│   ├── viewmodel/
│   │   └── CursoViewModel.kt        → Estado y acciones de la pantalla
│   ├── Logic/
│   │   └── CalculadoraNotas.kt      → Reglas académicas y cálculos
│   ├── data/
│   │   └── CursoRepositorio.kt      → Persistencia local (JSON)
│   ├── model/
│   │   ├── ResultadoAcademico.kt    → Un estudiante con sus notas
│   │   ├── EstadisticasCurso.kt     → Resumen del curso
│   │   └── OrdenEstudiantes.kt      → Criterios de ordenamiento
│   └── ui/theme/
│       ├── Theme.kt                 → Tema Material 3
│       ├── Color.kt                 → Paleta de colores
│       └── Type.kt                  → Tipografía
├── app/src/main/res/
│   ├── values/strings.xml           → Textos de la interfaz
│   ├── values/colors.xml            → Colores XML
│   ├── values/themes.xml            → Tema base Android
│   └── AndroidManifest.xml          → Configuración de la app
└── app/src/test/
    └── CalculadoraNotasTest.kt      → Pruebas unitarias
```

---

## Componentes uno a uno

### 1. `MainActivity.kt` — Punto de entrada y UI

**Ruta:** `app/src/main/java/com/example/calculadoranotas/MainActivity.kt`

Es la actividad principal. Arranca la app, activa el modo *edge-to-edge* y monta toda la interfaz con Compose.

| Composable | Qué hace |
|------------|----------|
| `MainActivity` | Inicializa `CalculadoraNotasTheme` y muestra la app |
| `AplicacionCalculadoraNotas` | Pantalla principal; conecta UI con `CursoViewModel` |
| `Encabezado` | Título y descripción de la app |
| `FormularioNotas` | Campos para nombre y 3 calificaciones |
| `CampoNota` | Campo numérico con indicación de rango (0.0–5.0) |
| `MensajeError` | Tarjeta roja cuando hay errores de validación |
| `TarjetaResumen` | Resumen estadístico del curso |
| `TarjetaEstudiante` | Tarjeta por estudiante: notas, promedio, estado, eliminar |
| `TextoListaVacia` | Mensaje cuando no hay estudiantes o no hay coincidencias |
| `FilaInformacion` | Fila reutilizable etiqueta ↔ valor |
| `colorEstado()` | Verde si aprobó, rojo si reprobó |
| `formatearNumero()` | Formatea números con 2 decimales |

**Qué estudiar aquí:**
- Cómo Compose observa el estado del ViewModel (`viewModel.estudiantes`, `viewModel.mensajeError`, etc.)
- Uso de `LazyColumn` para listas largas
- `FilterChip` para ordenamiento
- Separación de UI en composables pequeños y reutilizables

---

### 2. `CursoViewModel.kt` — Estado y acciones

**Ruta:** `app/src/main/java/com/example/calculadoranotas/viewmodel/CursoViewModel.kt`

Gestiona el estado de la pantalla con `mutableStateOf` (patrón MVVM).

#### Estado (propiedades observables)

| Propiedad | Tipo | Función |
|-----------|------|---------|
| `nombre` | `String` | Nombre del formulario |
| `nota1`, `nota2`, `nota3` | `String` | Calificaciones del formulario |
| `mensajeError` | `String?` | Error de validación (null = sin error) |
| `consultaBusqueda` | `String` | Texto del buscador |
| `orden` | `OrdenEstudiantes` | Criterio de ordenamiento |
| `estudiantes` | `List<ResultadoAcademico>` | Lista completa guardada |
| `estadisticas` | `EstadisticasCurso` | Calculadas en tiempo real |
| `estudiantesVisibles` | `List<ResultadoAcademico>` | Filtrados + ordenados para la UI |

#### Acciones (funciones)

| Función | Qué hace |
|---------|----------|
| `actualizarNombre/Nota1/Nota2/Nota3()` | Actualiza campos y limpia errores |
| `actualizarBusqueda()` | Cambia el filtro de búsqueda |
| `actualizarOrden()` | Cambia el criterio de orden |
| `agregarEstudiante()` | Valida → calcula → agrega → guarda → limpia formulario |
| `eliminarEstudiante()` | Quita de la lista y persiste |
| `limpiarFormulario()` | Vacía todos los campos |
| `persistir()` | Llama al repositorio para guardar |

**Qué estudiar aquí:**
- Patrón MVVM: la UI no contiene lógica de negocio
- `estudiantesVisibles` es una propiedad calculada (filtro + orden en cadena)
- Al agregar, se delega validación a `CalculadoraNotas` y persistencia a `CursoRepositorio`

---

### 3. `CalculadoraNotas.kt` — Lógica de negocio

**Ruta:** `app/src/main/java/com/example/calculadoranotas/Logic/CalculadoraNotas.kt`

Objeto singleton (`object`) con toda la lógica académica. **No depende de Android UI**, por eso es fácil de probar con unit tests.

| Función | Qué hace |
|---------|----------|
| `convertirNota(texto)` | Convierte `"4,5"` o `"4.5"` a `Double` |
| `validar(nombre, notas)` | Valida nombre, 3 notas numéricas y rango 0.0–5.0 |
| `validarRegistro(...)` | `validar()` + verifica nombres duplicados |
| `calcular(nombre, notas)` | Crea un `ResultadoAcademico` con nombre formateado |
| `calcularEstadisticas(estudiantes)` | Total, aprobados, reprobados, promedio, mejor |
| `filtrarPorNombre(estudiantes, consulta)` | Búsqueda parcial sin distinguir mayúsculas |
| `ordenar(estudiantes, criterio)` | Por nombre (A–Z) o promedio (mayor a menor) |
| `formatearNombre(nombre)` | `"  ana garcía  "` → `"Ana García"` (privada) |

**Constantes importantes:**
```kotlin
const val CANTIDAD_NOTAS = 3
const val NOTA_MINIMA = 0.0
const val NOTA_MAXIMA = 5.0
```

**Qué estudiar aquí:**
- Separación clara entre validación, cálculo y consultas
- Retorno de errores como `String?` (null = todo OK)
- Funciones puras fáciles de testear

---

### 4. Modelos de datos

#### `ResultadoAcademico.kt`

**Ruta:** `app/src/main/java/com/example/calculadoranotas/model/ResultadoAcademico.kt`

```kotlin
data class ResultadoAcademico(
    val nombreEstudiante: String,
    val notas: List<Double>
) {
    val promedio: Double      // promedio de las 3 notas
    val aprobo: Boolean       // true si promedio >= 3.0
    val estado: String        // "Aprobado" o "Reprobado"
}
```

- `NOTA_MINIMA_APROBACION = 3.0` define el umbral de aprobación
- Las propiedades `promedio`, `aprobo` y `estado` se calculan automáticamente (getters)

#### `EstadisticasCurso.kt`

**Ruta:** `app/src/main/java/com/example/calculadoranotas/model/EstadisticasCurso.kt`

```kotlin
data class EstadisticasCurso(
    val total: Int,
    val aprobados: Int,
    val reprobados: Int,
    val promedioGeneral: Double?,      // null si no hay estudiantes
    val mejorEstudiante: ResultadoAcademico?  // null si no hay estudiantes
)
```

#### `OrdenEstudiantes.kt`

**Ruta:** `app/src/main/java/com/example/calculadoranotas/model/OrdenEstudiantes.kt`

```kotlin
enum class OrdenEstudiantes {
    NOMBRE,
    PROMEDIO
}
```

---

### 5. `CursoRepositorio.kt` — Persistencia

**Ruta:** `app/src/main/java/com/example/calculadoranotas/data/CursoRepositorio.kt`

Guarda y carga estudiantes en **SharedPreferences** serializados como JSON.

| Función | Qué hace |
|---------|----------|
| `cargar()` | Lee JSON al iniciar; devuelve `emptyList()` si no hay datos o hay error |
| `guardar(estudiantes)` | Serializa la lista a JSON y la guarda |

**Formato JSON guardado:**
```json
[
  {
    "nombre": "Ana García",
    "notas": [4.5, 3.0, 5.0]
  },
  {
    "nombre": "Luis Pérez",
    "notas": [2.0, 2.5, 1.5]
  }
]
```

- Archivo de preferencias: `"curso_calificaciones"`
- Clave: `"estudiantes"`
- Si el JSON está corrupto, devuelve lista vacía (no crashea)

**Qué estudiar aquí:**
- Patrón Repository: abstrae el almacenamiento del ViewModel
- Serialización manual con `org.json.JSONArray` / `JSONObject`
- Manejo defensivo de errores con `try/catch`

---

### 6. Tema visual — `ui/theme/`

| Archivo | Qué hace |
|---------|----------|
| `Theme.kt` | `CalculadoraNotasTheme`: Material 3, modo oscuro, colores dinámicos (Android 12+) |
| `Color.kt` | Paleta púrpura/rosa para modo claro y oscuro |
| `Type.kt` | Tipografía base de Material 3 |

---

### 7. Recursos — `res/`

| Archivo | Qué hace |
|---------|----------|
| `strings.xml` | Todos los textos visibles en español |
| `colors.xml` | Colores del tema XML |
| `themes.xml` | Tema base de la actividad |
| `AndroidManifest.xml` | Declara la app, icono, `MainActivity` como LAUNCHER |

---

### 8. Tests — `CalculadoraNotasTest.kt`

**Ruta:** `app/src/test/java/com/example/calculadoranotas/CalculadoraNotasTest.kt`

Pruebas unitarias que cubren:

| Test | Qué verifica |
|------|--------------|
| `tresNotasValidasNoProducenError` | Validación básica OK |
| `estudianteApruebaConPromedioTres` | Aprobación con promedio = 3.0 |
| `estudianteRepruebaConPromedioInferiorATres` | Reprobación con promedio < 3.0 |
| `unaNotaMayorACincoProduceError` | Rango máximo 5.0 |
| `permiteUsarComaComoSeparadorDecimal` | `"4,5"` se convierte correctamente |
| `nombreDuplicadoProduceError` | No permite nombres repetidos |
| `nombreDuplicadoIgnoraMayusculas` | `"ana"` = `"Ana"` |
| `calculaEstadisticasDelCurso` | Total, aprobados, reprobados, promedio, mejor |
| `filtraEstudiantesPorNombre` | Búsqueda parcial funciona |
| `ordenaEstudiantesPorPromedio` | Orden descendente por promedio |

---

## Funcionalidades de la app

De arriba a abajo en la pantalla:

### A) Registro de estudiantes
- Campo **nombre** del estudiante
- Tres campos de **calificaciones** (0.0–5.0, acepta coma o punto)
- Botón **"Agregar estudiante"** → valida, calcula promedio y guarda
- Botón **"Limpiar formulario"** → vacía los campos sin afectar la lista

### B) Resumen del curso (`TarjetaResumen`)
Se actualiza en tiempo real al agregar o eliminar:
- Total de estudiantes
- Cantidad de aprobados y reprobados
- Promedio general del grupo
- Mejor estudiante (nombre + promedio)

### C) Búsqueda y ordenamiento
- **Buscar por nombre** → filtra la lista mientras escribes
- **Ordenar por Nombre** → alfabético A–Z
- **Ordenar por Promedio** → de mayor a menor

### D) Lista de estudiantes (`TarjetaEstudiante`)
Cada tarjeta muestra:
- Nombre del estudiante
- Las 3 calificaciones separadas por " – "
- Promedio destacado
- Estado con color: **verde** = Aprobado, **rojo** = Reprobado
- Botón **Eliminar**

### E) Persistencia automática
Los datos se guardan al agregar o eliminar y se cargan al abrir la app.

---

## Flujo de datos

### Al agregar un estudiante

```
Usuario completa formulario
        │
        ▼
  agregarEstudiante()          ← CursoViewModel
        │
        ▼
  validarRegistro()            ← CalculadoraNotas
        │
   ┌────┴────┐
   │ Error?  │
   └────┬────┘
    Sí  │  No
        │   │
        │   ▼
        │  calcular()           ← CalculadoraNotas
        │   │
        │   ▼
        │  estudiantes + nuevo  ← CursoViewModel
        │   │
        │   ▼
        │  guardar()            ← CursoRepositorio → SharedPreferences
        │   │
        │   ▼
        │  limpiarFormulario()
        │
        ▼
  mensajeError = "..."         ← Se muestra MensajeError en UI
```

### Al mostrar la lista

```
estudiantes (lista completa)
        │
        ▼
  filtrarPorNombre(consultaBusqueda)   ← CalculadoraNotas
        │
        ▼
  ordenar(orden)                        ← CalculadoraNotas
        │
        ▼
  estudiantesVisibles → UI (LazyColumn)
```

---

## Reglas de negocio

| Regla | Valor |
|-------|-------|
| Calificaciones por estudiante | Exactamente 3 |
| Rango de notas | 0.0 a 5.0 |
| Nota mínima para aprobar | Promedio ≥ 3.0 |
| Separador decimal | Punto o coma (`,`) |
| Nombres duplicados | No permitidos (sin distinguir mayúsculas) |
| Formato de nombre | Primera letra de cada palabra en mayúscula |

---

## Cómo ejecutar y probar

### Requisitos
- Android Studio (Ladybug o superior recomendado)
- JDK 11+
- Emulador Android o dispositivo físico (API 24+)

### Ejecutar la app
1. Abre el proyecto en Android Studio
2. Selecciona un emulador o dispositivo
3. Pulsa **Run** (▶)

### Ejecutar tests unitarios
```bash
./gradlew test
```

O desde Android Studio: clic derecho en `CalculadoraNotasTest.kt` → **Run Tests**.

### Tecnologías usadas

| Tecnología | Uso |
|------------|-----|
| Kotlin | Lenguaje principal |
| Jetpack Compose | UI declarativa |
| Material 3 | Diseño visual |
| ViewModel + Compose State | Gestión de estado |
| SharedPreferences | Persistencia local |
| JUnit | Tests unitarios |

---

## Orden sugerido para estudiar el código

1. **`model/`** — Entender las estructuras de datos (son simples)
2. **`CalculadoraNotas.kt`** — La lógica pura (sin Android)
3. **`CalculadoraNotasTest.kt`** — Ver cómo se prueba la lógica
4. **`CursoRepositorio.kt`** — Cómo se guardan los datos
5. **`CursoViewModel.kt`** — Cómo conecta todo
6. **`MainActivity.kt`** — La interfaz que consume el ViewModel
7. **`ui/theme/`** y **`strings.xml`** — Detalles visuales
