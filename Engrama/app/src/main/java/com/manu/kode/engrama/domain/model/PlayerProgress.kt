package com.manu.kode.engrama.domain.model

/**
 * Progreso derivado de la suma de puntos de las sesiones; nunca se persiste.
 * Fórmulas de iOS: Features/Settings/Division/DivisionViewModel.swift:21-32.
 */
data class PlayerProgress(
    val totalPoints: Int,
    val division: Division,
    val progress: Double,
    val pointsToNext: Int
) {
    companion object {
        fun from(totalPoints: Int): PlayerProgress {
            val division = Division.fromPoints(totalPoints)
            val next = division.next
            val progress = when {
                next == null -> 1.0
                next.threshold - division.threshold <= 0 -> 0.0
                else -> {
                    val range = next.threshold - division.threshold
                    val current = maxOf(totalPoints - division.threshold, 0)
                    minOf(current.toDouble() / range.toDouble(), 1.0)
                }
            }
            val pointsToNext = if (next == null) 0 else maxOf(next.threshold - totalPoints, 0)
            return PlayerProgress(totalPoints, division, progress, pointsToNext)
        }
    }
}
