package com.example.rmwiki.characters.domain

sealed class CharacterItem {

    interface Mapper<T> {
        fun map(
            name: String,
            status: String,
            imageUrl: String,
            exception: DomainException?
        ): T
    }

    abstract fun <T> map(mapper: Mapper<T>): T

    data class Success(
        private val name: String,
        private val status: String,
        private val imageUrl: String
    ) : CharacterItem() {

        override fun <T> map(mapper: Mapper<T>) = mapper.map(name, status, imageUrl, null)
    }

    data class Failure(private val exception: DomainException) : CharacterItem() {

        override fun <T> map(mapper: Mapper<T>) = mapper.map("", "", "", exception)
    }
}