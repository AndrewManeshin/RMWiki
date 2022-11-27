package com.example.rmwiki.characters.domain

interface CharactersInteractor {

     suspend fun init(): List<CharacterItem>

     suspend fun fetchCharacters(): List<CharacterItem>

    suspend fun needToLoadMoreData(lastVisibleItemPosition: Int): Boolean

    class Base(
        private val repository: CharactersRepository
    ) : CharactersInteractor {

        override suspend fun init(): List<CharacterItem> {
            return repository.allCharacters()
        }

        override suspend fun fetchCharacters(): List<CharacterItem> = try {
            repository.fetchCharacters()
            repository.allCharacters()
        } catch (e: DomainException) {
            ArrayList<CharacterItem>().apply {
                addAll(init())
                add(CharacterItem.Failure(e))
            }
        }

        override suspend fun needToLoadMoreData(lastVisibleItemPosition: Int): Boolean =
            with(repository.allCharacters()) {
            return isNotEmpty() || size - 1 == lastVisibleItemPosition
        }
    }
}