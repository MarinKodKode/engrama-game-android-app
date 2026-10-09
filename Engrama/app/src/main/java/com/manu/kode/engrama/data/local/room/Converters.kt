package com.manu.kode.engrama.data.local.room

import androidx.room.TypeConverter
import com.manu.kode.engrama.domain.model.GameOperation
import com.manu.kode.engrama.domain.model.GameType
import com.manu.kode.engrama.domain.model.fromRaw
import java.time.Instant

/** Room solo recibe datos validados: un raw desconocido lanza excepción. */
class Converters {

    @TypeConverter
    fun gameTypeToRaw(value: GameType): String = value.rawValue

    @TypeConverter
    fun rawToGameType(raw: String): GameType =
        fromRaw<GameType>(raw) ?: throw IllegalArgumentException("GameType desconocido en Room: \"$raw\"")

    @TypeConverter
    fun gameOperationToRaw(value: GameOperation): String = value.rawValue

    @TypeConverter
    fun rawToGameOperation(raw: String): GameOperation =
        fromRaw<GameOperation>(raw) ?: throw IllegalArgumentException("GameOperation desconocida en Room: \"$raw\"")

    @TypeConverter
    fun instantToEpochMillis(value: Instant): Long = value.toEpochMilli()

    @TypeConverter
    fun epochMillisToInstant(value: Long): Instant = Instant.ofEpochMilli(value)
}
