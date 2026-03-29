package com.miru.sdk.network.model

import kotlinx.serialization.Serializable

/**
 * Error response model from the API.
 *
 * @property message General error message
 * @property errors Field-specific validation errors mapped by field name
 * @property code Error code for machine-readable error handling
 */
@Serializable
data class ErrorResponse(
    val message: String? = null,
    val errors: Map<String, List<String>>? = null,
    val code: Int? = null
)
