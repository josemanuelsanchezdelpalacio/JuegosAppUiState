package com.dam2jms.juegosappuistate.states

data class UiState(
    var valorCartasJugador: Double = 0.0,
    var valorCartasPC: Double = 0.0,
    val apuesta: Int = 0,
    var dineroGanadoJugador: Int = 0,
    var dineroGanadoPC: Int = 0,

    val puntuacionJugador: Int = 0,
    val puntuacionPC: Int = 0,
    val seleccionJugador: String = "",
    val numeroJugador: Int = 0,
    var resultado: String = ""
)
