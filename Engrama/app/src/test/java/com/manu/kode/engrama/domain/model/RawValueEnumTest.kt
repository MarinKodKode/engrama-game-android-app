package com.manu.kode.engrama.domain.model

import com.manu.kode.engrama.testutil.LogSinkRule
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class RawValueEnumTest {

    @get:Rule
    val log = LogSinkRule()

    /** Fija los rawValues de iOS en orden y comprueba el round-trip. */
    private inline fun <reified T> assertRaws(expected: List<String>)
            where T : Enum<T>, T : RawValueEnum {
        val entries = enumValues<T>().toList()
        assertEquals(expected, entries.map { it.rawValue })
        entries.forEach { assertEquals(it, fromRaw<T>(it.rawValue)) }

        log.entries.clear()
        assertNull(fromRaw<T>("valor-inexistente"))
        assertNull("no debe aceptar el name de Kotlin", fromRaw<T>(entries.first().name))
        assertEquals(2, log.warnings.size)
        assertTrue(log.warnings.first().message.contains("valor-inexistente"))

        log.entries.clear()
        assertNull(fromRaw<T>(null))
        assertTrue("null no se loguea", log.entries.isEmpty())
    }

    @Test
    fun division() = assertRaws<Division>(
        listOf(
            "División Plutón", "División Mercurio", "División Venus", "División Tierra",
            "División Marte", "División Júpiter", "División Saturno", "División Urano",
            "División Neptuno", "División Luna", "División Sol"
        )
    )

    @Test
    fun gameType() = assertRaws<GameType>(listOf("math", "language", "trivia", "flashcards"))

    @Test
    fun gameOperation() = assertRaws<GameOperation>(
        listOf(
            "+", "-", "*", "/", "tipo_palabra", "trivia",
            "flashcards_classic", "flashcards_quiz", "flashcards_spacedRepetition"
        )
    )

    @Test
    fun appearanceMode() = assertRaws<AppearanceMode>(listOf("system", "light", "dark"))

    @Test
    fun profileAvatar() {
        assertRaws<ProfileAvatar>(
            listOf(
                "avatar_white", "avatar_blue", "avatar_red", "avatar_aqua",
                "avatar_pink", "avatar_green", "avatar_gold", "avatar_silver"
            )
        )
        assertEquals(ProfileAvatar.BLUE, ProfileAvatar.DEFAULT)
    }

    @Test
    fun triviaDifficulty() = assertRaws<TriviaDifficulty>(listOf("easy", "medium", "hard"))

    @Test
    fun triviaCategory() = assertRaws<TriviaCategory>(
        listOf("quimica", "fisica", "biologia", "geografia", "español", "matematicas", "historia", "miscelaneos")
    )

    @Test
    fun wordCategory() = assertRaws<WordCategory>(
        listOf("verbo", "sustantivo", "adjetivo", "adverbio", "articulo", "pronombre", "preposicion", "conjuncion")
    )

    @Test
    fun uppercaseName_isNotAccepted() {
        assertNull(fromRaw<GameType>("MATH"))
        assertNull(fromRaw<Division>("PLUTON"))
    }
}
