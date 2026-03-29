package com.miru.sdk.core

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Sealed class representing an async resource with three possible states.
 *
 * @param T the type of data held by this resource
 */
sealed class Resource<T> {
    /**
     * Represents a successful resource state.
     *
     * @param data the successful result
     */
    data class Success<T>(val data: T) : Resource<T>()

    /**
     * Represents a failed resource state.
     *
     * @param exception the exception that caused the failure
     * @param data optional data available before the error occurred
     */
    data class Error<T>(val exception: AppException, val data: T? = null) : Resource<T>()

    /**
     * Represents a loading resource state.
     *
     * @param data optional data available while loading
     */
    data class Loading<T>(val data: T? = null) : Resource<T>()
}

/**
 * Transforms the data within this Resource using the provided function.
 *
 * @param transform the transformation function
 * @return a new Resource with the transformed data
 */
inline fun <T, R> Resource<T>.map(transform: (T) -> R): Resource<R> = when (this) {
    is Resource.Success -> Resource.Success(transform(data))
    is Resource.Error -> Resource.Error(exception, data?.let(transform))
    is Resource.Loading -> Resource.Loading(data?.let(transform))
}

/**
 * Transforms the data within this Resource using a function that returns a Resource.
 *
 * @param transform the transformation function
 * @return the Resource returned by the transformation
 */
inline fun <T, R> Resource<T>.flatMap(transform: (T) -> Resource<R>): Resource<R> = when (this) {
    is Resource.Success -> transform(data)
    is Resource.Error -> Resource.Error(exception, null)
    is Resource.Loading -> Resource.Loading(null)
}

/**
 * Executes the given block if this Resource is a Success.
 *
 * @param block the block to execute with the data
 * @return this Resource
 */
inline fun <T> Resource<T>.onSuccess(block: (T) -> Unit): Resource<T> {
    if (this is Resource.Success) {
        block(data)
    }
    return this
}

/**
 * Executes the given block if this Resource is an Error.
 *
 * @param block the block to execute with the exception and data
 * @return this Resource
 */
inline fun <T> Resource<T>.onError(block: (exception: AppException, data: T?) -> Unit): Resource<T> {
    if (this is Resource.Error) {
        block(exception, data)
    }
    return this
}

/**
 * Executes the given block if this Resource is Loading.
 *
 * @param block the block to execute with the optional data
 * @return this Resource
 */
inline fun <T> Resource<T>.onLoading(block: (data: T?) -> Unit): Resource<T> {
    if (this is Resource.Loading) {
        block(data)
    }
    return this
}

/**
 * Returns the data if this Resource is a Success, otherwise null.
 *
 * @return the data or null
 */
fun <T> Resource<T>.getOrNull(): T? = when (this) {
    is Resource.Success -> data
    is Resource.Error -> data
    is Resource.Loading -> data
}

/**
 * Returns the data if this Resource is a Success, otherwise the default value.
 *
 * @param defaultValue the value to return if not a Success
 * @return the data or the default value
 */
fun <T> Resource<T>.getOrDefault(defaultValue: T): T = getOrNull() ?: defaultValue

/**
 * Returns the data if this Resource is a Success, otherwise throws the exception.
 *
 * @return the data
 * @throws AppException if this Resource is an Error
 */
fun <T> Resource<T>.getOrThrow(): T = when (this) {
    is Resource.Success -> data
    is Resource.Error -> throw exception
    is Resource.Loading -> throw IllegalStateException("Cannot get value from Loading state")
}

/**
 * Returns true if this Resource is a Success.
 */
fun <T> Resource<T>.isSuccess(): Boolean = this is Resource.Success

/**
 * Returns true if this Resource is an Error.
 */
fun <T> Resource<T>.isError(): Boolean = this is Resource.Error

/**
 * Returns true if this Resource is Loading.
 */
fun <T> Resource<T>.isLoading(): Boolean = this is Resource.Loading

/**
 * Converts a Flow into a Resource flow that emits Loading first, then maps emissions to Success or Error.
 *
 * @return a Flow<Resource<T>> that emits Loading first
 */
fun <T> Flow<T>.asResource(): Flow<Resource<T>> = map { value ->
    try {
        Resource.Success(value) as Resource<T>
    } catch (e: Exception) {
        Resource.Error(
            exception = if (e is AppException) e else AppException.UnknownException(e),
            data = null
        )
    }
}
