package com.levi.tellmeajokeapp.data.source.remote

import com.levi.tellmeajokeapp.data.Joke
import com.levi.tellmeajokeapp.data.JokeApi
import com.levi.tellmeajokeapp.data.Result
import com.levi.tellmeajokeapp.data.Result.Success
import com.levi.tellmeajokeapp.data.Result.Error
import com.levi.tellmeajokeapp.data.source.JokeDataSource

class RemoteDataSource(private val api: JokeApi) : JokeDataSource {

    override suspend fun getJoke(): Result<Joke> {
        return try {
            val joke = api.getRandomJoke()
            Success(data = joke)
        } catch (e: Exception) {
            Error(e)
        }
    }
}