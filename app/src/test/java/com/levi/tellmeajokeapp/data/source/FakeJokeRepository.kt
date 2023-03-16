package com.levi.tellmeajokeapp.data.source

import com.levi.tellmeajokeapp.data.Joke
import com.levi.tellmeajokeapp.data.Result

class FakeJokeRepository: JokeRepository {

    var shouldReturnError = false

    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }

    var jokeServiceData: Joke? = null

    fun setJoke(value: Joke) {
        jokeServiceData = value
    }

    override suspend fun getJoke(): Result<Joke> {
        if (shouldReturnError) {
            return Result.Error(Exception("Test exception"))
        }

        jokeServiceData?.let {
            return Result.Success(data = it)
        }
        return Result.Error(Exception("Joke not found"))
    }
}