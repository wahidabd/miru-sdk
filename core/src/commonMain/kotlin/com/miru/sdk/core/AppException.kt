package com.miru.sdk.core

/**
 * Base sealed class for all app exceptions.
 *
 * @param exceptionMessage the error message
 * @param exceptionCause the underlying cause exception
 */
sealed class AppException(
    exceptionMessage: String,
    exceptionCause: Throwable? = null
) : Exception(exceptionMessage, exceptionCause) {

    /**
     * Network-related exception.
     */
    data class NetworkException(
        override val message: String,
        override val cause: Throwable? = null
    ) : AppException(message, cause)

    /**
     * API error exception.
     *
     * @param code the HTTP status code
     * @param errorMessage the error message from the API
     * @param errorBody the error response body
     */
    data class ApiException(
        val code: Int,
        val errorMessage: String,
        val errorBody: String? = null
    ) : AppException("API Error: $code - $errorMessage")

    /**
     * 401 Unauthorized exception.
     */
    data class UnauthorizedException(
        override val message: String = "Unauthorized",
        override val cause: Throwable? = null
    ) : AppException(message, cause)

    /**
     * 403 Forbidden exception.
     */
    data class ForbiddenException(
        override val message: String = "Forbidden",
        override val cause: Throwable? = null
    ) : AppException(message, cause)

    /**
     * 404 Not Found exception.
     */
    data class NotFoundException(
        override val message: String = "Not Found",
        override val cause: Throwable? = null
    ) : AppException(message, cause)

    /**
     * Server error exception (5xx).
     *
     * @param code the HTTP status code
     */
    data class ServerException(
        val code: Int,
        override val message: String = "Server Error: $code",
        override val cause: Throwable? = null
    ) : AppException(message, cause)

    /**
     * Request timeout exception.
     */
    data class TimeoutException(
        override val message: String = "Request Timeout",
        override val cause: Throwable? = null
    ) : AppException(message, cause)

    /**
     * No internet connection exception.
     */
    data class NoInternetException(
        override val message: String = "No Internet Connection",
        override val cause: Throwable? = null
    ) : AppException(message, cause)

    /**
     * Serialization/Deserialization exception.
     */
    data class SerializationException(
        override val message: String = "Serialization Error",
        override val cause: Throwable? = null
    ) : AppException(message, cause)

    /**
     * Unknown exception.
     *
     * @param throwable the underlying cause exception
     */
    data class UnknownException(
        val throwable: Throwable? = null
    ) : AppException("Unknown Error: ${throwable?.message ?: "No message"}", throwable)
}
