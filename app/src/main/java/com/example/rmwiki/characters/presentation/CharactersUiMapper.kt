package com.example.rmwiki.characters.presentation

import com.example.rmwiki.characters.domain.CharacterItem

class CharactersUiMapper: Mapper<List<CharacterUi>, List<CharacterItem>> {

    private val baseMapper = CharacterUiMapper.Base()
    private val fullScreenErrorMapper = CharacterUiMapper.FullScreenError()
    private val bottomErrorMapper = CharacterUiMapper.BottomError()

    override fun map(source: List<CharacterItem>): List<CharacterUi> {
        val result = ArrayList<CharacterUi>()
        when {
            source.size == 1 && source[0] is CharacterItem.Failure ->
                result.add(source[0].map(fullScreenErrorMapper))
            source.last() is CharacterItem.Success -> {
                result.addAll(
                    source.map {
                        it.map(baseMapper)
                    }
                )
                result.add(CharacterUi.BottomProgress)
            }
            source.last() is CharacterItem.Failure -> {
                source.forEach { item ->
                    if (item is CharacterItem.Success)
                        result.add(item.map(baseMapper))
                }
                result.add(source.last().map(bottomErrorMapper))
            }
        }
        return result
    }
}