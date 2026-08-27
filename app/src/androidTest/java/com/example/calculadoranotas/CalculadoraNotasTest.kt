package com.example.calculadoranotas

import com.example.calculadoranotas.logic.CalculadoraNotas
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
}