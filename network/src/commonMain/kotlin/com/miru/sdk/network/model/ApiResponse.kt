package com.miru.sdk.network.model

import kotlinx.serialization.Serializable

/**
 * Generic wrapper for API responses from the backend.
 *
 * @param T The type of data contained in the response
 * @property data The actual response data
 * @property message Optional message from the API
 * @property status Whether the request was successful
 * @property code HTTP status code or custom API code
 * @property meta Optional pagination metadata
 */
@Serializable
data class ApiResponse<T>(
    val data: T? = null,
    val message: String? = null,
    val status: Boolean = false,
    val code: Int = 0,
    val meta: PaginationMeta? = null
)

/**
 * Pagination metadata for list responses.
 *
 * @property currentPage The current page number (1-indexed)
 * @property lastPage The last available page number
 * @property perPage Number of items per page
 * @property total Total number of items across all pages
 */
@Serializable
data class PaginationMeta(
    val currentPage: Int = 1,
    val lastPage: Int = 1,
    val perPage: Int = 10,
    val total: Int = 0
)
