package com.example.who_is_that_pokemon.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.who_is_that_pokemon.R
import com.example.who_is_that_pokemon.model.entity.Stats
import com.example.who_is_that_pokemon.model.entity.TypeSlot
import com.example.who_is_that_pokemon.ui.animation.LoadingAnimation
import com.example.who_is_that_pokemon.ui.theme.PokemonBlack
import com.example.who_is_that_pokemon.ui.theme.SearchBackground
import com.example.who_is_that_pokemon.ui.theme.White
import com.example.who_is_that_pokemon.ui.viewmodel.PokemonDetailsViewModel
import kotlin.text.replaceFirstChar

@Composable
fun PokemonDetailsView(viewModel: PokemonDetailsViewModel) {

    LaunchedEffect(Unit) {
        viewModel.loadPokemonInformation()
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearPokemonInfo()
        }
    }


    Scaffold(
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(color = viewModel.color),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center

            ) {

                if (viewModel.isLoading) {
                    LoadingAnimation(
                        circleSize = 50.dp,
                        travelDistance = 40.dp,
                        spaceBetween = 12.dp
                    )
                } else {
                    if (viewModel.shouldShowNotFoundComponent) {
                        NotFoundPokemonComponent()
                    } else {
                        HeaderView(viewModel)

                        Box(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Image(
                                painter = painterResource(R.drawable.pokeball_icon),
                                contentDescription = null,
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .size(250.dp)
                                    .alpha(0.15f)
                                    .rotate(315f)
                            )

                            AsyncImage(
                                model = viewModel.sprite,
                                contentDescription = viewModel.pokemonName,
                                modifier = Modifier
                                    .size(200.dp)
                                    .align(Alignment.Center)
                            )

                        }
                        MainView(viewModel)

                    }
                }
            }
        }
    )
}

@Composable
fun NotFoundPokemonComponent() {
    Column(
        modifier = Modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Pokemon not found!",
            style = MaterialTheme.typography.titleMedium,
            fontSize = 24.sp,
            color = PokemonBlack,
            textAlign = TextAlign.Center
        )

        Image(
            modifier = Modifier.width(300.dp),
            painter = painterResource(R.drawable.not_found),
            contentDescription = "Pokemon not found"
        )

        Text(
            text = "Please back and try again.",
            style = MaterialTheme.typography.titleMedium,
            fontSize = 24.sp,
            color = PokemonBlack,
            textAlign = TextAlign.Center
        )
    }
}


@Composable
fun HeaderView(viewModel: PokemonDetailsViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = viewModel.pokemonName.replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.titleMedium,
            fontSize = 32.sp,
            color = White
        )

        Text(
            text = "# " + viewModel.id,
            style = MaterialTheme.typography.titleMedium,
            color = White,
            fontSize = 24.sp,
        )
    }
}

@Composable
fun MainView(viewModel: PokemonDetailsViewModel) {

    val allPokemonStats by viewModel.pokemonStats.observeAsState(emptyList())
    val allPokemonTypes by viewModel.pokemonTypes.observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .background(White, RoundedCornerShape(16.dp))
            .padding(16.dp),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        allPokemonTypes.let { stat ->

            if (allPokemonTypes.isNotEmpty())
                LazyRow(content = {
                    itemsIndexed(stat) { index, item ->
                        TypeComponent(item, viewModel)
                    }
                })
        }

        Spacer(Modifier.height(16.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "About",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            fontSize = 24.sp,
            color = viewModel.color
        )

        Spacer(Modifier.height(16.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = viewModel.description.replace("\n", " ")
                .replace("\u000c", " "),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            fontSize = 16.sp
        )

        Spacer(Modifier.height(16.dp))


        allPokemonStats.let { stat ->

            if (allPokemonStats.isNotEmpty()) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Base Stats",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 24.sp,
                    color = viewModel.color
                )

                Spacer(Modifier.height(16.dp))

                LazyColumn(content = {
                    itemsIndexed(stat) { index, item ->
                        StatusComponent(item, viewModel)
                    }
                })
            }
        }
    }
}

@Composable
fun TypeComponent(item: TypeSlot, viewModel: PokemonDetailsViewModel) {
    Column(
        modifier = Modifier
            .widthIn(min = 50.dp)
            .background(viewModel.getTypeColor(item.type.name), RoundedCornerShape(24.dp))
            .padding(4.dp, 1.dp, 4.dp, 1.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = item.type.name,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            color = White,
            fontSize = 12.sp
        )
    }

    Spacer(Modifier.width(4.dp))
}

@Composable
fun StatusComponent(item: Stats, viewModel: PokemonDetailsViewModel) {

    Spacer(Modifier.height(8.dp))

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = item.stat.statName.replaceFirstChar { it.uppercase() }.replace("Special-", "Sp.")
                .replace("attack", "atk").replace("defense", "def"),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(0.24f),
            color = viewModel.color
        )

        Spacer(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
                .background(SearchBackground)
                .weight(0.02f)
        )

        Text(
            text = item.value.toString(),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(0.14f)
        )

        LinearProgressIndicator(
            progress = { item.value / 255f },
            modifier = Modifier
                .weight(0.60f)
                .height(8.dp),
            color = viewModel.color,
            trackColor = viewModel.color.copy(alpha = 0.25f),
            gapSize = 0.dp,
            strokeCap = StrokeCap.Round
        )
    }
}