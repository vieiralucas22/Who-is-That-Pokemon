package com.example.who_is_that_pokemon.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.who_is_that_pokemon.R
import com.example.who_is_that_pokemon.model.entity.Pokemon
import com.example.who_is_that_pokemon.ui.Routes
import com.example.who_is_that_pokemon.ui.animation.LoadingAnimation
import com.example.who_is_that_pokemon.ui.theme.PokemonRed
import com.example.who_is_that_pokemon.ui.theme.SearchBackground
import com.example.who_is_that_pokemon.ui.theme.White
import com.example.who_is_that_pokemon.ui.viewmodel.HomeViewModel

@Composable
fun HomeView(viewModel: HomeViewModel, navController: NavHostController) {

    Scaffold(
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(White)
                    .padding(padding)
            ) {
                MainView(viewModel, navController)
            }
        }
    )
}

@Composable
fun MainView(viewModel: HomeViewModel, navController: NavHostController) {
    val searchHeight = 56.dp
    val allPokemon by viewModel.displayedPokemon.observeAsState(emptyList())
    val gridState = rememberLazyGridState()
    var pokemonSearch by remember { mutableStateOf("")}

    val shouldLoadMore by remember {
        derivedStateOf {
            val layoutInfo = gridState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

            lastVisibleItem >= totalItems - 5
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            viewModel.loadNext20Pokemon()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
    ) {

        Text(
            text = "Pokedex",
            style = MaterialTheme.typography.titleMedium,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Search for a pokemon by name or using its National Number according pokedex.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = pokemonSearch,
            onValueChange = { it ->
                pokemonSearch = it
            },
            modifier = Modifier
                .height(searchHeight)
                .background(SearchBackground, RoundedCornerShape(16.dp))
                .fillMaxWidth(),
            placeholder = { Text("Search Pokemon") },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            leadingIcon = {
                IconButton(onClick = {
                    navController.navigate(Routes.PokemonDetailsView + "/" + pokemonSearch.lowercase().trim())
                })
                {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                }
            }, colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                disabledBorderColor = Color.Transparent,
                errorBorderColor = Color.Transparent,
                focusedContainerColor = SearchBackground,
                unfocusedContainerColor = SearchBackground
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (allPokemon.isNullOrEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                LoadingAnimation(circleSize = 30.dp, spaceBetween = 20.dp, travelDistance = 20.dp)
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                state = gridState,
            ) {
                itemsIndexed(allPokemon) { index, pokemon ->
                    PokemonItem(pokemon, navController)
                }
            }
        }
    }
}

@Composable
fun PokemonItem(pokemon: Pokemon, navController: NavHostController) {
    Column(
        modifier = Modifier
            .heightIn(min = 200.dp)
            .padding(4.dp)
            .background(pokemon.color, RoundedCornerShape(20.dp))
            .padding(12.dp)
            .clickable(onClick = {
                navController.navigate(Routes.PokemonDetailsView + "/" + pokemon.name)
            }),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        AsyncImage(
            model = pokemon.sprites.default,
            contentDescription = pokemon.name,
            modifier = Modifier.size(150.dp)
        )

        Text(
            text = pokemon.name.replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "# " + pokemon.id,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

    }
}




