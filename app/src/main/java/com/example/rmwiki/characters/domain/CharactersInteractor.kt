package com.example.rmwiki.characters.domain

interface CharactersInteractor {

    suspend fun init(): CharactersResult

    suspend fun fetchCharacters(): CharactersResult

    fun needToLoadMoreData(lastVisibleItemPosition: Int): Boolean

    class Base() : CharactersInteractor {

        override suspend fun init(): CharactersResult {
            TODO("Not yet implemented")
        }

        override suspend fun fetchCharacters(): CharactersResult {
            TODO("Not yet implemented")
        }

        override fun needToLoadMoreData(lastVisibleItemPosition: Int): Boolean {
            TODO("Not yet implemented")
        }
    }
}