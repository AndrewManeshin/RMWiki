package com.example.rmwiki.characters.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

interface CharactersCommunication : Observe {

    fun showList(list: List<CharacterUi>)

    class Base : CharactersCommunication {

        override fun showList(list: List<CharacterUi>) {
            TODO("Not yet implemented")
        }

        override fun observeList(owner: LifecycleOwner, observer: Observer<List<CharacterUi>>) {
            TODO("Not yet implemented")
        }
    }
}