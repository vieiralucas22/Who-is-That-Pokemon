package com.example.who_is_that_pokemon.model.entity

data class Pokemon(
    val id : Int,
    val name : String,
    val height : Double,
    val weight : Double,
    val types : List<TypeSlot>,
    val sprites : Sprites,
    val stats : List<Stats>
)

data class TypeSlot (
    val slot : Int,
    val type : Type
)

data class Type (
    val name: String,
    val url: String
)

data class Sprites(
    val default : String,
    val shiny : String
)

data class Stats(
    val value : Int,
    val stat : Stat
)

data class Stat(
    val statName : String,
)
