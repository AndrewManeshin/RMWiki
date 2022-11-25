package com.example.rmwiki.characters.domain

interface CharactersInteractor {

    suspend fun init(): List<CharacterItem>

    suspend fun fetchCharacters(): List<CharacterItem>

    fun needToLoadMoreData(lastVisibleItemPosition: Int): Boolean

    class Base() : CharactersInteractor {

        override suspend fun init(): List<CharacterItem> {
            TODO("Not yet implemented")
        }

        override suspend fun fetchCharacters(): List<CharacterItem> {
            TODO("Not yet implemented")
        }

        override fun needToLoadMoreData(lastVisibleItemPosition: Int): Boolean {
            TODO("Not yet implemented")
        }
    }
}