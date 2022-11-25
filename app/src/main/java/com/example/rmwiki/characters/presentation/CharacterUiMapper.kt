package com.example.rmwiki.characters.presentation

import com.example.rmwiki.characters.domain.CharacterItem

interface CharacterUiMapper<T : CharacterUi> : CharacterItem.Mapper<T> {

    class Base : CharacterUiMapper<CharacterUi.Base> {

        override fun map(
            name: String, status: String, imageUrl: String, errorMessage: String
        ): CharacterUi.Base = CharacterUi.Base(name, status, imageUrl)
    }

    class FullScreenError : CharacterUiMapper<CharacterUi.FullScreenError> {

        override fun map(
            name: String, status: String, imageUrl: String, errorMessage: String
        ): CharacterUi.FullScreenError = CharacterUi.FullScreenError(errorMessage)
    }

    class BottomError : CharacterUiMapper<CharacterUi.BottomError> {

        override fun map(
            name: String, status: String, imageUrl: String, errorMessage: String
        ): CharacterUi.BottomError = CharacterUi.BottomError(errorMessage)
    }
}