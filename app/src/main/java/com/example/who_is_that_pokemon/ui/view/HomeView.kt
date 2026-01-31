package com.example.who_is_that_pokemon.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.who_is_that_pokemon.R
import com.example.who_is_that_pokemon.model.entity.Pokemon
import com.example.who_is_that_pokemon.ui.theme.PokemonRed
import com.example.who_is_that_pokemon.ui.theme.SearchBackground
import com.example.who_is_that_pokemon.ui.theme.White
import com.example.who_is_that_pokemon.ui.viewmodel.HomeViewModel

@Composable
fun HomeView(viewModel: HomeViewModel) {

    Scaffold(
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                MainView(viewModel)
            }
        }
    )
}

@Composable
fun MainView(viewModel: HomeViewModel) {

    val searchHeight = 56.dp
    val allPokemon by viewModel.displayedPokemon.observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
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

        Row {
            OutlinedTextField(
                value = "",
                onValueChange = { it ->
                },
                modifier = Modifier
                    .weight(0.80f)
                    .height(searchHeight)
                    .background(SearchBackground, RoundedCornerShape(16.dp)),
                placeholder = { Text("Search Pokémon") },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                }, colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    disabledBorderColor = Color.Transparent,
                    errorBorderColor = Color.Transparent,
                    focusedContainerColor = SearchBackground,
                    unfocusedContainerColor = SearchBackground
                )
            )

            Spacer(modifier = Modifier.width(4.dp))

            Button(
                onClick = {},
                modifier = Modifier
                    .weight(0.18f)
                    .height(searchHeight),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(0.dp),
                colors = ButtonColors(
                    containerColor = PokemonRed,
                    contentColor = PokemonRed,
                    disabledContainerColor = PokemonRed,
                    disabledContentColor = PokemonRed
                )
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_filter),
                    contentDescription = "",
                    tint = White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

            if (allPokemon.isNullOrEmpty())
            {

            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                ) {
                    itemsIndexed(allPokemon) { index, pokemon ->
                        PokemonItem(pokemon)
                    }
                }
            }
    }
}

@Composable
fun PokemonItem(pokemon : Pokemon) {
    Column(
        modifier = Modifier
            .heightIn(min = 200.dp)
            .padding(4.dp)
            .background(pokemon.color, RoundedCornerShape(20.dp))
            .padding(12.dp),
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




