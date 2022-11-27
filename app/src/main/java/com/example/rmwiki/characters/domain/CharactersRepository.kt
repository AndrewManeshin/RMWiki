package com.example.rmwiki.characters.domain

interface CharactersRepository {

    suspend fun allCharacters(): ArrayList<CharacterItem.Success>

    suspend fun fetchCharacters(): List<CharacterItem.Success>
}
