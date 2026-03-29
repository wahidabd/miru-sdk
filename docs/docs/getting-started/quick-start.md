---
sidebar_position: 2
title: Quick Start
---

# Quick Start

This guide walks you through building a simple feature using Miru SDK's clean architecture pattern — from API call to screen in 6 steps.

## 1. Initialize the SDK

```kotlin
// Application.kt or shared entry point
MiruSdkInitializer.initialize(
    MiruSdkConfig(
        networkConfig = NetworkConfig(
            baseUrl = "https://api.yourapp.com/v1/",
            enableLogging = BuildConfig.DEBUG
        ),
        enableLogging = true,
        tokenProvider = MyTokenProvider(),     // optional
        additionalModules = listOf(appModule) // your Koin modules
    )
)
```

## 2. Define Domain Layer

The domain layer has zero external dependencies — only pure Kotlin.

```kotlin
// domain/model/User.kt
data class User(
    val id: Int,
    val name: String,
    val email: String
)

// domain/repository/UserRepository.kt — interface only
interface UserRepository {
    suspend fun getUsers(): Resource<List<User>>
    suspend fun getUserById(id: Int): Resource<User>
}

// domain/usecase/GetUsersUseCase.kt
class GetUsersUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(): Resource<List<User>> = repository.getUsers()
}
```

## 3. Implement Data Layer

The data layer implements domain interfaces with concrete data sources.

```kotlin
// data/model/UserDto.kt — API response model
@Serializable
data class UserDto(
    val id: Int,
    val name: String,
    val email: String
)

// data/mapper/UserMapper.kt
class UserMapper : Mapper<UserDto, User> {
    override fun map(from: UserDto) = User(
        id = from.id,
        name = from.name,
        email = from.email
    )
}

// data/source/UserApi.kt — remote data source
class UserApi(httpClient: HttpClient) : ApiService(httpClient) {
    suspend fun getUsers(): Resource<ApiResponse<List<UserDto>>> =
        get("users")
    suspend fun getUserById(id: Int): Resource<ApiResponse<UserDto>> =
        get("users/$id")
}

// data/repository/UserRepositoryImpl.kt
class UserRepositoryImpl(
    private val api: UserApi,
    private val mapper: UserMapper
) : UserRepository {

    override suspend fun getUsers(): Resource<List<User>> =
        api.getUsers().map { response ->
            response.data?.map { mapper.map(it) } ?: emptyList()
        }

    override suspend fun getUserById(id: Int): Resource<User> =
        api.getUserById(id).map { response ->
            mapper.map(response.data!!)
        }
}
```

## 4. Create ViewModel

Choose the helper that fits your use case. Here's the simplest approach using `collectResource()`:

```kotlin
data class UserListState(
    val selectedUser: User? = null
)

sealed interface UserListEvent {
    data class ShowError(val message: String) : UserListEvent
}

class UserListViewModel(
    private val getUsersUseCase: GetUsersUseCase
) : BaseViewModel<UserListState, UserListEvent>(UserListState()) {

    private val _users = MutableStateFlow<Resource<List<User>>>(Resource.Loading())
    val users = _users.asStateFlow()

    fun loadUsers() = collectResource(_users) { getUsersUseCase() }
}
```

:::tip
See [ViewModel Patterns](/docs/guides/viewmodel-patterns) for a comparison of all 5 helpers.
:::

## 5. Build the Screen

Use `MiruResourceView` to eliminate `when (resource)` boilerplate:

```kotlin
@Composable
fun UserListScreen(viewModel: UserListViewModel = koinViewModel()) {
    val usersResource by viewModel.users.collectAsStateWithLifecycle()

    MiruResourceView(
        resource = usersResource,
        loadingMessage = "Loading users...",
        onRetry = { viewModel.loadUsers() }
    ) { users ->
        LazyColumn {
            items(users, key = { it.id }) { user ->
                MiruCard {
                    Text(user.name, style = MiruTheme.typography.titleMedium)
                    Text(user.email, style = MiruTheme.typography.bodySmall)
                }
            }
        }
    }
}
```

## 6. Wire Dependencies

Bind all layers via Koin:

```kotlin
val userModule = module {
    // Data
    single { UserMapper() }
    single { UserApi(get()) }
    single<UserRepository> { UserRepositoryImpl(get(), get()) }

    // Domain
    factory { GetUsersUseCase(get()) }

    // Presentation
    viewModel { UserListViewModel(get()) }
}
```

Pass `userModule` to `MiruSdkInitializer`:

```kotlin
MiruSdkInitializer.initialize(
    MiruSdkConfig(
        // ...
        additionalModules = listOf(userModule)
    )
)
```

## What's Next?

- [Architecture](/docs/getting-started/architecture) — understand the dependency graph and layer rules
- [ViewModel Patterns](/docs/guides/viewmodel-patterns) — learn all 5 async helpers
- [Theming](/docs/guides/theming) — customize colors and typography
- [Error Handling](/docs/guides/error-handling) — typed exceptions and global error handling
