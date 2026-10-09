package com.manu.kode.engrama.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.manu.kode.engrama.R

val FredokaFamily = FontFamily(
    Font(R.font.fredoka_regular, FontWeight.Normal),
    Font(R.font.fredoka_medium, FontWeight.Medium),
    Font(R.font.fredoka_semibold, FontWeight.SemiBold),
    Font(R.font.fredoka_bold, FontWeight.Bold),
)

/** Interlineado aproximado al leading por defecto de iOS (sin lineSpacing explícito). */
private const val LINE_HEIGHT_FACTOR = 1.2f

private fun fredoka(weight: FontWeight, size: Int) = TextStyle(
    fontFamily = FredokaFamily,
    fontWeight = weight,
    fontSize = size.sp,
    lineHeight = (size * LINE_HEIGHT_FACTOR).sp,
    letterSpacing = 0.sp,
)

/**
 * Escala Fredoka mapeada a Material3 a partir de los tamaños que usa iOS
 * (`.custom("Fredoka-…", size:)`). Tabla en engrama-port-notes/08-cp2b-plan.md §3.4.
 */
val Typography = Typography(
    displayLarge = fredoka(FontWeight.SemiBold, 56),
    displayMedium = fredoka(FontWeight.SemiBold, 48),
    displaySmall = fredoka(FontWeight.SemiBold, 40),
    headlineLarge = fredoka(FontWeight.SemiBold, 32),
    headlineMedium = fredoka(FontWeight.SemiBold, 28),
    headlineSmall = fredoka(FontWeight.SemiBold, 24),
    titleLarge = fredoka(FontWeight.SemiBold, 22),
    titleMedium = fredoka(FontWeight.SemiBold, 18),
    titleSmall = fredoka(FontWeight.SemiBold, 16),
    bodyLarge = fredoka(FontWeight.Normal, 16),
    bodyMedium = fredoka(FontWeight.Normal, 14),
    bodySmall = fredoka(FontWeight.Normal, 13),
    labelLarge = fredoka(FontWeight.SemiBold, 14),
    labelMedium = fredoka(FontWeight.SemiBold, 13),
    labelSmall = fredoka(FontWeight.SemiBold, 12),
)
