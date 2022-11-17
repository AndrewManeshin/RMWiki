package com.example.rmwiki.characters.presentation

sealed class CharacterUi {

    class Base(
        private val name: String,
        private val status: String,
        private val imageUrl: String
    ) : CharacterUi()


    class Error(
        private val message: String
    ) : CharacterUi()

    class BottomError(
        private val message: String
    ) : CharacterUi()

    class BottomProgress() : CharacterUi()
    class Progress() : CharacterUi()
}