package com.manu.kode.engrama.domain.model

import com.manu.kode.engrama.domain.model.Division.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class DivisionTest {

    @Test
    fun fromPoints_respectsThresholdBorders() {
        val cases = listOf(
            -5 to PLUTON, 0 to PLUTON, 99 to PLUTON,
            100 to MERCURIO, 299 to MERCURIO,
            300 to VENUS, 599 to VENUS,
            600 to TIERRA, 999 to TIERRA,
            1000 to MARTE, 1499 to MARTE,
            1500 to JUPITER, 2199 to JUPITER,
            2200 to SATURNO, 2999 to SATURNO,
            3000 to URANO, 3999 to URANO,
            4000 to NEPTUNO, 5499 to NEPTUNO,
            5500 to LUNA, 7499 to LUNA,
            7500 to SOL, 100_000 to SOL
        )
        cases.forEach { (points, expected) ->
            assertEquals("puntos=$points", expected, Division.fromPoints(points))
        }
    }

    @Test
    fun thresholds_matchIos() {
        assertEquals(
            listOf(0, 100, 300, 600, 1000, 1500, 2200, 3000, 4000, 5500, 7500),
            Division.entries.map { it.threshold }
        )
    }

    @Test
    fun next_followsDeclarationOrder_andSolHasNone() {
        Division.entries.zipWithNext().forEach { (current, next) ->
            assertEquals(next, current.next)
        }
        assertNull(SOL.next)
    }

    @Test
    fun imageName_matchesIos() {
        assertEquals(
            listOf("pluton", "mercury", "venus", "earth", "mars", "jupyter", "saturn", "uranus", "neptune", "moon", "sun"),
            Division.entries.map { it.imageName }
        )
    }
}
