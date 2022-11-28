package com.example.rmwiki.characters.data

import com.example.rmwiki.characters.domain.CharacterItem

class CharacterDataToDomain : CharacterData.Mapper<CharacterItem.Success> {

    override fun map(name: String, status: String, imageUrl: String) =
        CharacterItem.Success(name, status, imageUrl)
}