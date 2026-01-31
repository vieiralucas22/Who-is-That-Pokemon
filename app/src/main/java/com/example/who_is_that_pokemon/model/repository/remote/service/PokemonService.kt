package com.example.who_is_that_pokemon.model.repository.remote.service

import com.example.who_is_that_pokemon.model.entity.InitialPokemonResponse
import com.example.who_is_that_pokemon.model.entity.Pokemon
import com.example.who_is_that_pokemon.model.entity.SpecieDetails
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PokemonService {

    @GET("pokemon")
    suspend fun getSomePokemon() : Response<InitialPokemonResponse>

    @GET("pokemon/{id}")
    suspend fun getPokemonById(id : Int) : Response<Pokemon>

    @GET("pokemon/{name}")
    suspend fun getPokemonByName(@Path("name") name: String): Response<Pokemon>

    @GET("pokemon-species/{name}")
    suspend fun getPokemonSpecieByName(@Path("name") name: String): Response<SpecieDetails>

}