package com.abra.hexmaster.core.color

import kotlin.random.Random

class ColorGenerator(private val random: Random = Random.Default) {
    fun generateRandomColor(): HexColor {
        return HexColor(
            r = random.nextInt(256),
            g = random.nextInt(256),
            b = random.nextInt(256)
        )
    }
}
