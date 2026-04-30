package com.manu.kode.engrama.data.model

enum class Division(val displayName: String, val threshold: Int) {
    PLUTON("División Plutón", 0),
    MERCURIO("División Mercurio", 100),
    VENUS("División Venus", 300),
    TIERRA("División Tierra", 600),
    MARTE("División Marte", 1000),
    JUPITER("División Júpiter", 1500),
    SATURNO("División Saturno", 2200),
    URANO("División Urano", 3000),
    NEPTUNO("División Neptuno", 4000),
    LUNA("División Luna", 5500),
    SOL("División Sol", 7500);

    val imageName: String get() = name.lowercase()

    val next: Division? get() {
        val values = entries
        val index = values.indexOf(this)
        return if (index + 1 < values.size) values[index + 1] else null
    }

    companion object {
        fun fromPoints(points: Int): Division {
            return entries.reversed().firstOrNull { points >= it.threshold } ?: PLUTON
        }
    }
}