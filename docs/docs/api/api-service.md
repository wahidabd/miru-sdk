---
sidebar_position: 5
title: ApiService
---

# ApiService

Abstract base class for remote data sources. Provides HTTP methods that automatically wrap responses in `Resource<T>`.

**Module:** `:network`

## Definition

```kotlin
abstract class ApiService(protected val httpClient: HttpClient)
```

## HTTP Methods

### GET

```kotlin
suspend fun getUsers(): Resource<ApiResponse<List<UserDto>>> =
    get("users")

suspend fun getUser(id: Int): Resource<ApiResponse<UserDto>> =
    get("users/$id")

suspend fun searchUsers(query: String): Resource<ApiResponse<List<UserDto>>> =
    get("users/search", params = mapOf("q" to query))
```

### POST

```kotlin
suspend fun createUser(request: CreateUserRequest): Resource<ApiResponse<UserDto>> =
    post("users", body = request)
```

### PUT

```kotlin
suspend fun updateUser(id: Int, request: UpdateUserRequest): Resource<ApiResponse<UserDto>> =
    put("users/$id", body = request)
```

### PATCH

```kotlin
suspend fun patchUser(id: Int, request: PatchUserRequest): Resource<ApiResponse<UserDto>> =
    patch("users/$id", body = request)
```

### DELETE

```kotlin
suspend fun deleteUser(id: Int): Resource<ApiResponse<Unit>> =
    delete("users/$id")
```

## Method Signatures

| Method | Signature |
|---|---|
| `get` | `suspend fun get<T>(path: String, params: Map<String, String>? = null): Resource<T>` |
| `post` | `suspend fun post<T>(path: String, body: Any? = null, params: Map<String, String>? = null): Resource<T>` |
| `put` | `suspend fun put<T>(path: String, body: Any? = null, params: Map<String, String>? = null): Resource<T>` |
| `patch` | `suspend fun patch<T>(path: String, body: Any? = null, params: Map<String, String>? = null): Resource<T>` |
| `delete` | `suspend fun delete<T>(path: String, params: Map<String, String>? = null): Resource<T>` |

## Error Handling

All methods internally use `safeApiCall` which catches exceptions and maps them to typed `AppException`:

- HTTP 401 → `AppException.UnauthorizedException`
- HTTP 403 → `AppException.ForbiddenException`
- HTTP 404 → `AppException.NotFoundException`
- HTTP 5xx → `AppException.ServerException(code)`
- Timeout → `AppException.TimeoutException`
- No network → `AppException.NetworkException`
- Other → `AppException.UnknownException`

## Complete Example

```kotlin
// Data source (data layer)
class ProductApi(httpClient: HttpClient) : ApiService(httpClient) {
    suspend fun getProducts(): Resource<ApiResponse<List<ProductDto>>> =
        get("products")
    suspend fun getProduct(id: Int): Resource<ApiResponse<ProductDto>> =
        get("products/$id")
    suspend fun createProduct(body: CreateProductRequest): Resource<ApiResponse<ProductDto>> =
        post("products", body = body)
}

// Repository implementation (data layer)
class ProductRepositoryImpl(
    private val api: ProductApi,
    private val mapper: ProductMapper
) : ProductRepository {

    override suspend fun getProducts(): Resource<List<Product>> =
        api.getProducts().map { response ->
            response.data?.map { mapper.map(it) } ?: emptyList()
        }
}
```
