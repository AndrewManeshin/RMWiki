package com.example.rmwiki.characters.domain

import com.example.rmwiki.R
import com.example.rmwiki.characters.presentation.ManageResources

abstract class DomainException : Exception() {

    abstract fun map(resources: ManageResources): String

    object NoInternetConnection : DomainException() {
        override fun map(resources: ManageResources) = resources.string(R.string.no_internet_connection)
    }

    object ServiceUnavailable : DomainException() {
        override fun map(resources: ManageResources) = resources.string(R.string.service_unavailable)
    }
}