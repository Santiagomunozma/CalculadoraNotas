package com.example.calculadoranotas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.calculadoranotas.logic.CalculadoraNotas
import com.example.calculadoranotas.model.EstadisticasCurso
import com.example.calculadoranotas.model.OrdenEstudiantes
import com.example.calculadoranotas.model.ResultadoAcademico
import com.example.calculadoranotas.ui.components.BotonNeumorfico
import com.example.calculadoranotas.ui.components.CampoTextoNeumorfico
import com.example.calculadoranotas.ui.components.TarjetaEstudianteNeumorfica
import com.example.calculadoranotas.ui.components.TarjetaResumenNeumorfica
import com.example.calculadoranotas.ui.theme.CalculadoraNotasTheme
import com.example.calculadoranotas.ui.theme.estadoContainerColor
import com.example.calculadoranotas.ui.theme.estadoContentColor
import com.example.calculadoranotas.viewmodel.CursoViewModel
import java.util.Locale
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

private object IndicesLista {
    const val NOMBRE = 1
    const val NOTA_1 = 2
    const val NOTA_2 = 3
    const val NOTA_3 = 4
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CalculadoraNotasTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    AplicacionCalculadoraNotas()
                }
            }
        }
    }
}

@Composable
fun AplicacionCalculadoraNotas(
    modifier: Modifier = Modifier,
    viewModel: CursoViewModel = viewModel()
) {
    val estadisticas = viewModel.estadisticas
    val estudiantesVisibles = viewModel.estudiantesVisibles
    val lazyListState = rememberLazyListState()
    val indiceBusqueda = if (viewModel.mensajeError != null) 8 else 7
    val isDarkTheme = isSystemInDarkTheme()
    val colorFondo = if (isDarkTheme) Color(0xFF1A202C) else Color(0xFFE6E9EF)

    Surface(
        modifier = modifier.fillMaxSize(),
        color = colorFondo
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent
        ) { innerPadding ->
        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding(),
            contentPadding = PaddingValues(
                start = 24.dp,
                end = 24.dp,
                top = 24.dp,
                bottom = 32.dp
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Encabezado()
            }
            item {
                Spacer(modifier = Modifier.height(20.dp))
                CampoTextoNeumorfico(
                    valor = viewModel.nombre,
                    etiqueta = stringResource(R.string.nombre),
                    onValueChange = viewModel::actualizarNombre,
                    imeAction = ImeAction.Next,
                    modifier = Modifier.centrarAlEnfocar(lazyListState, IndicesLista.NOMBRE)
                )
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))
                CampoTextoNeumorfico(
                    valor = viewModel.nota1,
                    etiqueta = stringResource(R.string.nota_1),
                    onValueChange = viewModel::actualizarNota1,
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next,
                    modifier = Modifier.centrarAlEnfocar(lazyListState, IndicesLista.NOTA_1)
                )
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))
                CampoTextoNeumorfico(
                    valor = viewModel.nota2,
                    etiqueta = stringResource(R.string.nota_2),
                    onValueChange = viewModel::actualizarNota2,
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next,
                    modifier = Modifier.centrarAlEnfocar(lazyListState, IndicesLista.NOTA_2)
                )
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))
                CampoTextoNeumorfico(
                    valor = viewModel.nota3,
                    etiqueta = stringResource(R.string.nota_3),
                    onValueChange = viewModel::actualizarNota3,
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Done,
                    modifier = Modifier.centrarAlEnfocar(lazyListState, IndicesLista.NOTA_3)
                )
            }

            viewModel.mensajeError?.let { mensaje ->
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    MensajeError(mensaje)
                }
            }

          item {
                Spacer(modifier = Modifier.height(20.dp))
                BotonNeumorfico(
                    texto = stringResource(R.string.agregar_estudiante),
                    onClick = viewModel::agregarEstudiante
                )

                Spacer(modifier = Modifier.height(12.dp))

                BotonNeumorfico(
                    texto = stringResource(R.string.limpiar_formulario),
                    onClick = viewModel::limpiarFormulario,
                    esSecundario = true
                )
            }


            item {
                Spacer(modifier = Modifier.height(20.dp))
                TarjetaResumenNeumorfica(estadisticas = estadisticas)
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))

                CampoTextoNeumorfico(
                    valor = viewModel.consultaBusqueda,
                    etiqueta = stringResource(R.string.buscar_estudiante),
                    onValueChange = viewModel::actualizarBusqueda,
                    imeAction = ImeAction.Search,
                    modifier = Modifier.centrarAlEnfocar(lazyListState, indiceBusqueda)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = stringResource(R.string.ordenar_por),
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = viewModel.orden == OrdenEstudiantes.NOMBRE,
                        onClick = {
                            viewModel.actualizarOrden(OrdenEstudiantes.NOMBRE)
                        },
                        label = {
                            Text(stringResource(R.string.ordenar_nombre))
                        }
                    )

                    FilterChip(
                        selected = viewModel.orden == OrdenEstudiantes.PROMEDIO,
                        onClick = {
                            viewModel.actualizarOrden(OrdenEstudiantes.PROMEDIO)
                        },
                        label = {
                            Text(stringResource(R.string.ordenar_promedio))
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.lista_estudiantes),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            if (viewModel.estudiantes.isEmpty()) {
                item {
                    TextoListaVacia(
                        mensaje = stringResource(R.string.lista_vacia)
                    )
                }
            } else if (estudiantesVisibles.isEmpty()) {
                item {
                    TextoListaVacia(
                        mensaje = stringResource(R.string.sin_coincidencias)
                    )
                }
            } else {
                items(
                    items = estudiantesVisibles,
                    key = { it.nombreEstudiante }
                ) { estudiante ->
                    TarjetaEstudianteNeumorfica(
                        resultado = estudiante,
                        onEliminar = {
                            viewModel.eliminarEstudiante(
                                estudiante.nombreEstudiante
                            )
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
        }
    }
}

@Composable
private fun Modifier.centrarAlEnfocar(
    lazyListState: LazyListState,
    indiceItem: Int
): Modifier {
    val scope = rememberCoroutineScope()

    return onFocusEvent { focusState ->
        if (focusState.isFocused) {
            scope.launch {
                kotlinx.coroutines.delay(150)
                lazyListState.centrarItem(indiceItem)
            }
        }
    }
}

private suspend fun LazyListState.centrarItem(indice: Int) {
    scrollToItem(indice)

    snapshotFlow { layoutInfo.visibleItemsInfo }
        .filter { items -> items.any { it.index == indice } }
        .first()

    val item = layoutInfo.visibleItemsInfo.find { it.index == indice } ?: return
    val centroViewport = layoutInfo.viewportSize.height / 2
    val centroItem = item.offset + item.size / 2
    val desplazamiento = centroItem - centroViewport

    if (desplazamiento != 0) {
        this.animateScrollBy(desplazamiento.toFloat())
    }
}

@Composable
private fun Encabezado() {
    Text(
        text = stringResource(R.string.titulo),
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(6.dp))

    Text(
        text = stringResource(R.string.descripcion),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun CampoNota(
    valor: String,
    etiqueta: String,
    onValueChange: (String) -> Unit,
    imeAction: ImeAction,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValueChange,
        label = {
            Text(etiqueta)
        },
        supportingText = {
            Text(stringResource(R.string.rango_notas))
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal,
            imeAction = imeAction
        ),
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
private fun MensajeError(mensaje: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Text(
            text = mensaje,
            color = MaterialTheme.colorScheme.onErrorContainer,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
private fun TarjetaResumen(
    estadisticas: EstadisticasCurso
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = stringResource(R.string.resumen_curso),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            FilaInformacion(
                etiqueta = stringResource(R.string.total_estudiantes),
                valor = estadisticas.total.toString()
            )

            Spacer(modifier = Modifier.height(8.dp))

            FilaInformacion(
                etiqueta = stringResource(R.string.aprobados),
                valor = estadisticas.aprobados.toString()
            )

            Spacer(modifier = Modifier.height(8.dp))

            FilaInformacion(
                etiqueta = stringResource(R.string.reprobados),
                valor = estadisticas.reprobados.toString()
            )

            Spacer(modifier = Modifier.height(8.dp))

            FilaInformacion(
                etiqueta = stringResource(R.string.promedio_grupo),
                valor = estadisticas.promedioGeneral?.let { promedio ->
                    formatearNumero(promedio)
                } ?: stringResource(R.string.sin_datos)
            )

            Spacer(modifier = Modifier.height(8.dp))

            FilaInformacion(
                etiqueta = stringResource(R.string.mejor_estudiante),
                valor = estadisticas.mejorEstudiante?.let { mejor ->
                    "${mejor.nombreEstudiante} (${formatearNumero(mejor.promedio)})"
                } ?: stringResource(R.string.sin_datos)
            )
        }
    }
}

@Composable
private fun TarjetaEstudiante(
    resultado: ResultadoAcademico,
    onEliminar: () -> Unit
) {
    val colorEstado = estadoContentColor(resultado.aprobo)
    val colorFondo = estadoContainerColor(resultado.aprobo)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorFondo
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = resultado.nombreEstudiante,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            FilaInformacion(
                etiqueta = stringResource(R.string.calificaciones),
                valor = resultado.notas.joinToString(
                    separator = " – "
                ) { nota ->
                    formatearNumero(nota)
                }
            )

            Spacer(modifier = Modifier.height(14.dp))

            HorizontalDivider()

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.promedio),
                        style = MaterialTheme.typography.labelLarge
                    )

                    Text(
                        text = formatearNumero(resultado.promedio),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(
                                color = colorEstado,
                                shape = CircleShape
                            )
                    )

                    Text(
                        text = resultado.estado,
                        color = colorEstado,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onEliminar,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.eliminar_estudiante))
            }
        }
    }
}

