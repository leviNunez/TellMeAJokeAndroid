package com.levi.tellmeajokeapp.model.network

import com.levi.tellmeajokeapp.model.JokesResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface JokeApiService {
    @GET("{category}/ten")
    suspend fun fetchJokes(@Path("category") type: String): JokesResponse
}
