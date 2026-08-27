package com.example.calculadoranotas.model

data class ResultadoAcademico(
    val nombreEstudiante: String,
    val notas: List<Double>
) {
    val promedio: Double
        get() = notas.average()

    val aprobo: Boolean
        get() = promedio >= NOTA_MINIMA_APROBACION

    val estado: String
        get() = if (aprobo) {
            "Aprobado"
        } else {
            "Reprobado"
        }

    companion object {
        const val NOTA_MINIMA_APROBACION = 3.0
    }
}