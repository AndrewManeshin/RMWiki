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

    class FullScreenError(
        private val resources: ManageResources
    ) : CharacterUiMapper<CharacterUi.FullScreenError> {

        override fun map(
            name: String,
            status: String,
            imageUrl: String,
            exception: DomainException?
        ) = CharacterUi.FullScreenError(exception!!.map(resources))
    }

    class BottomError(
        private val resources: ManageResources
    ) : CharacterUiMapper<CharacterUi.BottomError> {

        override fun map(
            name: String,
            status: String,
            imageUrl: String,
            exception: DomainException?
        ) = CharacterUi.BottomError(exception!!.map(resources))
    }
}