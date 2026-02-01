package com.example.who_is_that_pokemon.ui.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.application
import androidx.lifecycle.viewModelScope
import com.example.who_is_that_pokemon.model.entity.Pokemon
import com.example.who_is_that_pokemon.model.entity.Stats
import com.example.who_is_that_pokemon.model.entity.TypeSlot
import com.example.who_is_that_pokemon.model.repository.remote.PokemonRepository
import com.example.who_is_that_pokemon.ui.theme.White
import kotlinx.coroutines.launch

class PokemonDetailsViewModel(application: Application) : BaseViewModel(application) {

    var id by mutableIntStateOf(0)
    var pokemonName by mutableStateOf("")
    var description by mutableStateOf("")
    var sprite by mutableStateOf("")
    var color by mutableStateOf(White)
    var wasFound by mutableStateOf(false)
    private val _pokemonStats = MutableLiveData(emptyList<Stats>())
    val pokemonStats: LiveData<List<Stats>> = _pokemonStats

    private val _pokemonTypes = MutableLiveData(emptyList<TypeSlot>())

    val pokemonTypes: LiveData<List<TypeSlot>> = _pokemonTypes

    private var currentPokemonName = ""

    fun loadPokemonInformation()
    {
        if (currentPokemonName.isEmpty()) return

        if (isLoading) return

        isLoading = true

        viewModelScope.launch {

            try {
                val response = pokemonRepository.getPokemonByNameOrId(currentPokemonName)

                if (response.isSuccessful && response.body() != null) {
                    val specie = pokemonRepository.getPokemonSpecieByName(currentPokemonName)
                    val pokemon = response.body()
                    if (specie != null && pokemon != null)
                    {
                        fillPokemonColor(pokemon)

                        id = pokemon.id
                        pokemonName = pokemon.name
                        description = specie.descriptions[0].text
                        sprite = pokemon.sprites.default
                        color = pokemon.color
                        _pokemonStats.value = pokemon.stats
                        _pokemonTypes.value = pokemon.types
                        wasFound = true
                    }
                } else
                {
                    wasFound = false
                }
            } catch (e : Exception)
            {
                wasFound = false
                Toast.makeText(application, e.message, Toast.LENGTH_LONG).show()
            } finally {
                isLoading = false
            }
        }
    }

    fun getTypeColor(color : String) : Color {
        return when (color.lowercase()) {
            "fire" -> Color(0xFFF08030)
            "water" -> Color(0xFF6890F0)
            "grass" -> Color(0xFF78C850)
            "electric" -> Color(0xFFF8D030)
            "ice" -> Color(0xFF98D8D8)
            "fighting" -> Color(0xFFC03028)
            "poison" -> Color(0xFFA040A0)
            "ground" -> Color(0xFFE0C068)
            "flying" -> Color(0xFFA890F0)
            "psychic" -> Color(0xFFF85888)
            "bug" -> Color(0xFFA8B820)
            "rock" -> Color(0xFFB8A038)
            "ghost" -> Color(0xFF705898)
            "dragon" -> Color(0xFF7038F8)
            "dark" -> Color(0xFF705848)
            "steel" -> Color(0xFFB8B8D0)
            "fairy" -> Color(0xFFEE99AC)
            "normal" -> Color(0xFFA8A878)
            else -> Color.LightGray
        }
    }

    fun setCurrentPokemonName(pokemonName: String)
    {
        currentPokemonName = pokemonName
    }

    fun clearPokemonInfo()
    {
        isLoading = false
        pokemonName = ""
        description = ""
        sprite = ""
        color = White
        _pokemonStats.value = emptyList()
        _pokemonTypes.value = emptyList()
        wasFound = false
    }
}