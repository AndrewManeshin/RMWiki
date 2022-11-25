package com.example.rmwiki.characters.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

interface CharactersCommunication : Observe {

    fun showList(list: List<CharacterUi>)

    class Base : CharactersCommunication {

        private val liveData = MutableLiveData<List<CharacterUi>>()

        override fun showList(list: List<CharacterUi>) {
            liveData.postValue(list)
        }

        override fun observeList(owner: LifecycleOwner, observer: Observer<List<CharacterUi>>) {
            liveData.observe(owner, observer)
        }
    }
}