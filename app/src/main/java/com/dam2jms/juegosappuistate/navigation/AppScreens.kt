package com.dam2jms.juegosappuistate.navigation

sealed class AppScreens (val route: String){
    object MainScreen: AppScreens(route = "main_screen")
    object NonesScreen: AppScreens(route = "nones_screen")
    object PiedraScreen: AppScreens(route = "piedra_screen")
    object SieteScreen: AppScreens(route = "siete_screen")
}
