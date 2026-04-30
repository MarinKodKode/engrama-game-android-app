package com.manu.kode.engrama.data.model

import androidx.compose.ui.graphics.Color

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

// 👈 fuera del enum
fun Division.color(): Color = when (this) {
    Division.PLUTON   -> Color(0xFF78909C)
    Division.MERCURIO -> Color(0xFF90A4AE)
    Division.VENUS    -> Color(0xFFCE93D8)
    Division.TIERRA   -> Color(0xFF66BB6A)
    Division.MARTE    -> Color(0xFFEF5350)
    Division.JUPITER  -> Color(0xFFFF9800)
    Division.SATURNO  -> Color(0xFFFFD54F)
    Division.URANO    -> Color(0xFF4DD0E1)
    Division.NEPTUNO  -> Color(0xFF5C6BC0)
    Division.LUNA     -> Color(0xFFB0BEC5)
    Division.SOL      -> Color(0xFFFFEB3B)
}

fun Division.emoji(): String = when (this) {
    Division.PLUTON   -> "🪨"
    Division.MERCURIO -> "⚫"
    Division.VENUS    -> "🌕"
    Division.TIERRA   -> "🌍"
    Division.MARTE    -> "🔴"
    Division.JUPITER  -> "🟠"
    Division.SATURNO  -> "🪐"
    Division.URANO    -> "🔵"
    Division.NEPTUNO  -> "💙"
    Division.LUNA     -> "🌙"
    Division.SOL      -> "☀️"
}