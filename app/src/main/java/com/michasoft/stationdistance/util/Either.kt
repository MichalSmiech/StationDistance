package com.michasoft.stationdistance.util

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

sealed interface Either<out Success, out Failure> {
    data class Success<out R>(val value: R) :
        Either<R, Nothing>

    data class Failure<out F>(val value: F) :
        Either<Nothing, F>
}

@OptIn(ExperimentalContracts::class)
inline fun <Success, Failure> Either<Success, Failure>.getOrElse(onFailure: (failure: Failure) -> Success): Success {
    contract {
        callsInPlace(onFailure, InvocationKind.AT_MOST_ONCE)
    }
    return when (this) {
        is Either.Success -> value
        is Either.Failure -> {
            onFailure(value)
        }
    }
}

fun <T> T.asFailure(): Either<Nothing, T> {
    return Either.Failure(this)
}

fun <T> T.asSuccess(): Either<T, Nothing> {
    return Either.Success(this)
}

@OptIn(ExperimentalContracts::class)
inline fun <Success, Failure> Either<Success, Failure>.onFailure(action: (error: Failure) -> Unit): Either<Success, Failure> {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (this is Either.Failure) {
        action(value)
    }
    return this
}