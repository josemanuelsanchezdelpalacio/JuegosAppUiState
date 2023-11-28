package com.dam2jms.juegosappuistate.states

data class NonesUiState(
    val puntuacionJugador: Int = 0,
    val puntuacionPC: Int = 0,
    val seleccionJugador: String = "",
    val numeroJugador: Int = 0,
    var resultado: String = ""
)
