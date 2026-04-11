---
sidebar_position: 2
title: Network
---

# Network Module

Data layer module providing Ktor-based HTTP client with automatic error mapping, token management, and type-safe API definitions.

```kotlin
implementation("io.github.wahidabd:miru-sdk-network:<version>")
```

## ApiService

Base class for all remote data sources. Provides HTTP methods that automatically map responses to `Resource<T>`:

```kotlin
class ProductApi(httpClient: HttpClient) : ApiService(httpClient) {

    suspend fun getProducts(): Resource<ApiResponse<List<ProductDto>>> =
        get("products")

    suspend fun getProduct(id: Int): Resource<ApiResponse<ProductDto>> =
        get("products/$id")

    suspend fun createProduct(body: CreateProductRequest): Resource<ApiResponse<ProductDto>> =
        post("products", body = body)

    suspend fun updateProduct(id: Int, body: UpdateProductRequest): Resource<ApiResponse<ProductDto>> =
        put("products/$id", body = body)

    suspend fun deleteProduct(id: Int): Resource<ApiResponse<Unit>> =
        delete("products/$id")
}
```

### Available HTTP Methods

| Method | Function | Parameters |
|---|---|---|
| GET | `get(path, params?)` | Query parameters |
| POST | `post(path, body?, params?)` | Request body + query params |
| PUT | `put(path, body?, params?)` | Request body + query params |
| PATCH | `patch(path, body?, params?)` | Request body + query params |
| DELETE | `delete(path, params?)` | Query parameters |

### Query Parameters

```kotlin
suspend fun searchProducts(query: String, page: Int): Resource<ApiResponse<List<ProductDto>>> =
    get("products/search", params = mapOf("q" to query, "page" to page.toString()))
```

## SafeApiCall

Internally, all API calls are wrapped in `safeApiCall` which catches exceptions and maps them to `AppException`:

| HTTP Status | Exception |
|---|---|
| 401 | `AppException.UnauthorizedException` |
| 403 | `AppException.ForbiddenException` |
| 404 | `AppException.NotFoundException` |
| 5xx | `AppException.ServerException(code)` |
| Timeout | `AppException.TimeoutException` |
| No connection | `AppException.NetworkException` |
| Other | `AppException.UnknownException` |

## Token Management

### TokenProvider

Implement the `TokenProvider` interface to handle auth tokens:

```kotlin
class MyTokenProvider : TokenProvider {
    override suspend fun getAccessToken(): String? =
        prefs.getString("access_token")

    override suspend fun getRefreshToken(): String? =
        prefs.getString("refresh_token")

    override suspend fun saveTokens(accessToken: String, refreshToken: String) {
        prefs.putString("access_token", accessToken)
        prefs.putString("refresh_token", refreshToken)
    }

    override suspend fun clearTokens() {
        prefs.remove("access_token")
        prefs.remove("refresh_token")
    }

    override suspend fun isLoggedIn(): Boolean =
        getAccessToken() != null
}
```

### TokenEventBus

Observe auth state changes globally:

```kotlin
TokenEventBus.events.collect { event ->
    when (event) {
        TokenEvent.ForceLogout -> navigateToLogin()
        TokenEvent.TokenExpired -> refreshToken()
        TokenEvent.TokenRefreshed -> retryRequest()
    }
}
```

## NetworkConfig

Configure the HTTP client at initialization:

```kotlin
NetworkConfig(
    baseUrl = "https://api.yourapp.com/v1/",
    enableLogging = BuildConfig.DEBUG,
    connectTimeoutMs = 30_000L,
    requestTimeoutMs = 30_000L,
)
```

## ApiResponse

Standard wrapper for API responses:

```kotlin
@Serializable
data class ApiResponse<T>(
    val data: T? = null,
    val message: String? = null,
    val status: Boolean = true
)
```
