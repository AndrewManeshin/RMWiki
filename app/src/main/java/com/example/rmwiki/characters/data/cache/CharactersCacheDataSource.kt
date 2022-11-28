package com.example.rmwiki.characters.data.cache

import com.example.rmwiki.characters.data.CharacterData

interface CharactersCacheDataSource {

    suspend fun allCharacters(): ArrayList<CharacterData>

    suspend fun saveCharacters(data: List<CharacterData>, page: Int)

    suspend fun lastCachedPage(): Int
}