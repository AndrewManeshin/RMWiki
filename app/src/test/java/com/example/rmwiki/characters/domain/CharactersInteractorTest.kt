package com.example.rmwiki.characters.domain

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CharactersInteractorTest {

    private lateinit var repository: CharactersRepositoryTest
    private lateinit var interactor: CharactersInteractor

    @Before
    fun `set up`() {
        repository = CharactersRepositoryTest()
        interactor = CharactersInteractor.Base(
            repoitory = repository
        )
    }

    @Test
    fun `test init success`() {

        repository.changeExpectedCacheList(arrayListOf(CharacterItem.Success("1", "1", "1")))

        val actual = interactor.init()
        val expected = listOf(CharacterItem.Success("1", "1", "1"))

        assertEquals(expected, actual)
        assertEquals(1, repository.allCharactersCalledCount)
    }

    @Test
    fun `test load success case`()  = runBlocking {

        repository.changeExpectedCacheList(
            arrayListOf(
                CharacterItem.Success("1", "1", "1"),
                CharacterItem.Success("2", "2", "2"),
                CharacterItem.Success("3", "3", "3")
            )
        )

        val actual = interactor.needToLoadMoreData(2)
        val expected = true
        assertEquals(expected, actual)
        assertEquals(1, repository.allCharactersCalledCount)

        repository.changeExpectedCloudList(arrayListOf(
            CharacterItem.Success("4", "4", "4")
        ))

        val actualList = interactor.fetchCharacters()
        val expectedList = arrayListOf(
            CharacterItem.Success("1", "1", "1"),
            CharacterItem.Success("2", "2", "2"),
            CharacterItem.Success("3", "3", "3"),
            CharacterItem.Success("4", "4", "4")
        )

        assertEquals(expectedList, actualList)
        assertEquals(1, repository.fetchCharactersCalledCount)
        assertEquals(1, repository.allCharactersCalledCount)
    }

    @Test
    fun `test load fail case`()  = runBlocking {

        repository.changeExpectedCacheList(
            arrayListOf(
                CharacterItem.Success("1", "1", "1"),
                CharacterItem.Success("2", "2", "2"),
                CharacterItem.Success("3", "3", "3")
            )
        )

        val actual = interactor.needToLoadMoreData(2)
        val expected = true
        assertEquals(expected, actual)
        assertEquals(1, repository.allCharactersCalledCount)

        repository.expectingError(true)

        val actualList = interactor.fetchCharacters()
        val expectedList = arrayListOf(
            CharacterItem.Success("1", "1", "1"),
            CharacterItem.Success("2", "2", "2"),
            CharacterItem.Success("3", "3", "3"),
            CharacterItem.Failure(DomainException.NoInternetConnection())
        )

        assertEquals(expectedList, actualList)
        assertEquals(1, repository.fetchCharactersCalledCount)
        assertEquals(2, repository.allCharactersCalledCount)
    }

    private class CharactersRepositoryTest : CharactersRepository {

        private var expectedCacheList = arrayListOf<CharacterItem.Success>()
        private var expectedCloudList = listOf<CharacterItem.Success>()
        private var expectingError = false

        var allCharactersCalledCount = 0
        var fetchCharactersCalledCount = 0

        fun expectingError(error: Boolean) {
            expectingError = error
        }

        fun changeExpectedCacheList(list: ArrayList<CharacterItem.Success>) {
            expectedCacheList = list
        }

        fun changeExpectedCloudList(list: List<CharacterItem.Success>) {
            expectedCloudList = list
        }

        override suspend fun allCharacters(): ArrayList<CharacterItem.Success> {
            allCharactersCalledCount++
            return expectedCacheList
        }

        override suspend fun fetchCharacters(): List<CharacterItem.Success> {
            fetchCharactersCalledCount++
            if (expectingError) {
                throw DomainException.NoInternetConnection()
            }
            return expectedCloudList
        }
    }
}