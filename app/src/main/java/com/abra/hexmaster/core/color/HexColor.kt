package com.abra.hexmaster.core.color

data class HexColor(
    val r: Int,
    val g: Int,
    val b: Int
) {
    val hex: String get() = HexColorUtils.rgbToHex(r, g, b)
}
