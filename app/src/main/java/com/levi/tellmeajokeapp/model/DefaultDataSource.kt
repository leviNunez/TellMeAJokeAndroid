package com.levi.tellmeajokeapp.model

import com.levi.tellmeajokeapp.model.network.Result.Success
import com.levi.tellmeajokeapp.model.network.Result.Error
import com.levi.tellmeajokeapp.model.network.JokeApiService
import com.levi.tellmeajokeapp.model.network.Result

class DefaultDataSource(private val api: JokeApiService) : DataSource {
    override suspend fun getJoke(): Result<Joke> {
        return try {
            val joke = api.getRandomJoke()
            Success(data = joke)
        } catch (e: Exception) {
            Error(e)
        }
    }
}