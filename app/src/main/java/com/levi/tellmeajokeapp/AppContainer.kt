package com.levi.tellmeajokeapp

import com.levi.tellmeajokeapp.data.source.JokeRepository

interface AppContainer {
    val repository: JokeRepository
}