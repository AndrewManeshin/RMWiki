package com.example.rmwiki.characters.data

class CharacterData(
    private val id: UShort,
    private val name: String,
    private val status: String,
    private val imageUrl: String,
    private val page: UShort
) {

    interface Mapper<T> {
        fun map(name: String, status: String, imageUrl: String): T
    }

    fun <T> map(mapper: Mapper<T>): T = mapper.map(name, status, imageUrl)
}