package com.example.baseproject.view

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
    }
}

@Composable
fun AnimalListScreen(navController: NavHostController) {
    Text("Animal List Screen")
}