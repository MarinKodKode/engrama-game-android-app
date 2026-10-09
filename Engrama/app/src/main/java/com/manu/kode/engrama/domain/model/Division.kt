package com.manu.kode.engrama.domain.model

/** iOS: Core/Settings/UserProfile.swift:84-155 (origin/develop). */
enum class Division(
    override val rawValue: String,
    val threshold: Int,
    val imageName: String
) : RawValueEnum {
    PLUTON("División Plutón", 0, "pluton"),
    MERCURIO("División Mercurio", 100, "mercury"),
    VENUS("División Venus", 300, "venus"),
    TIERRA("División Tierra", 600, "earth"),
    MARTE("División Marte", 1000, "mars"),
    JUPITER("División Júpiter", 1500, "jupyter"),
    SATURNO("División Saturno", 2200, "saturn"),
    URANO("División Urano", 3000, "uranus"),
    NEPTUNO("División Neptuno", 4000, "neptune"),
    LUNA("División Luna", 5500, "moon"),
    SOL("División Sol", 7500, "sun");

    val next: Division?
        get() = entries.getOrNull(ordinal + 1)

    companion object {
        fun fromPoints(points: Int): Division =
            entries.lastOrNull { points >= it.threshold } ?: PLUTON
    }
}
