package com.levi.tellmeajokeapp.model

import com.levi.tellmeajokeapp.model.network.Result
import com.levi.tellmeajokeapp.model.network.Result.Success
import com.levi.tellmeajokeapp.model.network.Result.Error

class FakeDataSource(private val joke: Joke?) : DataSource {

    override suspend fun getJoke(): Result<Joke> {
        joke?.let {
            return Success(data = it)
        }
        return Error(Exception("Test exception"))
    }
}