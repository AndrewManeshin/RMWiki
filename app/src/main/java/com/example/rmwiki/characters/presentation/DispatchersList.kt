package com.example.rmwiki.characters.presentation

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface DispatchersList {

    fun io(): CoroutineDispatcher

    class Base: DispatchersList {
        override fun io() = Dispatchers.IO
    }
}