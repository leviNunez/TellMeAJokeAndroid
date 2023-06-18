package com.levi.tellmeajokeapp.model

import com.levi.tellmeajokeapp.model.network.Result

class DefaultJokeRepository(private val jokeRemoteDataSource: DataSource) : JokeRepository {
    override suspend fun getJoke(): Result<Joke> = jokeRemoteDataSource.getJoke()
}