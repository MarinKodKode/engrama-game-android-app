package com.manu.kode.engrama.domain.model

/**
 * iOS: Core/Settings/UserProfile.swift:11-19. Sin division: se deriva de las sesiones
 * (ver [PlayerProgress]). uid lo genera y persiste una sola vez la capa de datos.
 */
data class UserProfile(
    val name: String = "Jugador",
    val uid: String = "",
    val avatarImageName: String = ProfileAvatar.DEFAULT.imageName,
    val username: String? = null
)
