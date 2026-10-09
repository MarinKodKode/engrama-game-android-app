package com.manu.kode.engrama.features.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.DpOffset
import com.manu.kode.engrama.R
import com.manu.kode.engrama.ui.theme.ACCENT_CONTAINER_ALPHA
import com.manu.kode.engrama.ui.theme.DISABLED_CONTENT_ALPHA
import com.manu.kode.engrama.ui.theme.Dimens
import com.manu.kode.engrama.ui.theme.EngramaTheme
import com.manu.kode.engrama.ui.theme.TINT_SECONDARY_ALPHA
import com.manu.kode.engrama.ui.theme.TINT_TERTIARY_ALPHA

/** iOS `SectionMenuView`: título de sección y sus filas. */
@Composable
fun SectionMenu(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(Dimens.sectionTitleSpacing)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = EngramaTheme.colors.secondaryLabel,
            modifier = Modifier.padding(start = Dimens.sectionTitleStart)
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(Dimens.sectionRowSpacing),
            content = content
        )
    }
}

/**
 * iOS `MenuRowView`. Textos y chevron en el tinte del Button (ver TINT_SECONDARY_ALPHA). Con `enabled = false` la fila no responde y se atenúa
 * (destinos aún no portados: Estudio y Perfil de Aprendizaje).
 */
@Composable
fun MenuRow(
    @DrawableRes icon: Int,
    label: String,
    subtitle: String,
    color: Color,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val colors = EngramaTheme.colors
    val shape = RoundedCornerShape(Dimens.menuRowCorner)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(if (enabled) 1f else DISABLED_CONTENT_ALPHA)
            .dropShadow(
                shape = shape,
                shadow = Shadow(
                    radius = Dimens.cardShadowRadius,
                    color = colors.cardShadow,
                    offset = DpOffset.Zero.copy(y = Dimens.cardShadowOffsetY)
                )
            )
            .clip(shape)
            .background(colors.background)
            .clickable(enabled = enabled, role = Role.Button, onClick = onClick)
            .padding(Dimens.menuRowPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.menuRowSpacing)
    ) {
        Box(
            modifier = Modifier
                .size(Dimens.menuRowIconBox)
                .clip(RoundedCornerShape(Dimens.menuRowIconBoxCorner))
                .background(color.copy(alpha = ACCENT_CONTAINER_ALPHA)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(Dimens.menuRowIcon)
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Dimens.menuRowTextSpacing)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
                color = colors.blue
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelLarge,
                color = colors.blue.copy(alpha = TINT_SECONDARY_ALPHA)
            )
        }

        Icon(
            painter = painterResource(R.drawable.ic_chevron_right),
            contentDescription = null,
            tint = colors.blue.copy(alpha = TINT_TERTIARY_ALPHA),
            modifier = Modifier.size(Dimens.menuRowChevron)
        )
    }
}
