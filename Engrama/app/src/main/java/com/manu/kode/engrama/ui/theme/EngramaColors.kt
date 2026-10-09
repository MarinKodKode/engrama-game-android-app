package com.manu.kode.engrama.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Tokens de color de Engrama.
 *
 * Valores resueltos por UIKit en el simulador de iOS 26.0 (claro/oscuro), que es lo que
 * muestran los colores de sistema que usan las vistas iOS (`.blue`, `.secondary`,
 * `Color(.systemBackground)`…). Los valores de iOS 18 están registrados en
 * engrama-port-notes/08-cp2b-plan.md §9.2.
 */
@Immutable
data class EngramaColors(
    // Colores de sistema (SwiftUI Color.blue, .cyan…)
    val blue: Color,
    val cyan: Color,
    val purple: Color,
    val red: Color,
    val green: Color,
    val gray: Color,
    val orange: Color,
    val yellow: Color,
    val brown: Color,
    val indigo: Color,
    val pink: Color,
    // Semánticos
    val background: Color,          // systemBackground
    val secondaryBackground: Color, // secondarySystemBackground
    val groupedBackground: Color,   // systemGroupedBackground
    val label: Color,               // label / .primary
    val secondaryLabel: Color,      // secondaryLabel / .secondary
    val tertiaryLabel: Color,       // tertiaryLabel / .tertiary
    val white: Color,
    val black: Color,
    val cardShadow: Color,          // .black.opacity(0.06) (HomeView MenuRowView)
)

val LightEngramaColors = EngramaColors(
    blue = Color(0xFF0088FF),
    cyan = Color(0xFF00C0E8),
    purple = Color(0xFFCB30E0),
    red = Color(0xFFFF383C),
    green = Color(0xFF34C759),
    gray = Color(0xFF8E8E93),
    orange = Color(0xFFFF8D28),
    yellow = Color(0xFFFFCC00),
    brown = Color(0xFFAC7F5E),
    indigo = Color(0xFF6155F5),
    pink = Color(0xFFFF2D55),
    background = Color(0xFFFFFFFF),
    secondaryBackground = Color(0xFFF2F2F7),
    groupedBackground = Color(0xFFF2F2F7),
    label = Color(0xFF000000),
    secondaryLabel = Color(0x993C3C43),
    tertiaryLabel = Color(0x4C3C3C43),
    white = Color(0xFFFFFFFF),
    black = Color(0xFF000000),
    cardShadow = Color.Black.copy(alpha = 0.06f),
)

val DarkEngramaColors = EngramaColors(
    blue = Color(0xFF0091FF),
    cyan = Color(0xFF3CD3FE),
    purple = Color(0xFFDB34F2),
    red = Color(0xFFFF4245),
    green = Color(0xFF30D158),
    gray = Color(0xFF8E8E93),
    orange = Color(0xFFFF9230),
    yellow = Color(0xFFFFD600),
    brown = Color(0xFFB78A66),
    indigo = Color(0xFF6B5DFF),
    pink = Color(0xFFFF375F),
    background = Color(0xFF000000),
    secondaryBackground = Color(0xFF1C1C1E),
    groupedBackground = Color(0xFF000000),
    label = Color(0xFFFFFFFF),
    secondaryLabel = Color(0x99EBEBF5),
    tertiaryLabel = Color(0x4CEBEBF5),
    white = Color(0xFFFFFFFF),
    black = Color(0xFF000000),
    cardShadow = Color.Black.copy(alpha = 0.06f),
)

/** Opacidad del fondo de un color de acento (patrón iOS `color.opacity(0.15)`). */
const val ACCENT_CONTAINER_ALPHA = 0.15f

/**
 * Dentro de un `Button` de SwiftUI con estilo por defecto, `.primary`/`.secondary`/`.tertiary`
 * se resuelven sobre el tinte (accent = blue), no sobre label. Opacidades medidas en una captura
 * del simulador iOS 26 (HomeView, modo claro): secundario 50 %, terciario 25 %.
 */
const val TINT_SECONDARY_ALPHA = 0.5f
const val TINT_TERTIARY_ALPHA = 0.25f

/** Opacidad del contenido deshabilitado (valor estándar de Material3; iOS no lo fija). */
const val DISABLED_CONTENT_ALPHA = 0.38f

val LocalEngramaColors = staticCompositionLocalOf { LightEngramaColors }
