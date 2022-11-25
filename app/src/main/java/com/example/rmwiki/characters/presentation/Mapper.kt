package com.example.rmwiki.characters.presentation

interface Mapper<R, S> {

    fun map(source: S) : R
}