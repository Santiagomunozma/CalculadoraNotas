package com.example.calculadoranotas.data

import android.content.Context
import com.example.calculadoranotas.model.ResultadoAcademico
import org.json.JSONArray
import org.json.JSONObject

class CursoRepositorio(context: Context) {

    private val preferencias = context.applicationContext.getSharedPreferences(
        NOMBRE_PREFERENCIAS,
        Context.MODE_PRIVATE
    )

    fun cargar(): List<ResultadoAcademico> {
        val json = preferencias.getString(CLAVE_ESTUDIANTES, null)
            ?: return emptyList()

        return try {
            val arreglo = JSONArray(json)
            buildList {
                for (indice in 0 until arreglo.length()) {
                    val objeto = arreglo.getJSONObject(indice)
                    val notasJson = objeto.getJSONArray(CLAVE_NOTAS)
                    val notas = buildList {
                        for (indiceNota in 0 until notasJson.length()) {
                            add(notasJson.getDouble(indiceNota))
                        }
                    }

                    add(
                        ResultadoAcademico(
                            nombreEstudiante = objeto.getString(CLAVE_NOMBRE),
                            notas = notas
                        )
                    )
                }
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    fun guardar(estudiantes: List<ResultadoAcademico>) {
        val arreglo = JSONArray()

        estudiantes.forEach { estudiante ->
            val objeto = JSONObject()
            objeto.put(CLAVE_NOMBRE, estudiante.nombreEstudiante)

            val notasJson = JSONArray()
            estudiante.notas.forEach { nota ->
                notasJson.put(nota)
            }
            objeto.put(CLAVE_NOTAS, notasJson)

            arreglo.put(objeto)
        }

        preferencias.edit()
            .putString(CLAVE_ESTUDIANTES, arreglo.toString())
            .apply()
    }

    companion object {
        private const val NOMBRE_PREFERENCIAS = "curso_calificaciones"
        private const val CLAVE_ESTUDIANTES = "estudiantes"
        private const val CLAVE_NOMBRE = "nombre"
        private const val CLAVE_NOTAS = "notas"
    }
}
