package com.example.calculadoranotas.model

data class EstadisticasCurso(
    val total: Int,
    val aprobados: Int,
    val reprobados: Int,
    val promedioGeneral: Double?,
    val mejorEstudiante: ResultadoAcademico?
)
