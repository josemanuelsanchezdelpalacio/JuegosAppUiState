package com.dam2jms.juegosappuistate.states

data class NonesUiState(
    val puntuacionJugador: Double = 0.0,
    val puntuacionPC: Double = 0.0,
    val seleccionJugador: String = "",
    val numeroJugador: Int = 0,
    var resultado: String = ""
)
