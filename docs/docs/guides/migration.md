---
sidebar_position: 5
title: Migration Guide
---

# Migration Guide

## Migrating to the Umbrella Module

If you're currently importing individual modules, you can simplify by switching to the umbrella module.

### Before

```kotlin
commonMain.dependencies {
    implementation("com.github.wahidabd.miru-sdk:core:<version>")
    implementation("com.github.wahidabd.miru-sdk:network:<version>")
    implementation("com.github.wahidabd.miru-sdk:ui-state:<version>")
    implementation("com.github.wahidabd.miru-sdk:ui-components:<version>")
    implementation("com.github.wahidabd.miru-sdk:navigation:<version>")
    implementation("com.github.wahidabd.miru-sdk:di:<version>")
    implementation("com.github.wahidabd.miru-sdk:firebase:<version>")
    implementation("com.github.wahidabd.miru-sdk:auth:<version>")
    implementation("com.github.wahidabd.miru-sdk:persistent:<version>")
}
```

### After

```kotlin
commonMain.dependencies {
    implementation("com.github.wahidabd.miru-sdk:miru-sdk:<version>")
}
```

The umbrella module uses `api()` dependencies, so all sub-modules are transitively available.

:::note
Third-party libraries used by sub-modules (like Koin, Ktor, Coil) are declared as `implementation` — not `api()`. If your app code directly uses these libraries, keep those dependencies in your app's `build.gradle.kts`.
:::

## Adopting ViewModel Helpers

### From manual Resource handling to collectResource

**Before:**

```kotlin
class MyViewModel(...) : BaseViewModel<MyState, MyEvent>(MyState()) {

    private val _data = MutableStateFlow<Resource<List<Item>>>(Resource.Loading())
    val data = _data.asStateFlow()

    fun load() {
        viewModelScope.launch {
            _data.value = Resource.Loading()
            _data.value = useCase()
        }
    }
}
```

**After:**

```kotlin
fun load() = collectResource(_data) { useCase() }
```

### From when-blocks to MiruResourceView

**Before:**

```kotlin
when (val res = resource) {
    is Resource.Loading -> MiruFullScreenLoading()
    is Resource.Error -> MiruErrorView(
        message = res.exception.message ?: "Error",
        onRetry = { viewModel.load() }
    )
    is Resource.Success -> {
        LazyColumn {
            items(res.data) { ItemCard(it) }
        }
    }
}
```

**After:**

```kotlin
MiruResourceView(resource = resource, onRetry = { viewModel.load() }) { data ->
    LazyColumn {
        items(data) { ItemCard(it) }
    }
}
```
