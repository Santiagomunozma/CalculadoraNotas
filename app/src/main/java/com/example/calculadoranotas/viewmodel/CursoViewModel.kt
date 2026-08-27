package com.example.calculadoranotas.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.example.calculadoranotas.data.CursoRepositorio
import com.example.calculadoranotas.logic.CalculadoraNotas
import com.example.calculadoranotas.model.EstadisticasCurso
import com.example.calculadoranotas.model.OrdenEstudiantes
import com.example.calculadoranotas.model.ResultadoAcademico

class CursoViewModel(application: Application) : AndroidViewModel(application) {

    private val repositorio = CursoRepositorio(application)

    var nombre by mutableStateOf("")
        private set

    var nota1 by mutableStateOf("")
        private set

    var nota2 by mutableStateOf("")
        private set

    var nota3 by mutableStateOf("")
        private set

    var mensajeError by mutableStateOf<String?>(null)
        private set

    var consultaBusqueda by mutableStateOf("")
        private set

    var orden by mutableStateOf(OrdenEstudiantes.NOMBRE)
        private set

    var estudiantes by mutableStateOf(repositorio.cargar())
        private set

    val estadisticas: EstadisticasCurso
        get() = CalculadoraNotas.calcularEstadisticas(estudiantes)

    val estudiantesVisibles: List<ResultadoAcademico>
        get() {
            val filtrados = CalculadoraNotas.filtrarPorNombre(
                estudiantes = estudiantes,
                consulta = consultaBusqueda
            )

            return CalculadoraNotas.ordenar(
                estudiantes = filtrados,
                criterio = orden
            )
        }

    fun actualizarNombre(valor: String) {
        nombre = valor
        mensajeError = null
    }

    fun actualizarNota1(valor: String) {
        nota1 = valor
        mensajeError = null
    }

    fun actualizarNota2(valor: String) {
        nota2 = valor
        mensajeError = null
    }

    fun actualizarNota3(valor: String) {
        nota3 = valor
        mensajeError = null
    }

    fun actualizarBusqueda(valor: String) {
        consultaBusqueda = valor
    }

    fun actualizarOrden(criterio: OrdenEstudiantes) {
        orden = criterio
    }

    fun agregarEstudiante() {
        val notasEscritas = listOf(nota1, nota2, nota3)
        val nombresExistentes = estudiantes.map { it.nombreEstudiante }

        val error = CalculadoraNotas.validarRegistro(
            nombre = nombre,
            notasEscritas = notasEscritas,
            nombresExistentes = nombresExistentes
        )

        if (error != null) {
            mensajeError = error
            return
        }

        val nuevoEstudiante = CalculadoraNotas.calcular(
            nombre = nombre,
            notasEscritas = notasEscritas
        )

        estudiantes = estudiantes + nuevoEstudiante
        persistir()
        limpiarFormulario()
    }

    fun eliminarEstudiante(nombreEstudiante: String) {
        estudiantes = estudiantes.filterNot {
            it.nombreEstudiante.equals(nombreEstudiante, ignoreCase = true)
        }
        persistir()
    }

    fun limpiarFormulario() {
        nombre = ""
        nota1 = ""
        nota2 = ""
        nota3 = ""
        mensajeError = null
    }

    private fun persistir() {
        repositorio.guardar(estudiantes)
    }
}
