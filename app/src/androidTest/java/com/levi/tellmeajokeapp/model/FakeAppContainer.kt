package com.levi.tellmeajokeapp.model

import com.levi.tellmeajokeapp.model.AppContainer
import com.levi.tellmeajokeapp.model.JokeRepository

class FakeAppContainer(override val repository: JokeRepository) : AppContainer