package com.example.calculadoranotas

import com.example.calculadoranotas.logic.CalculadoraNotas
import com.example.calculadoranotas.model.OrdenEstudiantes
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CalculadoraNotasTest {

    @Test
    fun tresNotasValidasNoProducenError() {
        val error = CalculadoraNotas.validar(
            nombre = "Ana",
            notasEscritas = listOf(
                "4.0",
                "3.5",
                "2.5"
            )
        )

        assertNull(error)
    }

    @Test
    fun estudianteApruebaConPromedioTres() {
        val resultado = CalculadoraNotas.calcular(
            nombre = "Carlos",
            notasEscritas = listOf(
                "3.0",
                "3.0",
                "3.0"
            )
        )

        assertTrue(resultado.aprobo)
        assertEquals(3.0, resultado.promedio, 0.001)
    }

    @Test
    fun estudianteRepruebaConPromedioInferiorATres() {
        val resultado = CalculadoraNotas.calcular(
            nombre = "Laura",
            notasEscritas = listOf(
                "2.0",
                "2.5",
                "2.9"
            )
        )

        assertFalse(resultado.aprobo)
    }

    @Test
    fun unaNotaMayorACincoProduceError() {
        val error = CalculadoraNotas.validar(
            nombre = "Pedro",
            notasEscritas = listOf(
                "5.5",
                "4.0",
                "3.0"
            )
        )

        assertEquals(
            "Las calificaciones deben estar entre 0.0 y 5.0.",
            error
        )
    }

    @Test
    fun permiteUsarComaComoSeparadorDecimal() {
        val resultado = CalculadoraNotas.calcular(
            nombre = "Marta",
            notasEscritas = listOf(
                "4,5",
                "3,5",
                "4,0"
            )
        )

        assertEquals(4.0, resultado.promedio, 0.001)
    }

    @Test
    fun nombreDuplicadoProduceError() {
        val error = CalculadoraNotas.validarRegistro(
            nombre = "Ana",
            notasEscritas = listOf(
                "4.0",
                "3.5",
                "4.0"
            ),
            nombresExistentes = listOf("Ana")
        )

        assertEquals(
            "Ya existe un estudiante con ese nombre.",
            error
        )
    }

    @Test
    fun nombreDuplicadoIgnoraMayusculas() {
        val error = CalculadoraNotas.validarRegistro(
            nombre = "  ana  ",
            notasEscritas = listOf(
                "4.0",
                "3.5",
                "4.0"
            ),
            nombresExistentes = listOf("Ana")
        )

        assertEquals(
            "Ya existe un estudiante con ese nombre.",
            error
        )
    }

    @Test
    fun calculaEstadisticasDelCurso() {
        val estudiantes = listOf(
            CalculadoraNotas.calcular(
                nombre = "Ana",
                notasEscritas = listOf("5.0", "5.0", "5.0")
            ),
            CalculadoraNotas.calcular(
                nombre = "Luis",
                notasEscritas = listOf("2.0", "2.0", "2.0")
            ),
            CalculadoraNotas.calcular(
                nombre = "Marta",
                notasEscritas = listOf("4.0", "4.0", "4.0")
            )
        )

        val estadisticas = CalculadoraNotas.calcularEstadisticas(estudiantes)

        assertEquals(3, estadisticas.total)
        assertEquals(2, estadisticas.aprobados)
        assertEquals(1, estadisticas.reprobados)
        assertEquals(3.666, estadisticas.promedioGeneral!!, 0.001)
        assertEquals("Ana", estadisticas.mejorEstudiante?.nombreEstudiante)
    }

    @Test
    fun filtraEstudiantesPorNombre() {
        val estudiantes = listOf(
            CalculadoraNotas.calcular(
                nombre = "Ana García",
                notasEscritas = listOf("4.0", "4.0", "4.0")
            ),
            CalculadoraNotas.calcular(
                nombre = "Luis Pérez",
                notasEscritas = listOf("3.0", "3.0", "3.0")
            )
        )

        val filtrados = CalculadoraNotas.filtrarPorNombre(
            estudiantes = estudiantes,
            consulta = "ana"
        )

        assertEquals(1, filtrados.size)
        assertEquals("Ana García", filtrados.first().nombreEstudiante)
    }

    @Test
    fun ordenaEstudiantesPorPromedio() {
        val estudiantes = listOf(
            CalculadoraNotas.calcular(
                nombre = "Ana",
                notasEscritas = listOf("3.0", "3.0", "3.0")
            ),
            CalculadoraNotas.calcular(
                nombre = "Luis",
                notasEscritas = listOf("5.0", "5.0", "5.0")
            )
        )

        val ordenados = CalculadoraNotas.ordenar(
            estudiantes = estudiantes,
            criterio = OrdenEstudiantes.PROMEDIO
        )

        assertEquals("Luis", ordenados.first().nombreEstudiante)
    }
}
