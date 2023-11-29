package com.dam2jms.juegosappuistate.ui

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dam2jms.juegosappuistate.states.NonesUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ViewModelPiedraState : ViewModel() {

    private val _uiState = MutableStateFlow(NonesUiState())
    val uiState: StateFlow<NonesUiState> = _uiState.asStateFlow()

    fun onSeleccionJugador(seleccionJugadorUi: String) {
        _uiState.update {
                currentState -> currentState.copy(seleccionJugador = seleccionJugadorUi)
        }
    }

    fun onJuego(context: Context) {
        //llamo al metodo eleccionAleatoriaPC y lo guardo en su variable
        val seleccionPC = eleccionAleatoriaPC()

        //compruebo que la seleccion del jugador solo pueda ser pidra, papel o tijeras
        if (_uiState.value?.seleccionJugador != "piedra" || _uiState.value?.seleccionJugador != "papel" || _uiState.value?.seleccionJugador != "tijeras") {

            //comparo la seleccion del jugador y la del PC y si son iguales el resultado es "nadie"
            if (_uiState.value?.seleccionJugador.equals(seleccionPC)) {
                _uiState.value?.resultado = "nadie"
            } else if (
            //comparaciones para comprobar si gana el jugador
                (_uiState.value?.seleccionJugador.equals("piedra") && seleccionPC.equals("tijeras")) ||
                (_uiState.value?.seleccionJugador.equals("papel") && seleccionPC.equals("piedra")) ||
                (_uiState.value?.seleccionJugador.equals("tijeras") && seleccionPC.equals("papel"))
            ) {
                _uiState.value?.resultado = "Jugador"
                _uiState.update { currentState ->
                    currentState.copy(puntuacionJugador = currentState.puntuacionJugador.inc())
                }
            } else {
                //si no es ninguna de las comparaciones anteriores gana el PC
                _uiState.value?.resultado = "PC"
                _uiState.update { currentState ->
                    currentState.copy(puntuacionJugador = currentState.puntuacionJugador.dec())
                }
            }

            //muestro el resultado
            _uiState.value.resultado = "Jugador: ${_uiState.value.seleccionJugador}\n PC: $seleccionPC.\n Resultado: ${_uiState.value.resultado}"
        } else {
            Toast.makeText(context, "Solo puedes elejir entre piedra, papel o tijeras", Toast.LENGTH_SHORT).show()
        }
    }

    //metodo con una lista con las opciones de piedra, papel, tijeras que la randomiza
    fun eleccionAleatoriaPC(): String {
        val opciones = listOf("piedra", "papel", "tijeras")
        return opciones.random()
    }
}