package com.example.rmwiki.characters.presentation

import androidx.lifecycle.*
import com.example.rmwiki.characters.domain.CharactersInteractor
import kotlinx.coroutines.launch

class CharactersViewModel(
    private val communication: CharactersCommunication,
    private val interactor: CharactersInteractor,
    private val mapper: CharactersUiMapper,
    private val dispatchers: DispatchersList
) : Init, FetchCharacters, Observe, ViewModel() {

    private var lastVisibleItemPos = -1

    override fun observeList(owner: LifecycleOwner, observer: Observer<List<CharacterUi>>) {
        communication.observeList(owner, observer)
    }

    override fun init(isFirstRun: Boolean) {
        if (isFirstRun) {
            //TODO find solution to show fullscreen progress
            communication.showList(listOf(CharacterUi.FullScreenProgress))
            viewModelScope.launch(dispatchers.io()) {
                communication.showList(mapper.map(interactor.init()))
            }
        }
    }

    override fun fetchCharacters() {
        viewModelScope.launch(dispatchers.io()) {
            communication.showList(mapper.map(interactor.fetchCharacters()))
        }
    }

    override fun fetchMoreCharacters(lastVisibleItemPosition: Int) {
        if (lastVisibleItemPosition != lastVisibleItemPos) {
            viewModelScope.launch(dispatchers.io()) {
                if (interactor.needToLoadMoreData(lastVisibleItemPosition)) {
                    lastVisibleItemPos = lastVisibleItemPosition
                    fetchCharacters()
                }
            }
        }
    }
}

interface Observe {

    fun observeList(owner: LifecycleOwner, observer: Observer<List<CharacterUi>>)
}

interface FetchCharacters {

    fun fetchCharacters()

    fun fetchMoreCharacters(lastVisibleItemPosition: Int)
}

interface Init {

    fun init(isFirstRun: Boolean)
}