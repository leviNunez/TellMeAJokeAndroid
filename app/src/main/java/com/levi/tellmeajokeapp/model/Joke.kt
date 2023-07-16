package com.levi.tellmeajokeapp.model

import kotlinx.serialization.Serializable

@Serializable
data class Joke(
    val type: String,
    val setup: String,
    val punchline: String,
    val id: Int
)