package com.urbantransit.offline.data.model

/**
 * Generic wrapper representing UI and data state with Loading, Success, and Error.
 */
sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val message: String, val cause: Throwable? = null) : Resource<Nothing>()
    data object Loading : Resource<Nothing>()
}
