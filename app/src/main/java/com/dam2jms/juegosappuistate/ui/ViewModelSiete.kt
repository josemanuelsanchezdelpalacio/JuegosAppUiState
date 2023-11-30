package com.dam2jms.juegosappuistate.ui

import androidx.lifecycle.ViewModel
import com.dam2jms.juegosappuistate.states.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ViewModelSiete : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    // Propiedades para la banca
    private var apuestaBanca: Int = apuestaRandom()

    fun onJuego() {
        // Tanto el jugador como el PC obtienen una carta random y se les da el valor de esa carta
        val cartaJugador = cartas()
        val valorCartaJugador = valorCarta(cartaJugador)

        val cartaPC = cartas()
        val valorCartaPC = valorCarta(cartaPC)

        // Se van sumando cada vez que piden una carta
        val nuevaPuntuacionJugador = _uiState.value.valorCartasJugador + valorCartaJugador
        val nuevaPuntuacionPC = _uiState.value.valorCartasPC + valorCartaPC

        // Actualizo los valores de las cartas de cada jugador
        _uiState.update { currentState ->
            currentState.copy(
                valorCartasJugador = nuevaPuntuacionJugador,
                valorCartasPC = nuevaPuntuacionPC
            )
        }

        // Cuando uno de los dos llega a 7.5 ganan el doble de la apuesta
        if (nuevaPuntuacionPC == 7.5) {
            _uiState.update { currentState ->
                currentState.copy(
                    resultado = "Ganador PC. Gana el doble de la apuesta",
                    dineroGanadoJugador = apuestaBanca * 2
                )
            }
        } else if (nuevaPuntuacionJugador == 7.5) {
            _uiState.update { currentState ->
                currentState.copy(
                    resultado = "Ganaste. Ganas el doble de la apuesta",
                    dineroGanadoPC = apuestaBanca * 2
                )
            }
        }

        // Si se pasan, pagan a la banca el importe de la apuesta
        if (nuevaPuntuacionPC > 7.5) {
            _uiState.update { currentState ->
                currentState.copy(
                    resultado = "PC se pasó de 7.5. Paga",
                    dineroGanadoJugador = -apuestaBanca,
                    dineroGanadoPC = apuestaBanca * 2,
                    apuesta = 0
                )
            }
        } else if (nuevaPuntuacionJugador > 7.5) {
            _uiState.update { currentState ->
                currentState.copy(
                    resultado = "Te pasaste de 7.5. Pagas",
                    dineroGanadoJugador = apuestaBanca * 2,
                    dineroGanadoPC = -apuestaBanca,
                    apuesta = 0
                )
            }
        }

        // Si uno de los jugadores llega a 7.5 o se pasa, reiniciar el juego
        if (nuevaPuntuacionJugador >= 7.5 || nuevaPuntuacionPC >= 7.5) {
            reiniciarJuego()
        }
    }

    private fun reiniciarJuego() {
        _uiState.update { currentState ->
            currentState.copy(
                valorCartasJugador = 0.0,
                valorCartasPC = 0.0,
                resultado = "",
                apuesta = apuestaRandom()
            )
        }
    }


    fun onReset() {
        _uiState.update { currentState ->
            currentState.copy(
                valorCartasJugador = 0.0,
                valorCartasPC = 0.0
            )
        }
    }

    fun apuestaRandom(): Int {
        return (100..500).random()
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
