package com.github.markssova.gamescatalog.api

import retrofit2.http.GET

interface FreeToGameApi {
    @GET("games")
    suspend fun getGames(): List<Game>
}