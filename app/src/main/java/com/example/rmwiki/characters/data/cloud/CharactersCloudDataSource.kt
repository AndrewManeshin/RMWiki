package com.example.rmwiki.characters.data.cloud

import com.example.rmwiki.characters.data.CharacterData

interface CharactersCloudDataSource {

    suspend fun fetchCharacters(page: Int): List<CharacterData>
}