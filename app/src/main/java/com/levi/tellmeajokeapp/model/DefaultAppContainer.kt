package com.levi.tellmeajokeapp.model

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.levi.tellmeajokeapp.model.network.JokeApiService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit


class DefaultAppContainer : AppContainer {
    private val baseUrl = "https://official-joke-api.appspot.com"

    private val contentType = "application/json".toMediaType()
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(Json.asConverterFactory(contentType))
        .build()

    private val service: JokeApiService by lazy {
        retrofit.create(JokeApiService::class.java)
    }

    override val repository: JokeRepository by lazy {
        DefaultJokeRepository(jokeService = service)
    }

}