package com.levi.tellmeajokeapp.data.source

import com.levi.tellmeajokeapp.data.Joke
import com.levi.tellmeajokeapp.data.Result
import com.levi.tellmeajokeapp.data.Result.Success
import com.levi.tellmeajokeapp.data.Result.Error

class FakeDataSource(var joke: Joke?) : JokeDataSource {

    override suspend fun getJoke(): Result<Joke> {
        joke?.let {
            return Success(data = it)
        }
        return Error(Exception("Joke not found"))
    }
}