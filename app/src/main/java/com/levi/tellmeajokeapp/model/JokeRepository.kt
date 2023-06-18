package com.levi.tellmeajokeapp.model

import com.levi.tellmeajokeapp.model.network.Result

interface JokeRepository {
    suspend fun getJoke(): Result<Joke>
}