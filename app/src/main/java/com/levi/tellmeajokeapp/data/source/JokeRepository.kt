package com.levi.tellmeajokeapp.data.source

import com.levi.tellmeajokeapp.data.Joke
import com.levi.tellmeajokeapp.data.Result

interface JokeRepository {
    suspend fun getJoke(): Result<Joke>
}