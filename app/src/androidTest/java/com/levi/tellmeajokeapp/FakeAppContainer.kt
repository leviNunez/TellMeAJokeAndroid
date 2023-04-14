package com.levi.tellmeajokeapp

import com.levi.tellmeajokeapp.data.source.JokeRepository

class FakeAppContainer(override val repository: JokeRepository) : AppContainer