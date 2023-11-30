package com.dam2jms.juegosappuistate.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dam2jms.juegosappuistate.screens.mainScreen
import com.dam2jms.juegosappuistate.screens.nonesScreenState
import com.dam2jms.juegosappuistate.screens.piedraScreenState
import com.dam2jms.juegosappuistate.screens.sieteScreen
import com.dam2jms.juegosappuistate.ui.ViewModelNones
import com.dam2jms.juegosappuistate.ui.ViewModelPiedra
import com.dam2jms.juegosappuistate.ui.ViewModelSiete

@Composable
fun appNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppScreens.MainScreen.route) {
        composable(route = AppScreens.MainScreen.route) { mainScreen(navController) }
        composable(route = AppScreens.NonesScreen.route) { nonesScreenState(navController, mvvm = ViewModelNones()) }
        composable(route = AppScreens.PiedraScreen.route) { piedraScreenState(navController, mvvm = ViewModelPiedra()) }
        composable(route = AppScreens.SieteScreen.route) { sieteScreen(navController, mvvm = ViewModelSiete()) }
    }
}