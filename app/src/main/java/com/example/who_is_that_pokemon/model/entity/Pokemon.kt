package com.example.who_is_that_pokemon.model.entity

import androidx.compose.ui.graphics.Color
import com.google.gson.annotations.SerializedName

data class Pokemon(
    @SerializedName("id")
    var id : Int,
    @SerializedName("name")
    val name : String,
    @SerializedName("height")
    var height : Double,
    @SerializedName("weight")
    var weight : Double,
    @SerializedName("types")
    var types : List<TypeSlot>,
    @SerializedName("sprites")
    var sprites : Sprites,
    @SerializedName("stats")
    var stats : List<Stats>,

    var color : Color
)

data class TypeSlot (
    @SerializedName("slot")
    var slot : Int,
    @SerializedName("type")
    var type : Type
)

data class Type (
    @SerializedName("name")
    var name: String,
    @SerializedName("url")
    var url: String
)

data class Sprites(
    @SerializedName("front_default")
    var default : String = "",
    @SerializedName("front_shiny")
    var shiny : String = ""
)

data class Stats(
    @SerializedName("base_stat")
    var value : Float,
    @SerializedName("stat")
    var stat : Stat
)

data class Stat(
    @SerializedName("name")
    val statName : String,
)

data class SpecieDetails(
    @SerializedName("color")
    val pokemonColor : PokemonColor,
    @SerializedName("flavor_text_entries")
    val descriptions : List<PokemonDescription>
)

data class PokemonDescription(
    @SerializedName("flavor_text")
    var text : String
)

data class PokemonColor(
    @SerializedName("name")
    val colorName : String,
)
