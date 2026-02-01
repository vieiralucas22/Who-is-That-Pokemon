package com.example.who_is_that_pokemon.model.repository.remote.service

import com.example.who_is_that_pokemon.model.entity.InitialPokemonResponse
import com.example.who_is_that_pokemon.model.entity.Pokemon
import com.example.who_is_that_pokemon.model.entity.SpecieDetails
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonService {

    @GET("pokemon")
    suspend fun getSomePokemon() : Response<InitialPokemonResponse>

    @GET("pokemon/{nameOrId}")
    suspend fun getPokemonByNameOrId(@Path("nameOrId") nameOrId: String): Response<Pokemon>

    @GET("pokemon-species/{name}")
    suspend fun getPokemonSpecieByName(@Path("name") name: String): Response<SpecieDetails>

    @GET("pokemon")
    suspend fun getNext20Pokemon(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int): Response<InitialPokemonResponse>
}