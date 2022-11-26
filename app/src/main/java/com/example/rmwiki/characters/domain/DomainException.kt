package com.example.rmwiki.characters.domain

abstract class DomainException : Exception() {

    class NoInternetConnection : DomainException()
    class ServiceUnavailable : DomainException()
}