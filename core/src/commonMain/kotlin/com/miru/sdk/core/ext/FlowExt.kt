package com.miru.sdk.core.ext

import com.miru.sdk.core.AppException
import com.miru.sdk.core.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * Wraps a Flow's emissions in [Resource.Success], emitting [Resource.Loading] first.
 *
 * @return a Flow<Resource<T>> that emits Loading followed by Success for each value
 */
fun <T> Flow<T>.asResource(): Flow<Resource<T>> = flow {
    emit(Resource.Loading<T>())
    try {
        collect { value ->
            emit(Resource.Success(value))
        }
    } catch (e: Exception) {
        val exception = if (e is AppException) e else AppException.UnknownException(e)
        emit(Resource.Error(exception))
    }
}

/**
 * Throttles emissions of this Flow to at most one per [windowDuration].
 * Only the first emission within each window is passed through.
 *
 * @param windowDuration the minimum duration between emissions in milliseconds
 * @return a throttled Flow
 */
fun <T> Flow<T>.throttleFirst(windowDuration: Long): Flow<T> = flow {
    var lastEmitTime = 0L
    collect { value ->
        val currentTime = kotlinx.datetime.Clock.System.now().toEpochMilliseconds()
        if (currentTime - lastEmitTime >= windowDuration) {
            lastEmitTime = currentTime
            emit(value)
        }
    }
}

/**
 * Retries the Flow emissions with exponential backoff on failure.
 *
 * @param maxRetries the maximum number of retry attempts
 * @param initialDelay the initial delay between retries in milliseconds
 * @param factor the multiplication factor for exponential backoff
 * @return a Flow that retries with exponential backoff
 */
fun <T> Flow<T>.retryWithExponentialBackoff(
    maxRetries: Int = 3,
    initialDelay: Long = 100,
    factor: Double = 2.0
): Flow<T> = flow {
    var delayDuration = initialDelay
    var lastException: Exception? = null

    repeat(maxRetries + 1) { attempt ->
        try {
            collect { value ->
                emit(value)
            }
            return@flow
        } catch (e: Exception) {
            lastException = e
            if (attempt < maxRetries) {
                delay(delayDuration)
                delayDuration = (delayDuration * factor).toLong()
            }
        }
    }

    lastException?.let { throw it }
}

/**
 * Maps a [StateFlow] to a new [StateFlow] using the provided transform function.
 *
 * @param scope the [CoroutineScope] for managing the StateFlow
 * @param transform the transformation function
 * @return a new StateFlow with the transformed values
 */
fun <T, R> StateFlow<T>.mapState(
    scope: CoroutineScope,
    transform: (T) -> R
): StateFlow<R> = map(transform).stateIn(
    scope = scope,
    started = SharingStarted.Lazily,
    initialValue = transform(value)
)
