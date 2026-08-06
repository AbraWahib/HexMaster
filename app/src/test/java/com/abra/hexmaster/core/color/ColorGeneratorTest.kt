package com.abra.hexmaster.core.color

import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

class ColorGeneratorTest {

    @Test
    fun testGenerateRandomColor() {
        val generator = ColorGenerator(Random(42)) // Seeded for reproducibility
        val color = generator.generateRandomColor()
        
        assertTrue(color.r in 0..255)
        assertTrue(color.g in 0..255)
        assertTrue(color.b in 0..255)
    }
}
