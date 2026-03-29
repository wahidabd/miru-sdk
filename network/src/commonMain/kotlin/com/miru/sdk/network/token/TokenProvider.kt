package com.miru.sdk.network.token

/**
 * Interface for managing authentication tokens.
 * Implementations should handle secure storage and retrieval of tokens.
 */
interface TokenProvider {
    /**
     * Retrieve the current access token.
     *
     * @return The access token, or null if not available
     */
    suspend fun getAccessToken(): String?

    /**
     * Retrieve the current refresh token.
     *
     * @return The refresh token, or null if not available
     */
    suspend fun getRefreshToken(): String?

    /**
     * Save access and refresh tokens.
     *
     * @param accessToken The access token to save
     * @param refreshToken The refresh token to save
     */
    suspend fun saveTokens(accessToken: String, refreshToken: String)

    /**
     * Clear all stored tokens.
     */
    suspend fun clearTokens()

    /**
     * Check if the user is currently logged in.
     *
     * @return true if an access token is available, false otherwise
     */
    suspend fun isLoggedIn(): Boolean
}
