package com.manu.kode.engrama.data.dataset

import com.manu.kode.engrama.core.logging.AppLog
import com.manu.kode.engrama.domain.model.TriviaCategory
import com.manu.kode.engrama.domain.model.TriviaDifficulty
import com.manu.kode.engrama.domain.model.TriviaQuestion
import com.manu.kode.engrama.domain.model.WordCategory
import com.manu.kode.engrama.domain.model.WordEntry
import com.manu.kode.engrama.domain.model.fromRaw
import kotlinx.serialization.json.Json
import java.util.UUID

/** Entrada externa: los elementos con raw desconocido se descartan con Log.e. */
object DatasetParser {

    private const val TAG = "DatasetParser"

    private val json = Json { ignoreUnknownKeys = true }

    fun parseWords(text: String): List<WordEntry> =
        json.decodeFromString<WordBankDto>(text).words.mapNotNull { dto ->
            val category = fromRaw<WordCategory>(dto.category)
            if (category == null) {
                AppLog.e(TAG, "Palabra descartada \"${dto.word}\": categoría \"${dto.category}\"")
                null
            } else {
                WordEntry(
                    id = UUID.randomUUID().toString(),
                    word = dto.word,
                    category = category,
                    difficulty = dto.difficulty
                )
            }
        }

    fun parseTrivia(text: String): List<TriviaQuestion> =
        json.decodeFromString<TriviaBankDto>(text).questions.mapNotNull { dto ->
            val category = fromRaw<TriviaCategory>(dto.category)
            val difficulty = fromRaw<TriviaDifficulty>(dto.difficulty)
            if (category == null || difficulty == null) {
                AppLog.e(TAG, "Pregunta descartada \"${dto.id}\": ${dto.category}/${dto.difficulty}")
                null
            } else {
                TriviaQuestion(
                    id = dto.id,
                    question = dto.question,
                    answer = dto.answer,
                    options = dto.options,
                    category = category,
                    difficulty = difficulty
                )
            }
        }
}
