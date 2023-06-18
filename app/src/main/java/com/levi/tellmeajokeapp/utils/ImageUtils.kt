package com.levi.tellmeajokeapp.utils

import com.levi.tellmeajokeapp.R

fun randomLaughImage(): Int {
    val images = listOf(
        R.drawable.laugh_1,
        R.drawable.laugh_2,
        R.drawable.laugh_3,
        R.drawable.laugh_4
    )
    return  images.random()
}