package com.example.rmwiki.characters.presentation

import com.example.rmwiki.characters.domain.CharacterItem
import com.example.rmwiki.characters.domain.DomainException

interface CharacterUiMapper<T : CharacterUi> : CharacterItem.Mapper<T> {

    class Base : CharacterUiMapper<CharacterUi.Base> {

        override fun map(
            name: String,
            status: String,
            imageUrl: String,
            exception: DomainException?
        ): CharacterUi.Base = CharacterUi.Base(name, status, imageUrl)
    }

    class FullScreenError : CharacterUiMapper<CharacterUi.FullScreenError> {

        override fun map(
            name: String,
            status: String,
            imageUrl: String,
            exception: DomainException?
        ): CharacterUi.FullScreenError {
            //TODO refactoring by DRY
            return when (exception) {
                is DomainException.NoInternetConnection -> CharacterUi.FullScreenError("No internet connection")
                else -> CharacterUi.FullScreenError("Service is unavailable")
            }
        }
    }

    class BottomError : CharacterUiMapper<CharacterUi.BottomError> {

        override fun map(
            name: String,
            status: String,
            imageUrl: String,
            exception: DomainException?
        ): CharacterUi.BottomError {
            //TODO refactoring by DRY
            return when (exception) {
                is DomainException.NoInternetConnection -> CharacterUi.BottomError("No internet connection")
                else -> CharacterUi.BottomError("Service is unavailable")
            }
        }
    }
}