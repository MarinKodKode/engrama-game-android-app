package com.manu.kode.engrama.domain.model

/** iOS: Core/Settings/UserProfile.swift:53-63. */
enum class ProfileAvatar(override val rawValue: String) : RawValueEnum {
    WHITE("avatar_white"),
    BLUE("avatar_blue"),
    RED("avatar_red"),
    AQUA("avatar_aqua"),
    PINK("avatar_pink"),
    GREEN("avatar_green"),
    GOLD("avatar_gold"),
    SILVER("avatar_silver");

    val imageName: String
        get() = rawValue

    companion object {
        val DEFAULT = BLUE
    }
}
