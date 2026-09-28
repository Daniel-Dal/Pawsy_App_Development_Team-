package com.example.pawsy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pawsy.ui.screens.CreateUserScreen
import com.example.pawsy.ui.screens.DataUser
import com.example.pawsy.ui.theme.PawsyTheme
import com.example.pawsy.ui.screens.MyPetsScreen
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PawsyTheme {

                // Código original de Android Studio
                /*
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                */

                // Pantalla que estamos probando
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "createUser"
                ) {

                    composable("createUser") {
                        CreateUserScreen(navController)
                    }

                    composable("addDataUser") {
                        DataUser()
                    }
                }
            }
        }
    }
}
/*
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PawsyTheme {
        Greeting("Android")
    }
}
*/