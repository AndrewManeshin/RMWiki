package com.example.rmwiki.characters.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CharactersViewModelTest {

    private lateinit var communication: TestCharactersCommunication
    private lateinit var interactor: TestCharactersInteractor
    private lateinit var viewModel: CharactersViewModel

    @Before
    fun `set-up`() {
        communication = TestCharactersCommunication()
        interactor = TestCharactersInteractor()
        viewModel = CharactersViewModel(communication = communication, interactor = interactor)
    }

    @Test
    fun `test init and re-init`() = runBlocking {

        interactor.changeExpectedResult(CharactersResult.Success())

        viewModel.init(isFirstRun = true)

        assertEquals(true, communication.charactersList[0] is CharacterUi.Progress)
        assertEquals(true, communication.charactersList[1] is CharacterUi.Base)
        assertEquals(2, communication.timesShowList)

        viewModel.init(isFirstRun = false)

        assertEquals(true, communication.charactersList[1] is CharacterUi.Base)
        assertEquals(2, communication.timesShowList)
        assertEquals(1, interactor.initCalledList.size)
        assertEquals(true, interactor.initCalledList[0] is CharactersResult.Success)
    }

    @Test
    fun `test fetch characters with success`() = runBlocking {

        interactor.changeExpectedResult(
            CharactersResult.Success(
                listOf(
                    CharacterDomain(
                        id = "1",
                        name = "Rick",
                        status = "alive",
                        image = "image"
                    )
                )
            )
        )

        viewModel.fetchCharacters()

        assertEquals(true, communication.charactersList[0] is CharacterUi.Progress)
        assertEquals(
            CharactersUi.Base(
                name = "Rick",
                status = "alive",
                image = "image"
            ), communication.charactersList[1]
        )
        assertEquals(2, communication.timesShowList)
    }

    @Test
    fun `test fetch characters with error`() = runBlocking {
        interactor.changeExpectedResult(CharactersResult.Failure("no internet connection"))

        viewModel.fetchCharacters()

        assertEquals(true, communication.charactersList[0] is CharacterUi.Progress)
        assertEquals(CharacterUi.Error("no internet connection"), communication.charactersList[1])
    }

    @Test
    fun `test fetch more characters with success`() = runBlocking {
        interactor.changeCountOfCharacters(2)

        interactor.changeExpectedResult(
            CharactersResult.Success(
                listOf(
                    CharacterDomain(
                        id = "1",
                        name = "Rick",
                        status = "alive",
                        image = "image"
                    )
                )
            )
        )

        viewModel.fetchCharacters()

        assertEquals(true, communication.charactersList[0] is CharacterUi.Progress)
        assertEquals(
            CharactersUi.Base(
                name = "Rick",
                status = "alive",
                image = "image"
            ), communication.charactersList[1]
        )

        interactor.changeExpectedResult(
            CharactersResult.Success(
                listOf(
                    CharacterDomain(
                        id = "2",
                        name = "Morty",
                        status = "alive",
                        image = "image"
                    )
                )
            )
        )
        viewModel.fetchMoreCharacters()

        assertEquals(true, communication.charactersList[2] is CharacterUi.BottomProgress)
        assertEquals(
            CharactersUi.Base(
                name = "Morty",
                status = "alive",
                image = "image"
            ), communication.charactersList[3]
        )
    }

    @Test
    fun `test first fetch is success and second is error`() = runBlocking {
        interactor.changeCountOfCharacters(2)

        interactor.changeExpectedResult(
            CharactersResult.Success(
                listOf(
                    CharacterDomain(
                        id = "1",
                        name = "Rick",
                        status = "alive",
                        image = "image"
                    )
                )
            )
        )

        viewModel.fetchCharacters()

        assertEquals(true, communication.charactersList[0] is CharacterUi.Progress)
        assertEquals(
            CharactersUi.Base(
                name = "Rick",
                status = "alive",
                image = "image"
            ), communication.charactersList[1]
        )

        interactor.changeExpectedResult(
            CharactersResult.Falure("no internet connection")
        )
        viewModel.fetchMoreCharacters()

        assertEquals(true, communication.charactersList[2] is CharacterUi.BottomProgress)
        assertEquals(
            CharactersUi.BottomError("no internet connection"), communication.charactersList[3]
        )
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

        private var result = CharactersResult.Success()
        private var countOfCharacters = 1

        val initCalledList = mutableListOf<CharactersResult>()

        fun changeCountOfCharacters(newCount: Int) {
            countOfCharacters = newCount
        }

        fun changeExpectedResult(newResult: CharactersResult) {
            result = newResult
        }

        override suspend fun init(isFirstRun: Boolean): CharactersResult {
            initCalledList.add(result)
            return result
        }

        override suspend fun fetchCharacters(): CharactersResult = result

        override fun needToLoadMoreData(lastVisibleItemPosition: Int): Boolean =
            lastVisibleItemPosition < countOfCharacters
    }
}