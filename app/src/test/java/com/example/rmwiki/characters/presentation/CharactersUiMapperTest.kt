package com.example.rmwiki.characters.presentation

import com.example.rmwiki.characters.domain.CharacterItem
import org.junit.Assert.assertEquals
import org.junit.Test

class CharactersUiMapperTest {

    private val mapper = CharactersUiMapper()

    @Test
    fun `test fullscreen error`() {

        val actual = mapper.map(listOf(CharacterItem.Failure("error")))
        val expected  = listOf(CharacterUi.FullScreenError("error"))

        assertEquals(expected, actual)
    }

    @Test
    fun `test base item`() {

        val actual = mapper.map(listOf(
            CharacterItem.Success("1", "1", "1"),
            CharacterItem.Success("2", "2", "2"),
        ))
        val expected  = listOf(
            CharacterUi.Base("1", "1", "1"),
            CharacterUi.Base("2", "2", "2"),
            CharacterUi.BottomProgress,
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `test bottom error`() {

        val actual = mapper.map(listOf(
            CharacterItem.Success("1", "1", "1"),
            CharacterItem.Success("2", "2", "2"),
            CharacterItem.Failure("error")
        ))
        val expected  = listOf(
            CharacterUi.Base("1", "1", "1"),
            CharacterUi.Base("2", "2", "2"),
            CharacterUi.BottomError("error"),
        )

        assertEquals(expected, actual)
    }
}