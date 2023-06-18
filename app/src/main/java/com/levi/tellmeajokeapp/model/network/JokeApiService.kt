package com.levi.tellmeajokeapp.model.network

import com.levi.tellmeajokeapp.model.Joke
import retrofit2.http.GET

interface JokeApiService {
    @GET("/random_joke")
    suspend fun getRandomJoke(): Joke
}
