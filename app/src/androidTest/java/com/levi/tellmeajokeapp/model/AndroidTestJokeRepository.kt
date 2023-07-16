package com.levi.tellmeajokeapp.model

import com.levi.tellmeajokeapp.model.network.Result

class AndroidTestJokeRepository(
    vararg jokes: Joke,
    private val shouldReturnError: Boolean = false
) : JokeRepository {

    private val jokeData = jokes
    private var index = 0

    override suspend fun getJoke(): Result<Joke> {
        if (shouldReturnError) {
            return Result.Error(Exception("Test exception"))
        }

        return try {
            Result.Success(jokeData[index++])
        } catch (e: IndexOutOfBoundsException) {
            throw Exception("No more jokes left.")
        }
    }

}