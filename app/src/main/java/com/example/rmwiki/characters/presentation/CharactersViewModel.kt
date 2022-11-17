package com.example.rmwiki.characters.presentation

import androidx.lifecycle.*
import com.example.rmwiki.characters.domain.CharactersInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CharactersViewModel(
    private val communication: CharactersCommunication,
    private val interactor: CharactersInteractor
) : Init, FetchCharacters, Observe, ViewModel() {

    private var lastVisibleItemPos = -1

    override fun observeList(owner: LifecycleOwner, observer: Observer<List<CharacterUi>>) {
        communication.observeList(owner, observer)
    }

    override fun init(isFirstRun: Boolean) {
        if (isFirstRun) {
            viewModelScope.launch(Dispatchers.IO) {
                interactor.init()
            }
        }
    }

    override fun fetchCharacters() {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.fetchCharacters()
        }
    }

    override fun fetchMoreCharacters(lastVisibleItemPosition: Int) {
        if (lastVisibleItemPosition != lastVisibleItemPos) {
            if (interactor.needToLoadMoreData(lastVisibleItemPosition)) {
                lastVisibleItemPos = lastVisibleItemPosition
                fetchCharacters()
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