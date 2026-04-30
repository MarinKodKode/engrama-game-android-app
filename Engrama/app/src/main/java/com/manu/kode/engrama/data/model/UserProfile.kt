package com.manu.kode.engrama.data.model

data class UserProfile(
    val name: String = "Jugador",
    val division: Division = Division.PLUTON,
    val uid: String = java.util.UUID.randomUUID().toString()
)