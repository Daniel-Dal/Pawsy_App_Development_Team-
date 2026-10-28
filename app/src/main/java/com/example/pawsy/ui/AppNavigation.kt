package com.example.pawsy.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

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
        composable(
            route = "petProfile/{mascotaId}",
            arguments = listOf(navArgument("mascotaId") { type = NavType.StringType })
        ) { backStackEntry ->
            val mascotaId = backStackEntry.arguments?.getString("mascotaId")
            ProfileScreen(
                navController = navController,
                mascotaId = mascotaId
            )
        }
        composable("userProfile") {
            MyProfileText(navController)
        }
        composable("userData") {
            UserDataScreen(navController)
        }
        composable("pya") {
            RegistroPaseoActividad(navController)
        }
        composable("recom") {
            MyRecommText(navController)
        }
        composable("VerifyEmail") {
            VerifyEmailScreen(navController = navController)
        }

        composable(
            route = "historias/{mascotaId}",
            arguments = listOf(navArgument("mascotaId") { type = NavType.StringType })
        ) { backStackEntry ->
            MedicalRecordsScreen(
                navController = navController,
                mascotaId = backStackEntry.arguments?.getString("mascotaId")
            )
        }
        composable("nearbyVets") {
            NearbyVetsScreen(navController)
        }

    }
}