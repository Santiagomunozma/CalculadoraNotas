package com.example.calculadoranotas.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.calculadoranotas.R
import com.example.calculadoranotas.model.EstadisticasCurso
import com.example.calculadoranotas.model.ResultadoAcademico
import com.example.calculadoranotas.ui.theme.estadoContentColor

@Composable
fun TarjetaEstudianteNeumorfica(
    resultado: ResultadoAcademico,
    onEliminar: () -> Unit,
    isDarkTheme: Boolean = isSystemInDarkTheme()
) {
    val colorEstado = estadoContentColor(resultado.aprobo)
    val colorFondoBase = if (isDarkTheme) Color(0xFF242830) else Color(0xFFE6E9EF)
    val colorTextoTitulo = if (isDarkTheme) Color(0xFFE2E8F0) else Color(0xFF2D3748)
    val colorTextoSubtitulo = if (isDarkTheme) Color(0xFFA0AEC0) else Color(0xFF718096)
    val shape3D = RoundedCornerShape(28.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .neumorphicEffect(
                cornerRadius = 28.dp,
                elevation = 6.dp,
                isDarkTheme = isDarkTheme
            )
            .background(color = colorFondoBase, shape = shape3D)
            .padding(20.dp)
    ) {
        Column {
            Text(
                text = resultado.nombreEstudiante,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = colorTextoTitulo
            )

            Spacer(modifier = Modifier.height(8.dp))

            FilaInformacion(
                etiqueta = stringResource(R.string.calificaciones),
                valor = resultado.notas.joinToString(" – ") { String.format("%.2f", it) },
                isDarkTheme = isDarkTheme
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.promedio),
                        style = MaterialTheme.typography.labelLarge,
                        color = colorTextoSubtitulo
                    )

                    Text(
                        text = String.format("%.2f", resultado.promedio),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = colorTextoTitulo
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(color = colorEstado, shape = CircleShape)
                    )

                    Text(
                        text = resultado.estado,
                        color = colorEstado,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = onEliminar,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDarkTheme) Color(0xFF2D3748) else Color(0xFFDDE3EA),
                    contentColor = Color(0xFFE53E3E)
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.eliminar_estudiante),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun CampoTextoNeumorfico(
    valor: String,
    etiqueta: String,
    onValueChange: (String) -> Unit,
    imeAction: ImeAction,
    keyboardType: KeyboardType = KeyboardType.Text,
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean = isSystemInDarkTheme()
) {
    val colorFondo = if (isDarkTheme) Color(0xFF242830) else Color(0xFFE6E9EF)
    val colorTexto = if (isDarkTheme) Color(0xFFE2E8F0) else Color(0xFF2D3748)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .neumorphicEffect(
                cornerRadius = 20.dp,
                elevation = 2.dp,
                isDarkTheme = isDarkTheme
            )
            .background(colorFondo, shape = RoundedCornerShape(20.dp))
            .padding(horizontal = 16.dp, vertical = 2.dp)
    ) {
        TextField(
            value = valor,
            onValueChange = onValueChange,
            placeholder = { Text(etiqueta, color = Color(0xFFA0AEC0)) },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedTextColor = colorTexto,
                unfocusedTextColor = colorTexto,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun BotonNeumorfico(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    esSecundario: Boolean = false,
    isDarkTheme: Boolean = isSystemInDarkTheme()
) {
    val colorFondo = if (esSecundario) {
        if (isDarkTheme) Color(0xFF2D3748) else Color(0xFFE6E9EF)
    } else {
        Color(0xFF2B6CB0)
    }
    val colorTexto = if (esSecundario) {
        if (isDarkTheme) Color(0xFFE2E8F0) else Color(0xFF4A5568)
    } else {
        Color.White
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .neumorphicEffect(
                cornerRadius = 50.dp,
                elevation = 4.dp,
                isDarkTheme = isDarkTheme
            )
            .background(colorFondo, shape = RoundedCornerShape(50.dp))
    ) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = colorTexto
            ),
            elevation = null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = texto,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}

@Composable
fun TarjetaResumenNeumorfica(
    estadisticas: EstadisticasCurso,
    isDarkTheme: Boolean = isSystemInDarkTheme()
) {
    val colorFondo = if (isDarkTheme) Color(0xFF242830) else Color(0xFFE6E9EF)
    val colorTextoTitulo = if (isDarkTheme) Color(0xFFE2E8F0) else Color(0xFF2D3748)
    val shape3D = RoundedCornerShape(28.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .neumorphicEffect(
                cornerRadius = 28.dp,
                elevation = 6.dp,
                isDarkTheme = isDarkTheme
            )
            .background(color = colorFondo, shape = shape3D)
            .padding(20.dp)
    ) {
        Column {
            Text(
                text = stringResource(R.string.resumen_curso),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = colorTextoTitulo
            )

            Spacer(modifier = Modifier.height(12.dp))

            FilaInformacion(
                etiqueta = stringResource(R.string.total_estudiantes),
                valor = estadisticas.total.toString(),
                isDarkTheme = isDarkTheme
            )
            Spacer(modifier = Modifier.height(8.dp))

            FilaInformacion(
                etiqueta = stringResource(R.string.aprobados),
                valor = estadisticas.aprobados.toString(),
                isDarkTheme = isDarkTheme
            )
            Spacer(modifier = Modifier.height(8.dp))

            FilaInformacion(
                etiqueta = stringResource(R.string.reprobados),
                valor = estadisticas.reprobados.toString(),
                isDarkTheme = isDarkTheme
            )
            Spacer(modifier = Modifier.height(8.dp))

            FilaInformacion(
                etiqueta = stringResource(R.string.promedio_grupo),
                valor = estadisticas.promedioGeneral?.let { String.format("%.2f", it) }
                    ?: stringResource(R.string.sin_datos),
                isDarkTheme = isDarkTheme
            )
            Spacer(modifier = Modifier.height(8.dp))

            FilaInformacion(
                etiqueta = stringResource(R.string.mejor_estudiante),
                valor = estadisticas.mejorEstudiante?.let { mejor ->
                    "${mejor.nombreEstudiante} (${String.format("%.2f", mejor.promedio)})"
                } ?: stringResource(R.string.sin_datos),
                isDarkTheme = isDarkTheme
            )
        }
    }
}

@Composable
fun FilaInformacion(
    etiqueta: String,
    valor: String,
    isDarkTheme: Boolean = isSystemInDarkTheme()
) {
    val colorEtiqueta = if (isDarkTheme) Color(0xFFA0AEC0) else Color(0xFF4A5568)
    val colorValor = if (isDarkTheme) Color(0xFFE2E8F0) else Color(0xFF2D3748)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = etiqueta,
            fontWeight = FontWeight.SemiBold,
            color = colorEtiqueta,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = valor,
            textAlign = TextAlign.End,
            color = colorValor,
            modifier = Modifier.weight(2f)
        )
    }
}