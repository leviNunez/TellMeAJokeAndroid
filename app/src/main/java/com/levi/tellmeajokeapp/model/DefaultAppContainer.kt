package com.levi.tellmeajokeapp.model

import com.levi.tellmeajokeapp.model.network.JokeApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class DefaultAppContainer : AppContainer {
    private val baseUrl = "https://official-joke-api.appspot.com/jokes/"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val api = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(JokeApiService::class.java)

    private val defaultDataSource = DefaultDataSource(api = api)

    override val repository: JokeRepository
        get() = DefaultJokeRepository(jokeRemoteDataSource = defaultDataSource)
}