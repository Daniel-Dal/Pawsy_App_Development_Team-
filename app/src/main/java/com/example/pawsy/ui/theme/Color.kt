package com.example.pawsy.ui.theme

import androidx.compose.ui.graphics.Color



data class PawsyColors(
    val fondoPerfil: Color,
    val fondoInicioApp: Color,
    val letraInicioApp: Color,
    val letraInicioNombreApp: Color,
    val blanco: Color,
    val negro: Color,
    val recuadroInicioSesion: Color,
    val editTextInicioSesion: Color,
    val textEditTextInicioSesion: Color,
    val fondoSeleccionMascotaApp: Color,
    val fondoAgregarMascota: Color,
    val editTextAgregarMascota: Color,
    val textEditTextAgregarMascota: Color,
    val fondoPerfilUsuario: Color
)

val LightPawsyColors = PawsyColors(
    fondoPerfil = Color(0xFFFFF9F0),
    fondoInicioApp = Color(0xFFFFF9F0),
    letraInicioApp = Color(0xFFFE854F),
    letraInicioNombreApp = Color(0xFF002D57),
    blanco = Color(0xFFFFFFFF),
    negro = Color(0xFF000000),
    recuadroInicioSesion = Color(0xFFFF6F61),
    editTextInicioSesion = Color(0xFFEDBEA4),
    textEditTextInicioSesion = Color(0xFFC05C2E),
    fondoSeleccionMascotaApp = Color(0xFFFF6F61),
    fondoAgregarMascota = Color(0xFFEDBEA4),
    editTextAgregarMascota = Color(0xFFFE854F),
    textEditTextAgregarMascota = Color(0xFFC05C2E),
    fondoPerfilUsuario = Color(0xFFFFF9F0)
)

val DarkPawsyColors = PawsyColors(
    fondoPerfil = Color(0xFF1E1B18),
    fondoInicioApp = Color(0xFF1E1B18),

    letraInicioApp = Color(0xFF7FB3E8),
    letraInicioNombreApp = Color(0xFFB8D7F5),

    blanco = Color(0xFFF5F1EC),
    negro = Color(0xFFE6E1E5),

    recuadroInicioSesion = Color(0xFF222A58),
    editTextInicioSesion = Color(0xFF222A58),
    textEditTextInicioSesion = Color(0xFF7FB3E8),

    fondoSeleccionMascotaApp = Color(0xFF222A58),

    fondoAgregarMascota = Color(0xFF374EA2),
    editTextAgregarMascota = Color(0xFF7FB3E8),
    textEditTextAgregarMascota = Color(0xFFB8D7F5),

    fondoPerfilUsuario = Color(0xFF1E1B18)
)

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val colorPataSeleccionMascota = Color(0xFF7FCCD7)
val Pink40 = Color(0xFF7D5260)