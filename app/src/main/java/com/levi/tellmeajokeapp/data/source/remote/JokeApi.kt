package com.levi.tellmeajokeapp.data.source.remote

import com.levi.tellmeajokeapp.data.Joke
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

const val BASE_URL = "https://official-joke-api.appspot.com"

interface JokeApi {
    @GET("/random_joke")
    suspend fun getRandomJoke(): Joke
}