@Composable
private fun TextoListaVacia(mensaje: String) {
    Text(
        text = mensaje,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    )
}

@Composable
private fun FilaInformacion(
    etiqueta: String,
    valor: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = etiqueta,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = valor,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(2f)
        )
    }
}

private fun formatearNumero(numero: Double): String {
    return String.format(
        Locale.getDefault(),
        "%.2f",
        numero
    )
}

@Preview(showBackground = true)
@Composable
private fun AplicacionPreview() {
    CalculadoraNotasTheme {
        val estudianteAprobado = CalculadoraNotas.calcular(
            nombre = "Ana",
            notasEscritas = listOf("4.5", "4.0", "5.0")
        )
        val estudianteReprobado = CalculadoraNotas.calcular(
            nombre = "Luis",
            notasEscritas = listOf("2.0", "2.5", "1.5")
        )

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Encabezado()
            Spacer(modifier = Modifier.height(16.dp))
            TarjetaResumen(
                estadisticas = CalculadoraNotas.calcularEstadisticas(
                    listOf(estudianteAprobado, estudianteReprobado)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            TarjetaEstudiante(
                resultado = estudianteAprobado,
                onEliminar = {}
            )
            Spacer(modifier = Modifier.height(12.dp))
            TarjetaEstudiante(
                resultado = estudianteReprobado,
                onEliminar = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AplicacionPreviewOscuro() {
    CalculadoraNotasTheme(darkTheme = true) {
        val estudianteAprobado = CalculadoraNotas.calcular(
            nombre = "Ana",
            notasEscritas = listOf("4.5", "4.0", "5.0")
        )
        val estudianteReprobado = CalculadoraNotas.calcular(
            nombre = "Luis",
            notasEscritas = listOf("2.0", "2.5", "1.5")
        )

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            TarjetaEstudiante(
                resultado = estudianteAprobado,
                onEliminar = {}
            )
            Spacer(modifier = Modifier.height(12.dp))
            TarjetaEstudiante(
                resultado = estudianteReprobado,
                onEliminar = {}
            )
        }
    }
}