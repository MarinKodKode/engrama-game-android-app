package com.manu.kode.engrama.data.dataset

import com.manu.kode.engrama.domain.model.TriviaCategory
import com.manu.kode.engrama.domain.model.TriviaDifficulty
import com.manu.kode.engrama.domain.model.WordCategory
import com.manu.kode.engrama.testutil.LogSinkRule
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.io.File

/** Conteos de 01-auditoria-main.md §2 (idénticos en iOS origin/develop). */
class DatasetParserTest {

    @get:Rule
    val log = LogSinkRule()

    private fun asset(name: String) = File("src/main/assets/$name").readText()

    @Test
    fun words_countsMatchIos() {
        val words = DatasetParser.parseWords(asset("words.json"))

        assertEquals(540, words.size)
        assertEquals(
            mapOf(
                WordCategory.VERBO to 90, WordCategory.SUSTANTIVO to 90, WordCategory.ADJETIVO to 60,
                WordCategory.ADVERBIO to 82, WordCategory.ARTICULO to 30, WordCategory.PRONOMBRE to 74,
                WordCategory.PREPOSICION to 74, WordCategory.CONJUNCION to 40
            ),
            words.groupingBy { it.category }.eachCount()
        )
        assertEquals(
            mapOf("easy" to 228, "medium" to 186, "hard" to 126),
            words.groupingBy { it.difficulty }.eachCount()
        )
        assertEquals("ids únicos", 540, words.map { it.id }.toSet().size)
        assertTrue(log.entries.isEmpty())
    }

    @Test
    fun trivia_countsMatchIos() {
        val questions = DatasetParser.parseTrivia(asset("trivia.json"))

        assertEquals(420, questions.size)
        assertEquals(
            mapOf(
                TriviaCategory.QUIMICA to 50, TriviaCategory.FISICA to 50, TriviaCategory.BIOLOGIA to 45,
                TriviaCategory.GEOGRAFIA to 45, TriviaCategory.ESPANOL to 50, TriviaCategory.MATEMATICAS to 60,
                TriviaCategory.HISTORIA to 60, TriviaCategory.MISCELANEOS to 60
            ),
            questions.groupingBy { it.category }.eachCount()
        )
        assertEquals(
            mapOf(TriviaDifficulty.EASY to 167, TriviaDifficulty.MEDIUM to 159, TriviaDifficulty.HARD to 94),
            questions.groupingBy { it.difficulty }.eachCount()
        )
        assertTrue(questions.all { it.options.size == 3 && it.answer in it.options })
        assertTrue(log.entries.isEmpty())
    }

    @Test
    fun unknownRaws_areDiscardedWithError() {
        val words = DatasetParser.parseWords(
            """{"words":[{"word":"correr","category":"verbo","difficulty":"easy"},
                         {"word":"x","category":"interjeccion","difficulty":"easy"}]}"""
        )
        val trivia = DatasetParser.parseTrivia(
            """{"questions":[{"id":"q1","question":"?","answer":"a","options":["a","b","c"],"category":"astrologia","difficulty":"easy"}]}"""
        )

        assertEquals(listOf("correr"), words.map { it.word })
        assertTrue(trivia.isEmpty())
        assertEquals(2, log.errors.size)
    }
}
