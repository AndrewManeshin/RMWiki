package com.example.rmwiki.characters.presentation

sealed class CharacterUi {

    data class Base(
        private val name: String,
        private val status: String,
        private val imageUrl: String
    ) : CharacterUi()


    data class FullScreenError(
        private val message: String
    ) : CharacterUi()

    data class BottomError(
        private val message: String
    ) : CharacterUi()

    object BottomProgress : CharacterUi()

    object FullScreenProgress : CharacterUi()
}