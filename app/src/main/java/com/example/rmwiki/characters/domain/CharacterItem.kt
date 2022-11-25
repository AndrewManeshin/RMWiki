package com.example.rmwiki.characters.domain

sealed class CharacterItem {

    interface Mapper<T> {
        fun map(
            name: String,
            status: String,
            imageUrl: String,
            errorMessage: String
        ): T
    }

    abstract fun <T> map(mapper: Mapper<T>): T

    data class Success(
        private val name: String,
        private val status: String,
        private val imageUrl: String
    ) : CharacterItem() {

        override fun <T> map(mapper: Mapper<T>) = mapper.map(name, status, imageUrl, "")
    }

    class Failure(private val message: String) : CharacterItem() {

        override fun <T> map(mapper: Mapper<T>) = mapper.map("", "", "", message)
    }
}