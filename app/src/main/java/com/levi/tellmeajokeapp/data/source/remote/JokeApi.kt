package com.levi.tellmeajokeapp.data

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import retrofit2.http.GET

const val BASE_URL = "https://official-joke-api.appspot.com"

interface JokeApi {
    @GET("random_joke")
    fun getRandomJoke(): Joke
}

object RetrofitInstance {
    val api: JokeApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(JokeApi::class.java)
    }
}

