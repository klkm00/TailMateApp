package com.example.app.view

import androidx.compose.runtime.Composable
import androidx.navigation.Navtype
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument


@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppScreen.AnimalListScreen.route
){
    composable(route  = AppScreen.AnimalListScreen.route){
        homeScreen(navController = navController)
        
    }
}

}