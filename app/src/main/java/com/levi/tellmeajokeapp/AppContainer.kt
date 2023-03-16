package com.levi.tellmeajokeapp

import com.levi.tellmeajokeapp.data.source.JokeRepositoryImpl
import com.levi.tellmeajokeapp.data.source.remote.BASE_URL
import com.levi.tellmeajokeapp.data.source.remote.JokeApi
import com.levi.tellmeajokeapp.data.source.remote.JokeRemoteDataSource
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class AppContainer {

    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(JokeApi::class.java)

    private val remoteDataSource = JokeRemoteDataSource(api = api)

    val jokeRepository = JokeRepositoryImpl(jokeRemoteDataSource = remoteDataSource)
}