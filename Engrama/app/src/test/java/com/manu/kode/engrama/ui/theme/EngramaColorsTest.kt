package com.manu.kode.engrama.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

/**
 * Los hex esperados son los que resolvió UIKit en el simulador de iOS 26.0
 * (engrama-port-notes/08-cp2b-plan.md §9.2).
 */
class EngramaColorsTest {

    private fun hex(color: Color) = "%08X".format(color.toArgb())

    private fun EngramaColors.asMap() = mapOf(
        "blue" to blue, "cyan" to cyan, "purple" to purple, "red" to red, "green" to green,
        "gray" to gray, "orange" to orange, "yellow" to yellow, "brown" to brown,
        "indigo" to indigo, "pink" to pink, "background" to background,
        "secondaryBackground" to secondaryBackground, "groupedBackground" to groupedBackground,
        "label" to label, "secondaryLabel" to secondaryLabel, "tertiaryLabel" to tertiaryLabel,
        "white" to white, "black" to black, "cardShadow" to cardShadow,
    ).mapValues { hex(it.value) }

    @Test
    fun `tokens claros coinciden con iOS 26`() {
        assertEquals(
            mapOf(
                "blue" to "FF0088FF", "cyan" to "FF00C0E8", "purple" to "FFCB30E0",
                "red" to "FFFF383C", "green" to "FF34C759", "gray" to "FF8E8E93",
                "orange" to "FFFF8D28", "yellow" to "FFFFCC00", "brown" to "FFAC7F5E",
                "indigo" to "FF6155F5", "pink" to "FFFF2D55", "background" to "FFFFFFFF",
                "secondaryBackground" to "FFF2F2F7", "groupedBackground" to "FFF2F2F7",
                "label" to "FF000000", "secondaryLabel" to "993C3C43", "tertiaryLabel" to "4C3C3C43",
                "white" to "FFFFFFFF", "black" to "FF000000", "cardShadow" to "0F000000",
            ),
            LightEngramaColors.asMap()
        )
    }

    @Test
    fun `tokens oscuros coinciden con iOS 26`() {
        assertEquals(
            mapOf(
                "blue" to "FF0091FF", "cyan" to "FF3CD3FE", "purple" to "FFDB34F2",
                "red" to "FFFF4245", "green" to "FF30D158", "gray" to "FF8E8E93",
                "orange" to "FFFF9230", "yellow" to "FFFFD600", "brown" to "FFB78A66",
                "indigo" to "FF6B5DFF", "pink" to "FFFF375F", "background" to "FF000000",
                "secondaryBackground" to "FF1C1C1E", "groupedBackground" to "FF000000",
                "label" to "FFFFFFFF", "secondaryLabel" to "99EBEBF5", "tertiaryLabel" to "4CEBEBF5",
                "white" to "FFFFFFFF", "black" to "FF000000", "cardShadow" to "0F000000",
            ),
            DarkEngramaColors.asMap()
        )
    }

    @Test
    fun `colorScheme usa los tokens iOS en los roles principales`() {
        listOf(false to LightEngramaColors, true to DarkEngramaColors).forEach { (dark, c) ->
            val s = c.toColorScheme(dark)
            assertEquals(c.blue, s.primary)
            assertEquals(c.gray, s.secondary)
            assertEquals(c.blue, s.tertiary)
            assertEquals(c.background, s.background)
            assertEquals(c.background, s.surface)
            assertEquals(c.label, s.onSurface)
            assertEquals(c.secondaryLabel, s.onSurfaceVariant)
            assertEquals(c.secondaryBackground, s.secondaryContainer)
            assertEquals(c.secondaryBackground, s.surfaceContainerHigh)
            assertEquals(c.tertiaryLabel, s.outline)
            assertEquals(c.red, s.error)
            assertEquals(c.blue.copy(alpha = ACCENT_CONTAINER_ALPHA), s.primaryContainer)
        }
    }

    @Test
    fun `ningun rol mapeado conserva el color base de Material3`() {
        listOf(
            LightEngramaColors.toColorScheme(false) to lightColorScheme(),
            DarkEngramaColors.toColorScheme(true) to darkColorScheme(),
        ).forEach { (s, base) ->
            listOf(
                s.primary to base.primary, s.secondary to base.secondary, s.tertiary to base.tertiary,
                s.primaryContainer to base.primaryContainer,
                s.secondaryContainer to base.secondaryContainer,
                s.tertiaryContainer to base.tertiaryContainer,
                s.surface to base.surface, s.surfaceVariant to base.surfaceVariant,
                s.surfaceContainer to base.surfaceContainer,
                s.surfaceContainerHigh to base.surfaceContainerHigh,
                s.outline to base.outline, s.surfaceTint to base.surfaceTint,
            ).forEach { (mapped, original) -> assertNotEquals(original, mapped) }
        }
    }
}
