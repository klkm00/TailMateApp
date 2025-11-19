package com.example.baseproject.ui.screens

sealed class AppScreen(val route : String) {
    object RegisterScreen : AppScreen("register_screen")
    object Welcome : AppScreen("Welcome_screen")
    object LoginScreen : AppScreen("login_screen")
    object AnimalListScreen : AppScreen("animal_list_screen")
    object AnimalDetailScreen : AppScreen("animal_detail_screen/{animalId}") {
        fun createRoute(animalId: String) = "animal_detail_screen/$animalId"

    }
}