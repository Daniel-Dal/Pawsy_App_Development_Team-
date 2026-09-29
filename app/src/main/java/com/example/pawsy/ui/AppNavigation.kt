package com.example.pawsy.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(onFinished = {
                navController.navigate("menu") {
                    popUpTo("splash") { inclusive = true }
                }
            })
        }
        composable("menu") {
            LoginScreen(navController)
        }
        composable("createUser") {
            CreateUserScreen(navController)
        }
        composable("addDataUser") {
            DataUser(navController)
        }
        composable("addPet") {
            MyText(navController)
        }
        composable("myPets") {
            MyPetsScreen(navController)
        }
        composable("petProfile") {
            ProfileScreen(navController)
        }
        composable("userProfile") {
            MyProfileText(navController)
        }
        composable("pya") {
            RegistroPaseoActividad(navController)
        }
        composable("recom") {
            MyRecommText(navController)
        }

    }
}