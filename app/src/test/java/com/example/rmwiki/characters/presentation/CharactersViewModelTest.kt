package com.example.rmwiki.characters.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.example.rmwiki.characters.domain.CharactersInteractor
import com.example.rmwiki.characters.domain.CharacterItem
import com.example.rmwiki.characters.domain.DomainException
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CharactersViewModelTest : BaseTest() {

    private lateinit var communication: TestCharactersCommunication
    private lateinit var interactor: TestCharactersInteractor
    private lateinit var dispatchers: TestDispatchersList
    private lateinit var resources: ManageResourcesTest
    private lateinit var viewModel: CharactersViewModel

    @Before
    fun `set-up`() {
        communication = TestCharactersCommunication()
        interactor = TestCharactersInteractor()
        dispatchers = TestDispatchersList()
        resources = ManageResourcesTest()
        viewModel = CharactersViewModel(
            communication = communication,
            interactor = interactor,
            mapper = CharactersUiMapper(
                CharacterUiMapper.Base(),
                CharacterUiMapper.FullScreenError(resources),
                CharacterUiMapper.BottomError(resources)
            ),
            dispatchers
        )
    }

    @Test
    fun `test init and re-init`(): Unit = runBlocking {

        interactor.changeExpectedResult(listOf(CharacterItem.Success("", "", "")))

        viewModel.init(isFirstRun = true)
        dispatchers.await()

        assertEquals(true, communication.charactersList[0] is CharacterUi.FullScreenProgress)
        assertEquals(true, communication.charactersList[1] is CharacterUi.Base)
        assertEquals(true, communication.charactersList[2] is CharacterUi.BottomProgress)
        assertEquals(2, communication.timesShowList)

        viewModel.init(isFirstRun = false)

        assertEquals(true, communication.charactersList[1] is CharacterUi.Base)
        assertEquals(2, communication.timesShowList)
        assertEquals(1, interactor.initCalledList.size)
        assertEquals(CharacterItem.Success("", "", ""), interactor.initCalledList[0])
    }

    @Test
    fun `test fetch characters with success`() = runBlocking {

        interactor.changeExpectedResult(
            listOf(
                CharacterItem.Success(
                    name = "Rick",
                    status = "alive",
                    imageUrl = "image"
                )
            )
        )

        viewModel.fetchCharacters()
        dispatchers.await()

        assertEquals(
            CharacterUi.Base(
                name = "Rick",
                status = "alive",
                imageUrl = "image"
            ), communication.charactersList[0]
        )
        assertEquals(true, communication.charactersList[1] is CharacterUi.BottomProgress)
        assertEquals(1, communication.timesShowList)
    }

    @Test
    fun `test fetch characters with fullscreen error`() = runBlocking {

        interactor.changeExpectedResult(listOf(CharacterItem.Failure(DomainException.NoInternetConnection)))

        viewModel.fetchCharacters()
        dispatchers.await()

        assertEquals(
            CharacterUi.FullScreenError("No internet connection"),
            communication.charactersList[0]
        )
    }

    @Test
    fun `test fetch more characters with success`() = runBlocking {
        interactor.changeCountOfCharacters(2)

        interactor.changeExpectedResult(
            listOf(
                CharacterItem.Success(
                    name = "Rick",
                    status = "alive",
                    imageUrl = "image"
                )

            )
        )

        viewModel.fetchCharacters()
        dispatchers.await()

        assertEquals(
            CharacterUi.Base(
                name = "Rick",
                status = "alive",
                imageUrl = "image"
            ), communication.charactersList[0]
        )
        assertEquals(true, communication.charactersList[1] is CharacterUi.BottomProgress)

        interactor.changeExpectedResult(
            listOf(
                CharacterItem.Success(
                    name = "Morty",
                    status = "alive",
                    imageUrl = "image"
                )
            )
        )
        viewModel.fetchMoreCharacters(1)
        dispatchers.await()

        assertEquals(
            CharacterUi.Base(
                name = "Morty",
                status = "alive",
                imageUrl = "image"
            ), communication.charactersList[2]
        )
        assertEquals(true, communication.charactersList[3] is CharacterUi.BottomProgress)
    }

    @Test
    fun `test first fetch is success and second is error`() = runBlocking {
        interactor.changeCountOfCharacters(2)

        interactor.changeExpectedResult(
            listOf(
                CharacterItem.Success(
                    name = "Rick",
                    status = "alive",
                    imageUrl = "image"
                )
            )
        )

        viewModel.fetchCharacters()
        dispatchers.await()

        assertEquals(
            CharacterUi.Base(
                name = "Rick",
                status = "alive",
                imageUrl = "image"
            ), communication.charactersList[0]
        )
        assertEquals(true, communication.charactersList[1] is CharacterUi.BottomProgress)

        interactor.changeExpectedResult(
            listOf(
                CharacterItem.Success(
                    name = "Rick",
                    status = "alive",
                    imageUrl = "image"
                ),
                CharacterItem.Failure(DomainException.NoInternetConnection)
            )
        )

        viewModel.fetchMoreCharacters(1)
        dispatchers.await()

        assertEquals(
            CharacterUi.Base(
                name = "Rick",
                status = "alive",
                imageUrl = "image"
            ), communication.charactersList[2]
        )
        assertEquals(
            CharacterUi.BottomError("No internet connection"), communication.charactersList[3]
        )
        assertEquals(2, communication.timesShowList)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private class TestDispatchersList: DispatchersList {

        private val scheduler = TestCoroutineScheduler()

        override fun io() = StandardTestDispatcher(scheduler)

        fun await() = scheduler.advanceUntilIdle()
    }

    private class TestCharactersCommunication : CharactersCommunication {

        val charactersList = mutableListOf<CharacterUi>()
        var timesShowList = 0

        override fun showList(list: List<CharacterUi>) {
            timesShowList++
            charactersList.addAll(list)
        }

        override fun observeList(owner: LifecycleOwner, observer: Observer<List<CharacterUi>>) =
            Unit
    }

    private class TestCharactersInteractor : CharactersInteractor {

        private var result: List<CharacterItem> = emptyList()
        private var countOfCharacters = 1

        val initCalledList = mutableListOf<CharacterItem>()

        fun changeCountOfCharacters(newCount: Int) {
            countOfCharacters = newCount
        }

        fun changeExpectedResult(newResult: List<CharacterItem>) {
            result = newResult
        }

        override suspend fun init(): List<CharacterItem> {
            initCalledList.addAll(result)
            return result
        }

        override suspend fun fetchCharacters(): List<CharacterItem> = result

        override suspend fun needToLoadMoreData(lastVisibleItemPosition: Int): Boolean =
            lastVisibleItemPosition < countOfCharacters
    }
}