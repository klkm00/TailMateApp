package com.example.baseproject.view

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.baseproject.ui.screens.AppScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppScreen.LoginScreen.route
    ) {
        composable(route = AppScreen.LoginScreen.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(AppScreen.AnimalListScreen.route) {
                        popUpTo(AppScreen.LoginScreen.route) {
                            inclusive = true
                        }

                    }
                }

            )
        }
        composable(route = AppScreen.AnimalListScreen.route) {
            AnimalListScreen(navController = navController)
        }
        composable(
            route = AppScreen.AnimalDetailScreen.route,
            arguments = listOf(navArgument("animalId") { type = NavType.StringType })
        ) { backStackEntry ->
            val animalId = backStackEntry.arguments?.getString("animalId")
            if (animalId != null) {
                AnimalDetailScreen(animalId = animalId.toInt())
            }
        }
    }
}