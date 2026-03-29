package com.miru.sdk.network

import com.miru.sdk.core.Resource
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody

/**
 * Base class for API services.
 * Provides common HTTP request methods wrapped in [Resource] for error handling.
 *
 * Usage:
 * ```kotlin
 * class UserApiService(httpClient: HttpClient) : ApiService(httpClient) {
 *     suspend fun getUsers(): Resource<List<User>> = get("users")
 *     suspend fun createUser(user: User): Resource<User> = post("users", body = user)
 * }
 * ```
 *
 * @property httpClient The Ktor [HttpClient] instance
 */
abstract class ApiService(
    protected val httpClient: HttpClient
) {
    /**
     * Perform a GET request.
     *
     * @param T The expected response type
     * @param path The endpoint path (appended to base URL)
     * @param queryParams Optional query parameters
     * @return [Resource] containing the response or an error
     */
    protected suspend inline fun <reified T> get(
        path: String,
        queryParams: Map<String, String> = emptyMap()
    ): Resource<T> {
        return safeApiCall {
            httpClient.get(path) {
                queryParams.forEach { (key, value) ->
                    parameter(key, value)
                }
            }.body<T>()
        }
    }

    /**
     * Perform a POST request.
     *
     * @param T The expected response type
     * @param path The endpoint path
     * @param body Optional request body
     * @param queryParams Optional query parameters
     * @return [Resource] containing the response or an error
     */
    protected suspend inline fun <reified T> post(
        path: String,
        body: Any? = null,
        queryParams: Map<String, String> = emptyMap()
    ): Resource<T> {
        return safeApiCall {
            httpClient.post(path) {
                queryParams.forEach { (key, value) ->
                    parameter(key, value)
                }
                if (body != null) {
                    setBody(body)
                }
            }.body<T>()
        }
    }

    /**
     * Perform a PUT request.
     *
     * @param T The expected response type
     * @param path The endpoint path
     * @param body Optional request body
     * @param queryParams Optional query parameters
     * @return [Resource] containing the response or an error
     */
    protected suspend inline fun <reified T> put(
        path: String,
        body: Any? = null,
        queryParams: Map<String, String> = emptyMap()
    ): Resource<T> {
        return safeApiCall {
            httpClient.put(path) {
                queryParams.forEach { (key, value) ->
                    parameter(key, value)
                }
                if (body != null) {
                    setBody(body)
                }
            }.body<T>()
        }
    }

    /**
     * Perform a PATCH request.
     *
     * @param T The expected response type
     * @param path The endpoint path
     * @param body Optional request body
     * @param queryParams Optional query parameters
     * @return [Resource] containing the response or an error
     */
    protected suspend inline fun <reified T> patch(
        path: String,
        body: Any? = null,
        queryParams: Map<String, String> = emptyMap()
    ): Resource<T> {
        return safeApiCall {
            httpClient.patch(path) {
                queryParams.forEach { (key, value) ->
                    parameter(key, value)
                }
                if (body != null) {
                    setBody(body)
                }
            }.body<T>()
        }
    }

    /**
     * Perform a DELETE request.
     *
     * @param T The expected response type
     * @param path The endpoint path
     * @param queryParams Optional query parameters
     * @return [Resource] containing the response or an error
     */
    protected suspend inline fun <reified T> delete(
        path: String,
        queryParams: Map<String, String> = emptyMap()
    ): Resource<T> {
        return safeApiCall {
            httpClient.delete(path) {
                queryParams.forEach { (key, value) ->
                    parameter(key, value)
                }
            }.body<T>()
        }
    }
}
