package com.example.who_is_that_pokemon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.who_is_that_pokemon.ui.Routes
import com.example.who_is_that_pokemon.ui.view.HomeView
import com.example.who_is_that_pokemon.ui.view.PokemonDetailsView
import com.example.who_is_that_pokemon.ui.viewmodel.HomeViewModel
import com.example.who_is_that_pokemon.ui.viewmodel.PokemonDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        val homeViewModel : HomeViewModel by viewModels()
        val pokemonDetailsViewModel : PokemonDetailsViewModel by viewModels()


        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = Routes.HomeView, builder = {

                composable (Routes.HomeView)
                {
                    HomeView(homeViewModel, navController)
                }

                composable (Routes.PokemonDetailsView + "/{name}")
                {
                    val name = it.arguments?.getString("name")

                    if (name != null)
                    {
                        pokemonDetailsViewModel.setCurrentPokemonName(name)
                        PokemonDetailsView(pokemonDetailsViewModel)
                    }
                }
            })
        }
    }
}

