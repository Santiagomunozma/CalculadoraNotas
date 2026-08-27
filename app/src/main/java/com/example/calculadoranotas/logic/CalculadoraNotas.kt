package com.example.calculadoranotas.logic

import com.example.calculadoranotas.model.EstadisticasCurso
import com.example.calculadoranotas.model.OrdenEstudiantes
import com.example.calculadoranotas.model.ResultadoAcademico

object CalculadoraNotas {

    const val CANTIDAD_NOTAS = 3
    const val NOTA_MINIMA = 0.0
    const val NOTA_MAXIMA = 5.0

    fun convertirNota(texto: String): Double? {
        val textoNormalizado = texto
            .trim()
            .replace(',', '.')

        return textoNormalizado.toDoubleOrNull()
    }

    fun validar(
        nombre: String,
        notasEscritas: List<String>
    ): String? {
        if (nombre.isBlank()) {
            return "Debe ingresar el nombre del estudiante."
        }

        if (notasEscritas.size != CANTIDAD_NOTAS) {
            return "Se requieren exactamente tres calificaciones."
        }

        if (notasEscritas.any { it.isBlank() }) {
            return "Debe ingresar las tres calificaciones."
        }

        val notasConvertidas = notasEscritas.map {
            convertirNota(it)
        }

        if (notasConvertidas.any { it == null }) {
            return "Las calificaciones deben ser valores numéricos."
        }

        val existeNotaFueraDelRango = notasConvertidas.any { nota ->
            nota != null && nota !in NOTA_MINIMA..NOTA_MAXIMA
        }

        if (existeNotaFueraDelRango) {
            return "Las calificaciones deben estar entre 0.0 y 5.0."
        }

        return null
    }

    fun validarRegistro(
        nombre: String,
        notasEscritas: List<String>,
        nombresExistentes: List<String>
    ): String? {
        val errorValidacion = validar(
            nombre = nombre,
            notasEscritas = notasEscritas
        )

        if (errorValidacion != null) {
            return errorValidacion
        }

        val nombreNormalizado = formatearNombre(nombre)
        val nombreDuplicado = nombresExistentes.any { existente ->
            formatearNombre(existente).equals(
                nombreNormalizado,
                ignoreCase = true
            )
        }

        if (nombreDuplicado) {
            return "Ya existe un estudiante con ese nombre."
        }

        return null
    }

    fun calcularEstadisticas(
        estudiantes: List<ResultadoAcademico>
    ): EstadisticasCurso {
        if (estudiantes.isEmpty()) {
            return EstadisticasCurso(
                total = 0,
                aprobados = 0,
                reprobados = 0,
                promedioGeneral = null,
                mejorEstudiante = null
            )
        }

        val aprobados = estudiantes.count { it.aprobo }

        return EstadisticasCurso(
            total = estudiantes.size,
            aprobados = aprobados,
            reprobados = estudiantes.size - aprobados,
            promedioGeneral = estudiantes.map { it.promedio }.average(),
            mejorEstudiante = estudiantes.maxBy { it.promedio }
        )
    }

    fun filtrarPorNombre(
        estudiantes: List<ResultadoAcademico>,
        consulta: String
    ): List<ResultadoAcademico> {
        val consultaNormalizada = consulta.trim()

        if (consultaNormalizada.isEmpty()) {
            return estudiantes
        }

        return estudiantes.filter { estudiante ->
            estudiante.nombreEstudiante.contains(
                consultaNormalizada,
                ignoreCase = true
            )
        }
    }

    fun ordenar(
        estudiantes: List<ResultadoAcademico>,
        criterio: OrdenEstudiantes
    ): List<ResultadoAcademico> {
        return when (criterio) {
            OrdenEstudiantes.NOMBRE -> estudiantes.sortedBy {
                it.nombreEstudiante.lowercase()
            }

            OrdenEstudiantes.PROMEDIO -> estudiantes.sortedByDescending {
                it.promedio
            }
        }
    }

    fun calcular(
        nombre: String,
        notasEscritas: List<String>
    ): ResultadoAcademico {
        val notas = notasEscritas.map { texto ->
            convertirNota(texto) ?: 0.0
        }

        return ResultadoAcademico(
            nombreEstudiante = formatearNombre(nombre),
            notas = notas
        )
    }

    private fun formatearNombre(nombre: String): String {
        return nombre
            .trim()
            .lowercase()
            .split(" ")
            .filter { palabra -> palabra.isNotBlank() }
            .joinToString(" ") { palabra ->
                palabra.replaceFirstChar { caracter ->
                    caracter.uppercase()
                }
            }
    }
}