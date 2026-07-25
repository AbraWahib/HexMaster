package com.abra.hexmaster.core.color

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class HexColorUtilsTest {

    @Test
    fun testIsValidHex() {
        assertTrue(HexColorUtils.isValidHex("FFFFFF"))
        assertTrue(HexColorUtils.isValidHex("#000000"))
        assertTrue(HexColorUtils.isValidHex("ABCDEF"))
        assertTrue(HexColorUtils.isValidHex("123456"))
        assertFalse(HexColorUtils.isValidHex("GGGGGG"))
        assertFalse(HexColorUtils.isValidHex("FFF"))
        assertFalse(HexColorUtils.isValidHex("FFFFFFF"))
    }

    @Test
    fun testHexToRgb() {
        assertEquals(Triple(255, 255, 255), HexColorUtils.hexToRgb("FFFFFF"))
        assertEquals(Triple(0, 0, 0), HexColorUtils.hexToRgb("#000000"))
        assertEquals(Triple(255, 0, 0), HexColorUtils.hexToRgb("FF0000"))
    }

    @Test
    fun testRgbToHex() {
        assertEquals("FFFFFF", HexColorUtils.rgbToHex(255, 255, 255))
        assertEquals("000000", HexColorUtils.rgbToHex(0, 0, 0))
        assertEquals("FF0000", HexColorUtils.rgbToHex(255, 0, 0))
    }
}
