package com.manu.kode.engrama.ui.theme

import androidx.compose.ui.unit.dp

/**
 * Espaciados, radios y tamaños tomados de las vistas iOS (pt → dp 1:1).
 * Solo lo que usan las pantallas ya re-estiladas; crece por pantalla.
 */
object Dimens {
    // Home (HomeView.swift)
    val homeTopBarHorizontal = 16.dp
    val homeTopBarTop = 8.dp
    val homeTopBarImage = 36.dp
    val homeTopBarImageInset = 8.dp
    val homeContentHorizontal = 20.dp
    val homeContentTop = 24.dp
    val homeSectionSpacing = 28.dp
    val homeLogo = 40.dp
    /** Espaciado por defecto de un VStack() de SwiftUI sin `spacing`. */
    val homeLogoTitleSpacing = 8.dp
    val homeHeaderBottom = 40.dp

    // SectionMenuView
    val sectionTitleSpacing = 12.dp
    val sectionTitleStart = 4.dp
    val sectionRowSpacing = 10.dp

    // MenuRowView
    val menuRowPadding = 16.dp
    val menuRowSpacing = 16.dp
    val menuRowCorner = 16.dp
    val menuRowIconBox = 52.dp
    val menuRowIconBoxCorner = 12.dp
    /**
     * SF Symbol a 22 pt ≈ glifo de ~20 pt. Un Material Symbol de 24 dp tiene un glifo de ~20 dp,
     * así que se usa 24 dp para igualar el tamaño visible.
     */
    val menuRowIcon = 24.dp
    val menuRowTextSpacing = 3.dp
    /**
     * `chevron.right` a 14 pt ≈ glifo de 12 × 7 pt. El chevron_right de Material ocupa ~50 % × 30 %
     * de su caja, así que 24 dp da un glifo de ~12 × 7 dp.
     */
    val menuRowChevron = 24.dp
    val cardShadowRadius = 8.dp
    val cardShadowOffsetY = 2.dp
}
