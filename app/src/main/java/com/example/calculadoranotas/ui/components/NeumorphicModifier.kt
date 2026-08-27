package com.example.calculadoranotas.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.neumorphicEffect(
    cornerRadius: Dp = 28.dp,
    elevation: Dp = 6.dp,
    isDarkTheme: Boolean = isSystemInDarkTheme()
): Modifier {
    // Definimos la paleta de colores según el tema
    val backgroundColor = if (isDarkTheme) Color(0xFF242830) else Color(0xFFE6E9EF)
    val lightShadowColor = if (isDarkTheme) Color(0xFF2C323D) else Color(0xFFFFFFFF)
    val darkShadowColor = if (isDarkTheme) Color(0xFF191C22) else Color(0xFFA3B1C6)

    return this.then(
        Modifier.drawBehind {
            val cornerRadiusPx = cornerRadius.toPx()
            val elevationPx = elevation.toPx()

            drawIntoCanvas { canvas ->
                // 1. Sombra Oscura (Abajo - Derecha)
                val darkPaint = Paint().apply {
                    val frameworkPaint = asFrameworkPaint()
                    frameworkPaint.color = darkShadowColor.toArgb()
                    frameworkPaint.setShadowLayer(
                        elevationPx * 2,
                        elevationPx,
                        elevationPx,
                        darkShadowColor.toArgb()
                    )
                }
                canvas.drawRoundRect(
                    0f, 0f, size.width, size.height,
                    cornerRadiusPx, cornerRadiusPx, darkPaint
                )

                // 2. Luz Clara (Arriba - Izquierda)
                val lightPaint = Paint().apply {
                    val frameworkPaint = asFrameworkPaint()
                    frameworkPaint.color = lightShadowColor.toArgb()
                    frameworkPaint.setShadowLayer(
                        elevationPx * 2,
                        -elevationPx,
                        -elevationPx,
                        lightShadowColor.toArgb()
                    )
                }
                canvas.drawRoundRect(
                    0f, 0f, size.width, size.height,
                    cornerRadiusPx, cornerRadiusPx, lightPaint
                )
            }
        }
    )
}