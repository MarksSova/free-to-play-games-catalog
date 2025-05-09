package com.github.markssova.gamescatalog.api

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://www.freetogame.com/api/"
    private val objectMapper = jacksonObjectMapper().registerModule(kotlinModule())

    val freeToGameApiInstance: FreeToGameApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .build()
            .create(FreeToGameApi::class.java)
    }
}
