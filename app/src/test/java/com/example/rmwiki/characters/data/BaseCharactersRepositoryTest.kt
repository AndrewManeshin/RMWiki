package com.example.rmwiki.characters.data

import com.example.rmwiki.characters.data.cache.CharactersCacheDataSource
import com.example.rmwiki.characters.data.cloud.CharactersCloudDataSource
import com.example.rmwiki.characters.domain.CharacterItem
import com.example.rmwiki.characters.domain.CharactersRepository
import com.example.rmwiki.characters.domain.DomainException
import com.example.rmwiki.characters.presentation.BaseTest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.net.UnknownHostException

class BaseCharactersRepositoryTest : BaseTest() {

    private lateinit var repository: CharactersRepository
    private lateinit var cacheDataSource: TestCharactersCacheDataSource
    private lateinit var cloudDataSource: TestCharactersCloudDataSource
    private lateinit var dispatchers: TestDispatchersList

    @Before
    fun `set up`() {
        cacheDataSource = TestCharactersCacheDataSource()
        cloudDataSource = TestCharactersCloudDataSource()

        cacheDataSource.replaceData(
            listOf(
                CharacterData(
                    id = 1U,
                    "Rick", "alive", "image",
                    page = 1U
                ),
                CharacterData(2U, "Morty", "alive", "image", 2U),
            )
        )
        cacheDataSource.lastCachedPage = 2

        dispatchers = TestDispatchersList()
        repository = BaseCharactersRepository(
            cacheDataSource,
            cloudDataSource,
            CharacterDataToDomain(),
            dispatchers
        )
    }

    @Test
    fun `test all characters`() = runBlocking {


        val actual = repository.allCharacters()
        val expected = listOf(
            CharacterItem.Success("Rick", "alive", "image"),
            CharacterItem.Success("Morty", "alive", "image"),
        )

        assertEquals(expected, actual)
        assertEquals(1, cacheDataSource.allCharactersCalledCount)
    }

    @Test
    fun `test fetch characters with cache Success`() = runBlocking {
        cloudDataSource.changeExpectedData(
            listOf(CharacterData(3U, "Jak", "alive", "image", 3U)),
            3
        )

        dispatchers.await()
        assertEquals(
            listOf(CharacterItem.Success("Jak", "alive", "image")),
            repository.fetchCharacters()
        )

        val actual = repository.allCharacters()
        val expected = listOf(
            CharacterItem.Success("Rick", "alive", "image"),
            CharacterItem.Success("Morty", "alive", "image"),
            CharacterItem.Success("Jak", "alive", "image"),
        )

        assertEquals(expected, actual)
        assertEquals(1, cacheDataSource.lastCachedPageCalledCount)
        assertEquals(1, cloudDataSource.fetchCharactersCalledCount)
        assertEquals(1, cacheDataSource.saveCharactersCalledCount)
        assertEquals(1, cacheDataSource.allCharactersCalledCount)
    }

    @Test(expected = DomainException.NoInternetConnection::class)
    fun `fetch characters with cache Failure`() = runBlocking {
        cloudDataSource.changeConnection(false)

        repository.fetchCharacters()

        assertEquals(1, cacheDataSource.lastCachedPageCalledCount)
        assertEquals(1, cloudDataSource.fetchCharactersCalledCount)
        assertEquals(0, cacheDataSource.saveCharactersCalledCount)
        assertEquals(0, cacheDataSource.allCharactersCalledCount)
    }

    private class TestCharactersCloudDataSource : CharactersCloudDataSource {

        private val characterData = mutableListOf<CharacterData>()
        private var page: Int = 0
        private var thereIsConnection = true
        var fetchCharactersCalledCount = 0

        fun changeConnection(connected: Boolean) {
            thereIsConnection = connected
        }

        fun changeExpectedData(data: List<CharacterData>, page: Int) {
            this.page = page
            characterData.clear()
            characterData.addAll(data)
        }

        override suspend fun fetchCharacters(page: Int): List<CharacterData> {
            fetchCharactersCalledCount++
            if (!thereIsConnection)
                throw UnknownHostException()
            return if (page == this.page) {
                characterData
            } else {
                emptyList()
            }
        }
    }

    private class TestCharactersCacheDataSource : CharactersCacheDataSource {

        private val data = ArrayList<CharacterData>()
        var allCharactersCalledCount = 0
        var saveCharactersCalledCount = 0
        var lastCachedPageCalledCount = 0
        var lastCachedPage = 0

        fun replaceData(newData: List<CharacterData>) {
            data.clear()
            data.addAll(newData)
        }

        override suspend fun allCharacters(): ArrayList<CharacterData> {
            allCharactersCalledCount++
            return data
        }

        override suspend fun saveCharacters(data: List<CharacterData>, page: Int) {
            saveCharactersCalledCount++
            this.data.addAll(data)
        }

        override suspend fun lastCachedPage(): Int {
            lastCachedPageCalledCount++
            return lastCachedPage
        }
    }
}