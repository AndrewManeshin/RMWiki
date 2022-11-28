package com.example.rmwiki.characters.data

import com.example.rmwiki.characters.data.cache.CharactersCacheDataSource
import com.example.rmwiki.characters.data.cloud.CharactersCloudDataSource
import com.example.rmwiki.characters.domain.CharacterItem
import com.example.rmwiki.characters.domain.CharactersRepository
import com.example.rmwiki.characters.domain.DomainException
import com.example.rmwiki.characters.presentation.DispatchersList
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.UnknownHostException

@OptIn(DelicateCoroutinesApi::class)
class BaseCharactersRepository(
    private val cacheDataSource: CharactersCacheDataSource,
    private val cloudDataSource: CharactersCloudDataSource,
    private val mapper: CharacterDataToDomain,
    dispatchers: DispatchersList
) : CharactersRepository {


    private var page = 0

    init {
        GlobalScope.launch(dispatchers.io()) {
            page = cacheDataSource.lastCachedPage()
        }
    }

    override suspend fun allCharacters(): ArrayList<CharacterItem.Success> {
        return ArrayList(cacheDataSource
            .allCharacters()
            .map { characterData -> characterData.map(mapper) }
        )
    }

    override suspend fun fetchCharacters(): List<CharacterItem.Success> {
        //TODO improve it
        try {
            return cloudDataSource.fetchCharacters(++page).run {
                cacheDataSource.saveCharacters(this, page)
                this.map { characterData ->
                    characterData.map(mapper)
                }
            }
        } catch (e: Exception) {
            throw when (e) {
                is UnknownHostException -> DomainException.NoInternetConnection
                else -> DomainException.ServiceUnavailable
            }
        }
    }
}