package com.example.baseproject.view

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.baseproject.ui.screens.AppScreen
import com.example.baseproject.viewmodel.LoginViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val loginViewModel : LoginViewModel = viewModel()

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
                },
                onNavigateToRegister = {
                    navController.navigate("register_screen")
                }

            )
        }
        composable(route = "register_screen"){
            RegisterScreen(
                navController  = navController,
                viewModel = loginViewModel
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