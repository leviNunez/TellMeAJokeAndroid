package com.levi.tellmeajokeapp.viewmodel

import com.levi.tellmeajokeapp.model.Joke
import com.levi.tellmeajokeapp.model.JokeRepository
import com.levi.tellmeajokeapp.model.network.Result

class FakeJokeRepository(private val joke: Joke) : JokeRepository {
    var shouldReturnError = false

    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }

    override suspend fun getJoke(): Result<Joke> {
        if (shouldReturnError) {
            return Result.Error(Exception("Test exception"))
        }
        return Result.Success(data = joke)
    }
}