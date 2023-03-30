package com.levi.tellmeajokeapp

import androidx.annotation.VisibleForTesting
import com.levi.tellmeajokeapp.data.source.JokeRepository
import com.levi.tellmeajokeapp.data.source.JokeRepositoryImpl
import com.levi.tellmeajokeapp.data.source.remote.BASE_URL
import com.levi.tellmeajokeapp.data.source.remote.JokeApi
import com.levi.tellmeajokeapp.data.source.remote.JokeRemoteDataSource
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class AppContainer {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(JokeApi::class.java)

    private val remoteDataSource = JokeRemoteDataSource(api = api)

    var jokeRepository: JokeRepository = JokeRepositoryImpl(jokeRemoteDataSource = remoteDataSource)
        @VisibleForTesting set
}