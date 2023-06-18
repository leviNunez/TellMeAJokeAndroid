package com.levi.tellmeajokeapp.model

import com.levi.tellmeajokeapp.model.network.Result

interface DataSource {
    suspend fun getJoke(): Result<Joke>
}