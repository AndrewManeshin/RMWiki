package com.example.rmwiki.characters.domain

sealed class CharactersResult {

    class Success(
        private val list: List<CharacterDomain>
    ) : CharactersResult()

    class Failure(private val message: String) : CharactersResult()
}