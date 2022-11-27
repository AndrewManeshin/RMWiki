package com.example.rmwiki.characters.presentation

abstract class BaseTest {

    protected class ManageResourcesTest : ManageResources {

        private var result = "No internet connection"

        fun changeResult(new: String) {
            result = new
        }

        override fun string(id: Int) = result
    }
}