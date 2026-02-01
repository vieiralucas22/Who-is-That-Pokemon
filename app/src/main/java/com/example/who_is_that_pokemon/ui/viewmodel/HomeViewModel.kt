package com.example.who_is_that_pokemon.ui.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.application
import androidx.lifecycle.viewModelScope
import com.example.who_is_that_pokemon.constants.RetrofitConstants
import com.example.who_is_that_pokemon.model.entity.Pokemon
import com.example.who_is_that_pokemon.model.entity.Sprites
import com.example.who_is_that_pokemon.model.repository.remote.PokemonRepository
import com.example.who_is_that_pokemon.ui.theme.PokemonBlack
import com.example.who_is_that_pokemon.ui.theme.PokemonBlue
import com.example.who_is_that_pokemon.ui.theme.PokemonBrown
import com.example.who_is_that_pokemon.ui.theme.PokemonDefault
import com.example.who_is_that_pokemon.ui.theme.PokemonGray
import com.example.who_is_that_pokemon.ui.theme.PokemonGreen
import com.example.who_is_that_pokemon.ui.theme.PokemonPink
import com.example.who_is_that_pokemon.ui.theme.PokemonPurple
import com.example.who_is_that_pokemon.ui.theme.PokemonRed
import com.example.who_is_that_pokemon.ui.theme.PokemonWhite
import com.example.who_is_that_pokemon.ui.theme.PokemonYellow
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : BaseViewModel(application) {

    private val _displayedPokemon = MutableLiveData(emptyList<Pokemon>())
    val displayedPokemon: LiveData<List<Pokemon>> = _displayedPokemon

    private var nextPokemon: String = ""

    private var pokemonInScreen: MutableList<Pokemon> = mutableListOf()

    init {
        loadPokemon()
    }

    fun loadPokemon() {
        if (isLoading) return

        isLoading = true

        viewModelScope.launch {
            try {
                val response = pokemonRepository.getInitialPokemon()

                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()

                    if (body != null && body.pokemons.isNotEmpty()) {
                        fillAllPokemonInfo(body.pokemons)
                        nextPokemon = body.next20Pokemons
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(application, e.message, Toast.LENGTH_LONG).show()
            } finally {
            isLoading = false
        }
        }
    }

    fun loadNext20Pokemon() {
        if (isLoading) return

        isLoading = true

        viewModelScope.launch {
            try {

                val (offset, limit) = getNext20PokemonInfo()

                val response = pokemonRepository.getNext20Pokemon(offset,limit)

                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()

                    if (body != null && body.pokemons != null && body.pokemons.isNotEmpty()) {
                        fillAllPokemonInfo(body.pokemons)
                        nextPokemon = body.next20Pokemons
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(application, e.message, Toast.LENGTH_LONG).show()
            } finally {
                isLoading = false
            }
        }
    }

    fun fillAllPokemonInfo(allPokemon: List<Pokemon>) {
        viewModelScope.launch {
            for (pokemon in allPokemon) {
                val response = pokemonRepository.getPokemonByNameOrId(pokemon.name)

                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()

                    fillPokemonInfo(pokemon, body)
                }
            }

            updatePokemonDisplayed(allPokemon)
        }
    }

    suspend fun fillPokemonInfo(newPokemon: Pokemon, body: Pokemon?) {
        newPokemon.weight = body?.weight!!
        newPokemon.height = body.height
        newPokemon.id = body.id
        fillAllPokemonStats(newPokemon, body)
        fillPokemonSprites(newPokemon, body)
        fillPokemonTypes(newPokemon, body)
        fillPokemonColor(newPokemon)
    }

    fun fillAllPokemonStats(newPokemon: Pokemon, body: Pokemon?) {
        if (body != null && body.stats.isNotEmpty())
            newPokemon.stats = body.stats
    }

    fun fillPokemonSprites(newPokemon: Pokemon, body: Pokemon?) {
        if (body != null) {
            newPokemon.sprites = Sprites()
            newPokemon.sprites.default = body.sprites.default
            newPokemon.sprites.shiny = body.sprites.shiny
        }
    }

    fun fillPokemonTypes(newPokemon: Pokemon, body: Pokemon?) {
        if (body != null && body.types.isNotEmpty())
            newPokemon.types = body.types
    }

    fun getNext20PokemonInfo() : Pair<Int, Int> {
        val query = nextPokemon.replace(RetrofitConstants.BASE_POKE_API_URL, "").substringAfter("?", "")
        val params = query.split("&")
            .associate {
                val (key, value) = it.split("=")
                key to value
            }

        val offset = params["offset"]?.toIntOrNull() ?: 0
        val limit = params["limit"]?.toIntOrNull() ?: 20

        return offset to limit
    }

    fun updatePokemonDisplayed(newPokemons : List<Pokemon>)
    {
        for (pokemon in newPokemons)
        {
            pokemonInScreen.add(pokemon)
        }

        _displayedPokemon.value = pokemonInScreen.toList()
    }

}