package com.abra.hexmaster.core.color

object HexColorUtils {
    fun isValidHex(input: String): Boolean {
        val cleaned = input.removePrefix("#")
        return cleaned.length == 6 && cleaned.all { it.isDigit() || it.lowercaseChar() in 'a'..'f' }
    }

    fun hexToRgb(hex: String): Triple<Int, Int, Int> {
        val cleaned = hex.removePrefix("#")
        if (!isValidHex(cleaned)) {
            throw IllegalArgumentException("Invalid hex color: $hex")
        }
        val r = cleaned.substring(0, 2).toInt(16)
        val g = cleaned.substring(2, 4).toInt(16)
        val b = cleaned.substring(4, 6).toInt(16)
        return Triple(r, g, b)
    }

    fun rgbToHex(r: Int, g: Int, b: Int): String {
        return "%02X%02X%02X".format(r, g, b)
    }
}
