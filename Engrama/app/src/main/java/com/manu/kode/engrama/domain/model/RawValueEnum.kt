package com.manu.kode.engrama.domain.model

import com.manu.kode.engrama.core.logging.AppLog

/** Enum persistido con el rawValue exacto de iOS. Nunca se persiste `name`. */
interface RawValueEnum {
    val rawValue: String
}

/**
 * Devuelve la constante cuyo rawValue coincide con [raw], o null.
 * Un valor desconocido se loguea; elegir un default es responsabilidad del llamador.
 */
inline fun <reified T> fromRaw(raw: String?): T? where T : Enum<T>, T : RawValueEnum {
    if (raw == null) return null
    val match = enumValues<T>().firstOrNull { it.rawValue == raw }
    if (match == null) {
        AppLog.w("RawValueEnum", "rawValue desconocido para ${T::class.java.simpleName}: \"$raw\"")
    }
    return match
}
