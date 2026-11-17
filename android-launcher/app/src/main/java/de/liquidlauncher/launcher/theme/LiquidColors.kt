package de.liquidlauncher.launcher.theme

import androidx.compose.ui.graphics.Color

/**
 * Designtokens des Liquid-Glass-Stils. Alle Oberflächen beziehen ihre Farben von hier,
 * damit das Erscheinungsbild einheitlich bleibt.
 */
object LiquidColors {
    val WallpaperTop = Color(0xFF1B2A4A)
    val WallpaperMiddle = Color(0xFF2E2350)
    val WallpaperBottom = Color(0xFF0E1322)

    val TextPrimary = Color(0xFFFFFFFF)
    val TextSecondary = Color(0xB3FFFFFF)

    val GlassFill = Color(0x26FFFFFF)
    val GlassBorder = Color(0x33FFFFFF)
    val IconTileFill = Color(0x1AFFFFFF)
}

object LiquidDimens {
    const val IconCornerRadiusDp = 22
    const val IconSizeDp = 60
    const val IconPixelSize = 192
    const val GridColumns = 4
}
