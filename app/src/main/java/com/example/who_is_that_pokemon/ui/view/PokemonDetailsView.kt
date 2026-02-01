package com.example.who_is_that_pokemon.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.who_is_that_pokemon.model.entity.Stats
import com.example.who_is_that_pokemon.ui.animation.LoadingAnimation
import com.example.who_is_that_pokemon.ui.theme.SearchBackground
import com.example.who_is_that_pokemon.ui.viewmodel.PokemonDetailsViewModel
import kotlin.text.replaceFirstChar

@Composable
fun PokemonDetailsView(viewModel: PokemonDetailsViewModel) {

    LaunchedEffect(Unit) {
        viewModel.loadPokemonInformation()
    }

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
fun MainView(viewModel: PokemonDetailsViewModel) {

    val allPokemonStats by viewModel.pokemonStats.observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "# " + viewModel.id,
            style = MaterialTheme.typography.titleMedium
        )
        AsyncImage(
            model = viewModel.sprite,
            contentDescription = viewModel.pokemonName,
            modifier = Modifier.size(150.dp)
        )
        Text(
            text = viewModel.pokemonName.replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.titleMedium,
            fontSize = 32.sp
        )
        Text(
            text = "Tipos",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(Modifier.height(8.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = viewModel.description.replace("\n", " ")
                .replace("\u000c", " "),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            fontSize = 16.sp
        )

        Spacer(Modifier.height(8.dp))


        allPokemonStats.let { stat ->

            if (allPokemonStats.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    LoadingAnimation(
                        circleSize = 30.dp,
                        spaceBetween = 20.dp,
                        travelDistance = 20.dp
                    )
                }
            } else {


                LazyColumn(content = {
                    itemsIndexed(stat) { index, item ->
                        StatusComponent(item)
                    }
                }, modifier = Modifier.background(SearchBackground, shape = RoundedCornerShape(16.dp)).padding(16.dp))
            }
        }
    }
}

@Composable
fun StatusComponent(item: Stats) {

    Spacer(Modifier.height(8.dp))

    Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = item.stat.statName.replaceFirstChar { it.uppercase() }.replace("Special-", "Sp.").replace("attack", "atk").replace("defense", "def"),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(0.24f)
        )
        Text(
            text = item.value.toString(),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(0.14f)
        )

        LinearProgressIndicator(
            progress = { item.value },
            modifier = Modifier.weight(0.62f),
            color = ProgressIndicatorDefaults.linearColor,
            trackColor = ProgressIndicatorDefaults.linearTrackColor,
            strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
        )
    }
}