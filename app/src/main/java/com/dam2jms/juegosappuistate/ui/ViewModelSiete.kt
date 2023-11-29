package com.dam2jms.juegosappuistate.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dam2jms.juegosappuistate.states.NonesUiState

class ViewModelSiete : ViewModel() {

    private val _uiState = MutableLiveData<NonesUiState>()
    val uiState: LiveData<NonesUiState> = _uiState

    fun onJuego() {
        // Tanto el jugador como el PC obtienen una carta random y se les da el valor de esa carta
        val cartaJugador = cartas()
        val valorCartaJugador = valorCarta(cartaJugador)

        val cartaPC = cartas()
        val valorCartaPC = valorCarta(cartaPC)

        // Se van sumando cada vez que pida una carta
        val currentState = _uiState.value
        if (currentState != null) {
            val nuevaPuntuacionJugador = currentState.puntuacionJugador + valorCartaJugador
            val nuevaPuntuacionPC = currentState.puntuacionPC + valorCartaPC

            if (nuevaPuntuacionPC > 7.5) {
                _uiState.value = currentState.copy(
                    resultado = "Has ganado, el PC se ha pasado de 7.5",
                    puntuacionJugador = nuevaPuntuacionJugador,
                    puntuacionPC = nuevaPuntuacionPC
                )
            } else if (nuevaPuntuacionPC > nuevaPuntuacionJugador) {
                _uiState.value = currentState.copy(
                    resultado = "Has perdido, el PC tiene más puntos",
                    puntuacionJugador = nuevaPuntuacionJugador,
                    puntuacionPC = nuevaPuntuacionPC
                )
            }
        }
    }

    private fun cartas(): String {
        val cartas = listOf("As", "2", "3", "4", "5", "6", "7", "Sota", "Caballo", "Rey")
        return cartas.random()
    }

    private fun valorCarta(carta: String): Double {
        return when (carta) {
            "As" -> 1.0
            "2" -> 2.0
            "3" -> 3.0
            "4" -> 4.0
            "5" -> 5.0
            "6" -> 6.0
            "7" -> 7.0
            else -> 0.5
        }
    }
}
