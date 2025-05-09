package com.github.markssova.gamescatalog.api

import com.fasterxml.jackson.annotation.JsonProperty
import retrofit2.http.GET
import java.util.Date

interface FreeToGameApi {
    @GET("games")
    suspend fun getGames(): List<Game>
}

data class Game(
    val id: Int,
    val title: String,
    val thumbnail: String,
    @JsonProperty("short_description")
    val shortDescription: String,
    @JsonProperty("game_url")
    val gameUrl: String,
    val genre: String,
    val platform: String,
    val publisher: String,
    val developer: String,
    @JsonProperty("release_date")
    val releaseDate: Date,
    @JsonProperty("freetogame_profile_url")
    val freeToGameProfileUrl: String
)

