---
sidebar_position: 4
title: Clean Architecture
---

# Clean Architecture Guide

Every feature built with Miru SDK follows Clean Architecture with three layers. This guide shows how to structure your code.

## Layer Rules

1. **Domain** has zero dependencies — only pure Kotlin
2. **Data** depends on Domain (implements interfaces)
3. **Presentation** depends on Domain (consumes use cases)
4. Data and Presentation never depend on each other

```
Presentation → Domain ← Data
```

## Step-by-Step: Building a Feature

### 1. Domain Layer

Start with the domain — define what your feature does, not how.

**Model** — pure business object:

```kotlin
data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val inStock: Boolean
)
```

**Repository interface** — contract for data access:

```kotlin
interface ProductRepository {
    suspend fun getProducts(): Resource<List<Product>>
    suspend fun getProduct(id: Int): Resource<Product>
    fun observeProducts(): Flow<Resource<List<Product>>>
}
```

**Use case** — single-responsibility business operation:

```kotlin
class GetProductsUseCase(private val repository: ProductRepository) {
    suspend operator fun invoke(): Resource<List<Product>> =
        repository.getProducts()
}

class ObserveProductsUseCase(private val repository: ProductRepository) {
    operator fun invoke(): Flow<Resource<List<Product>>> =
        repository.observeProducts()
}
```

### 2. Data Layer

Implement the domain contracts with real data sources.

**DTO** — maps 1:1 to the API response:

```kotlin
@Serializable
data class ProductDto(
    val id: Int,
    val name: String,
    val price: Double,
    @SerialName("in_stock") val inStock: Boolean
)
```

**Mapper** — converts DTO to domain model:

```kotlin
class ProductMapper : Mapper<ProductDto, Product> {
    override fun map(from: ProductDto) = Product(
        id = from.id,
        name = from.name,
        price = from.price,
        inStock = from.inStock
    )
}
```

**Remote data source** — API calls:

```kotlin
class ProductApi(httpClient: HttpClient) : ApiService(httpClient) {
    suspend fun getProducts(): Resource<ApiResponse<List<ProductDto>>> =
        get("products")
    suspend fun getProduct(id: Int): Resource<ApiResponse<ProductDto>> =
        get("products/$id")
}
```

**Repository implementation**:

```kotlin
class ProductRepositoryImpl(
    private val api: ProductApi,
    private val mapper: ProductMapper
) : ProductRepository {

    override suspend fun getProducts(): Resource<List<Product>> =
        api.getProducts().map { response ->
            response.data?.map { mapper.map(it) } ?: emptyList()
        }

    override suspend fun getProduct(id: Int): Resource<Product> =
        api.getProduct(id).map { response ->
            mapper.map(response.data!!)
        }

    override fun observeProducts(): Flow<Resource<List<Product>>> =
        flow {
            emit(getProducts())
        }
}
```

### 3. Presentation Layer

Consume domain use cases in a ViewModel, render in a Composable.

**ViewModel**:

```kotlin
class ProductListViewModel(
    private val getProductsUseCase: GetProductsUseCase
) : BaseViewModel<Unit, ProductEvent>(Unit) {

    private val _products = MutableStateFlow<Resource<List<Product>>>(Resource.Loading())
    val products = _products.asStateFlow()

    init { loadProducts() }

    fun loadProducts() = collectResource(_products) { getProductsUseCase() }
}
```

**Screen**:

```kotlin
@Composable
fun ProductListScreen(viewModel: ProductListViewModel = koinViewModel()) {
    val resource by viewModel.products.collectAsStateWithLifecycle()

    MiruResourceView(
        resource = resource,
        onRetry = { viewModel.loadProducts() }
    ) { products ->
        LazyColumn {
            items(products, key = { it.id }) { product ->
                ProductCard(product)
            }
        }
    }
}
```

### 4. DI Wiring

```kotlin
val productModule = module {
    single { ProductMapper() }
    single { ProductApi(get()) }
    single<ProductRepository> { ProductRepositoryImpl(get(), get()) }
    factory { GetProductsUseCase(get()) }
    viewModel { ProductListViewModel(get()) }
}
```

## Folder Structure

```
your-feature/
├── data/
│   ├── model/          ProductDto.kt
│   ├── mapper/         ProductMapper.kt
│   ├── source/         ProductApi.kt
│   └── repository/     ProductRepositoryImpl.kt
├── domain/
│   ├── model/          Product.kt
│   ├── repository/     ProductRepository.kt
│   └── usecase/        GetProductsUseCase.kt
└── presentation/
    ├── viewmodel/      ProductListViewModel.kt
    └── ui/             ProductListScreen.kt
```

## Key Principles

- **Domain is king** — all business rules live here, with no framework dependencies
- **Repository pattern** — domain defines the interface, data provides the implementation
- **Use cases are optional** — for simple CRUD, the ViewModel can call the repository directly. Use cases shine when there's actual business logic to encapsulate.
- **DTOs stay in data** — never expose API models to the presentation layer
- **Mapper per entity** — keep conversions explicit and testable
