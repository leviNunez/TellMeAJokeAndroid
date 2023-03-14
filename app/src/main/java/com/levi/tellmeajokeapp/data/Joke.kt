package com.levi.tellmeajokeapp.data

import com.squareup.moshi.Json


data class Joke(
    @field:Json(name = "id") val id: Int,
    @field:Json(name = "punchline") val punchline: String,
    @field:Json(name = "setup") val setup: String,
    @field:Json(name = "type") val type: String
)