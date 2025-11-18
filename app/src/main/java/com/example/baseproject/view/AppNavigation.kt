package com.example.baseproject.view

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.baseproject.ui.screens.AppScreen
import androidx.navigation.navArgument
import com.example.baseproject.ui.screens.WelcomeScreen


@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppScreen.AnimalListScreen.route
){
    composable(route  = AppScreen.AnimalListScreen.route){
        homeScreen (navController = navController)
        
    }
}

}