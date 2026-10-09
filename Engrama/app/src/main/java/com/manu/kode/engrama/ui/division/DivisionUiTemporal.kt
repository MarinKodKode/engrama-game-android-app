package com.manu.kode.engrama.ui.division

import androidx.compose.ui.graphics.Color
import com.manu.kode.engrama.domain.model.Division

// TEMPORAL (se reescribe en la Fase 1): mapeo de develop sin equivalente en iOS
// (iOS usa colores RGB en UserProfile.swift:130-142 e imágenes de planetas).
// Copiado tal cual de data/model/Division.kt:34-60 de develop para no cambiar la UI.

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