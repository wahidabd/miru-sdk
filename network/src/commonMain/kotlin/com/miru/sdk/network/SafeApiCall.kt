package com.miru.sdk.network

import com.miru.sdk.core.AppException
import com.miru.sdk.core.Resource
import io.github.aakira.napier.Napier
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.TimeoutCancellationException

/**
 * Safely execute an API call and wrap the result in a [Resource].
 * Handles various exception types and maps them to appropriate [AppException] subclasses.
 *
 * @param T The expected response type
 * @param apiCall The suspend function that makes the API call
 * @return [Resource.Success] with the data or [Resource.Error] with an [AppException]
 */
suspend inline fun <reified T> safeApiCall(
    crossinline apiCall: suspend () -> T
): Resource<T> {
    return try {
        val result = apiCall()
        Resource.Success(result)
    } catch (e: ResponseException) {
        val statusCode = e.response.status.value
        val description = e.response.status.description

        val exception = when (statusCode) {
            400 -> AppException.ApiException(
                code = 400,
                errorMessage = "Bad Request"
            )
            401 -> AppException.UnauthorizedException(
                message = "Unauthorized"
            )
            403 -> AppException.ForbiddenException(
                message = "Forbidden"
            )
            404 -> AppException.NotFoundException(
                message = "Resource not found"
            )
            in 500..599 -> AppException.ServerException(
                code = statusCode,
                message = description
            )
            else -> AppException.ApiException(
                code = statusCode,
                errorMessage = description
            )
        }

        Napier.e(tag = "SafeApiCall", message = "HTTP Error $statusCode", throwable = e)
        Resource.Error(exception)
    } catch (e: TimeoutCancellationException) {
        Napier.e(tag = "SafeApiCall", message = "Timeout", throwable = e)
        Resource.Error(AppException.TimeoutException())
    } catch (e: IOException) {
        Napier.e(tag = "SafeApiCall", message = "Network error", throwable = e)
        Resource.Error(AppException.NetworkException(message = "Network error: ${e.message}"))
    } catch (e: kotlinx.serialization.SerializationException) {
        Napier.e(tag = "SafeApiCall", message = "Serialization error", throwable = e)
        Resource.Error(AppException.SerializationException(message = "Failed to parse response: ${e.message}"))
    } catch (e: AppException) {
        Napier.e(tag = "SafeApiCall", message = "App exception", throwable = e)
        Resource.Error(e)
    } catch (e: Exception) {
        Napier.e(tag = "SafeApiCall", message = "Unexpected error", throwable = e)
        Resource.Error(AppException.UnknownException(throwable = e))
    }
}
