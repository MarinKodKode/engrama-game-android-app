package com.manu.kode.engrama.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

class PlayerProgressTest {

    private fun assertProgress(total: Int, division: Division, progress: Double, toNext: Int) {
        val p = PlayerProgress.from(total)
        assertEquals("división ($total)", division, p.division)
        assertEquals("progress ($total)", progress, p.progress, 1e-9)
        assertEquals("pointsToNext ($total)", toNext, p.pointsToNext)
        assertEquals(total, p.totalPoints)
    }

    @Test
    fun startOfRange() = assertProgress(0, Division.PLUTON, 0.0, 100)

    @Test
    fun middleOfRange() = assertProgress(50, Division.PLUTON, 0.5, 50)

    @Test
    fun endOfRange() = assertProgress(99, Division.PLUTON, 0.99, 1)

    @Test
    fun crossingIntoNextDivision() = assertProgress(100, Division.MERCURIO, 0.0, 200)

    @Test
    fun widerRange() = assertProgress(6500, Division.LUNA, 0.5, 1000)

    @Test
    fun lastDivision_isComplete() {
        assertProgress(7500, Division.SOL, 1.0, 0)
        assertProgress(9000, Division.SOL, 1.0, 0)
    }
}
