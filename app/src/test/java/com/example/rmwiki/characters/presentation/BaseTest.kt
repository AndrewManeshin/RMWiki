package com.example.rmwiki.characters.presentation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler

abstract class BaseTest {

    protected class ManageResourcesTest : ManageResources {

        private var result = "No internet connection"

        fun changeResult(new: String) {
            result = new
        }

        override fun string(id: Int) = result
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    protected class TestDispatchersList : DispatchersList {

        private val scheduler = TestCoroutineScheduler()

        override fun io() = StandardTestDispatcher(scheduler)

        fun await() = scheduler.advanceUntilIdle()
    }
}