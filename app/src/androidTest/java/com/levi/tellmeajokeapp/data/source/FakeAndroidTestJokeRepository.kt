package com.levi.tellmeajokeapp.data.source

import com.levi.tellmeajokeapp.data.Joke
import com.levi.tellmeajokeapp.data.Result

class FakeAndroidTestJokeRepository(
    private val jokeData: List<Joke>,
    private val shouldReturnError: Boolean = false
) : JokeRepository {

    private var index = -1

    override suspend fun getJoke(): Result<Joke> {
        if (shouldReturnError) {
            return Result.Error(Exception("Test exception"))
        }

        index++

        return Result.Success(data = jokeData[index])
    }
}