package com.manu.kode.engrama.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import com.manu.kode.engrama.domain.model.AppearanceMode

/** Resuelve si el tema es oscuro según la apariencia guardada (iOS AppSettings.appearance). */
@Composable
fun AppearanceMode.isDark(): Boolean = when (this) {
    AppearanceMode.SYSTEM -> isSystemInDarkTheme()
    AppearanceMode.LIGHT -> false
    AppearanceMode.DARK -> true
}

/**
 * Roles de Material3 derivados de los tokens iOS, para que los componentes M3
 * (Button, FilterChip, Switch, AlertDialog…) no usen la paleta base morada.
 * Mapeo en engrama-port-notes/08-cp2b-plan.md §9.3.
 *
 * Los roles `*Fixed` / `*FixedDim` (primaryFixed, onPrimaryFixed…) no se mapean a propósito:
 * conservan la paleta base de M3. Ningún componente de la app los usa; si alguno los
 * necesita, se mapean entonces.
 */
internal fun EngramaColors.toColorScheme(dark: Boolean): ColorScheme {
    val accentContainer = blue.copy(alpha = ACCENT_CONTAINER_ALPHA)
    val base = if (dark) darkColorScheme() else lightColorScheme()
    return base.copy(
        primary = blue,
        onPrimary = white,
        primaryContainer = accentContainer,
        onPrimaryContainer = blue,
        inversePrimary = blue,
        secondary = gray,
        onSecondary = white,
        secondaryContainer = secondaryBackground,
        onSecondaryContainer = label,
        tertiary = blue,
        onTertiary = white,
        tertiaryContainer = accentContainer,
        onTertiaryContainer = blue,
        background = background,
        onBackground = label,
        surface = background,
        onSurface = label,
        surfaceVariant = secondaryBackground,
        onSurfaceVariant = secondaryLabel,
        surfaceTint = background,
        inverseSurface = label,
        inverseOnSurface = background,
        error = red,
        onError = white,
        errorContainer = red.copy(alpha = ACCENT_CONTAINER_ALPHA),
        onErrorContainer = red,
        outline = tertiaryLabel,
        outlineVariant = tertiaryLabel,
        scrim = black,
        surfaceBright = background,
        surfaceDim = secondaryBackground,
        surfaceContainerLowest = background,
        surfaceContainerLow = secondaryBackground,
        surfaceContainer = secondaryBackground,
        surfaceContainerHigh = secondaryBackground,
        surfaceContainerHighest = secondaryBackground,
    )
}

@Composable
fun EngramaTheme(
    appearance: AppearanceMode = AppearanceMode.SYSTEM,
    content: @Composable () -> Unit
) {
    val dark = appearance.isDark()
    val colors = if (dark) DarkEngramaColors else LightEngramaColors

    CompositionLocalProvider(LocalEngramaColors provides colors) {
        MaterialTheme(
            colorScheme = colors.toColorScheme(dark),
            typography = Typography,
            content = content
        )
    }
}

/** Acceso a los tokens: `EngramaTheme.colors.blue`. */
object EngramaTheme {
    val colors: EngramaColors
        @Composable
        @ReadOnlyComposable
        get() = LocalEngramaColors.current
}
