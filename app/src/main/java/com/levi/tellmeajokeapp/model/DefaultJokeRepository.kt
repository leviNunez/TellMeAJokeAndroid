package com.levi.tellmeajokeapp.model

import com.levi.tellmeajokeapp.model.network.JokeApiService
import com.levi.tellmeajokeapp.model.network.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DefaultJokeRepository(
    private val jokeService: JokeApiService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : JokeRepository {
    override suspend fun getJoke(): Result<Joke> {
        return try {
            withContext(ioDispatcher) {
                val response = jokeService.getRandomJoke()
                Result.Success(response)
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}