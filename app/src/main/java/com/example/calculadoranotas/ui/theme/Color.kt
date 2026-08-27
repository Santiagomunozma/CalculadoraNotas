package com.example.calculadoranotas.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Paleta principal — tonos teal académicos
val TealPrimaryLight = Color(0xFF006874)
val TealOnPrimaryLight = Color(0xFFFFFFFF)
val TealPrimaryContainerLight = Color(0xFF9EEFFD)
val TealOnPrimaryContainerLight = Color(0xFF001F24)

val TealSecondaryLight = Color(0xFF4A6267)
val TealSecondaryContainerLight = Color(0xFFCDE7EC)
val TealOnSecondaryContainerLight = Color(0xFF051F23)

val TealBackgroundLight = Color(0xFFFAFDFD)
val TealSurfaceLight = Color(0xFFFAFDFD)
val TealSurfaceVariantLight = Color(0xFFDBE4E6)
val TealOnSurfaceLight = Color(0xFF191C1D)
val TealOnSurfaceVariantLight = Color(0xFF3F484A)
val TealOutlineLight = Color(0xFF6F797B)

val TealPrimaryDark = Color(0xFF82D3E0)
val TealOnPrimaryDark = Color(0xFF00363D)
val TealPrimaryContainerDark = Color(0xFF004F58)
val TealOnPrimaryContainerDark = Color(0xFF9EEFFD)

val TealSecondaryDark = Color(0xFFB1CBD0)
val TealSecondaryContainerDark = Color(0xFF334B4F)
val TealOnSecondaryContainerDark = Color(0xFFCDE7EC)

val TealBackgroundDark = Color(0xFF191C1D)
val TealSurfaceDark = Color(0xFF191C1D)
val TealSurfaceVariantDark = Color(0xFF3F484A)
val TealOnSurfaceDark = Color(0xFFE1E3E3)
val TealOnSurfaceVariantDark = Color(0xFFBFC8CA)
val TealOutlineDark = Color(0xFF899294)

// Estados académicos — aprobado / reprobado
val AprobadoContainerLight = Color(0xFFD1E7DD)
val AprobadoContentLight = Color(0xFF0F5132)

val ReprobadoContainerLight = Color(0xFFF8D7DA)
val ReprobadoContentLight = Color(0xFF842029)

val AprobadoContainerDark = Color(0xFF1B4332)
val AprobadoContentDark = Color(0xFF95D5B2)

val ReprobadoContainerDark = Color(0xFF3D1515)
val ReprobadoContentDark = Color(0xFFF5C2C7)

@Composable
fun estadoContainerColor(aprobo: Boolean): Color {
    val oscuro = isSystemInDarkTheme()
    return when {
        aprobo && oscuro -> AprobadoContainerDark
        aprobo -> AprobadoContainerLight
        oscuro -> ReprobadoContainerDark
        else -> ReprobadoContainerLight
    }
}

@Composable
fun estadoContentColor(aprobo: Boolean): Color {
    val oscuro = isSystemInDarkTheme()
    return when {
        aprobo && oscuro -> AprobadoContentDark
        aprobo -> AprobadoContentLight
        oscuro -> ReprobadoContentDark
        else -> ReprobadoContentLight
    }
}
