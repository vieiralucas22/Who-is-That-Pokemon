package com.example.who_is_that_pokemon.ui.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.application
import androidx.lifecycle.viewModelScope
import com.example.who_is_that_pokemon.model.entity.Pokemon
import com.example.who_is_that_pokemon.model.entity.Stats
import com.example.who_is_that_pokemon.model.repository.remote.PokemonRepository
import com.example.who_is_that_pokemon.ui.theme.White
import kotlinx.coroutines.launch

class PokemonDetailsViewModel(application: Application) : AndroidViewModel(application) {

    private val pokemonRepository = PokemonRepository(application)

    var id by mutableIntStateOf(0)
    var pokemonName by mutableStateOf("")
    var description by mutableStateOf("")
    var sprite by mutableStateOf("")
    var color by mutableStateOf(White)
    private val _pokemonStats = MutableLiveData(emptyList<Stats>())
    val pokemonStats: LiveData<List<Stats>> = _pokemonStats

    private var isLoading = false
    private var currentPokemonName = ""

    init {
        loadPokemonInformation()
    }

    fun loadPokemonInformation()
    {
        if (isLoading) return

        isLoading = true

        viewModelScope.launch {
            try {
                val response = pokemonRepository.getPokemonByName(currentPokemonName)

                if (response.isSuccessful && response.body() != null) {
                    val specie = pokemonRepository.getPokemonSpecieByName(currentPokemonName)
                    val pokemon = response.body()
                    if (specie != null && pokemon != null)
                    {
                        id = pokemon.id
                        pokemonName = pokemon.name
                        description = specie.descriptions[0].text
                        sprite = pokemon.sprites.default
                        color = pokemon.color
                        _pokemonStats.value = pokemon.stats
                    }
                }
            } catch (e : Exception)
            {
                Toast.makeText(application, e.message, Toast.LENGTH_LONG).show()
            } finally {
                isLoading = false
            }
        }
    }

    fun setCurrentPokemonName(pokemonName: String)
    {
        currentPokemonName = pokemonName
    }
}