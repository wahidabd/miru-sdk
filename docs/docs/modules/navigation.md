---
sidebar_position: 5
title: Navigation
---

# Navigation Module

Compose Navigation wrapper with safe navigation, custom transitions, and result passing.

```kotlin
implementation("io.github.wahidabd:miru-sdk-navigation:<version>")
```

## MiruNavHost

Wrapper around Compose `NavHost` with built-in transition animations:

```kotlin
@Composable
fun AppNavigation() {
    val navigationManager = remember { NavigationManagerImpl() }

    MiruNavigationHost(startDestination = "home") {
        composable("home") { HomeScreen() }
        composable("users") { UserListScreen() }
        composable("user/{id}") { backStackEntry ->
            val id = backStackEntry.getIntArgument("id")
            UserDetailScreen(userId = id)
        }
    }
}
```

## NavigationManager

Interface for programmatic navigation:

```kotlin
interface NavigationManager {
    fun navigate(route: String)
    fun navigateAndClear(route: String)
    fun popBackStack()
    fun popBackStack(route: String, inclusive: Boolean = false)
}
```

## Safe Navigation

Type-safe argument extraction from back stack entries:

```kotlin
// Extract typed arguments
val id: Int = backStackEntry.getIntArgument("id")
val name: String = backStackEntry.getStringArgument("name")
val optional: String? = backStackEntry.getOptionalStringArgument("filter")
```

## Navigation Transitions

Built-in transition animations:

```kotlin
MiruNavigationHost(
    startDestination = "home",
    transition = NavigationTransition.SlideHorizontal
) {
    // ...
}
```

Available transitions: `SlideHorizontal`, `SlideVertical`, `Fade`, `None`.

## NavigationResult

Pass results back between screens:

```kotlin
// Send result from screen B
navigationManager.setResult("selected_user", user)
navigationManager.popBackStack()

// Receive result in screen A
val result = navigationManager.getResult<User>("selected_user")
```
