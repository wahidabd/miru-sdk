---
sidebar_position: 4
title: UI Components
---

# UI Components Module

Presentation layer module with ready-to-use Material 3 composables, full theming support, and `MiruResourceView` for declarative loading states.

```kotlin
implementation("com.github.wahidabd.miru-sdk:ui-components:<version>")
```

## Theming

Customize the entire UI library per project:

```kotlin
MiruTheme(
    colorScheme = MiruColorScheme(
        primary = Color(0xFF1E88E5),
        secondary = Color(0xFFFF6F00),
        background = Color(0xFFFAFAFA),
        surface = Color(0xFFFFFFFF),
        error = Color(0xFFD32F2F),
        onPrimary = Color.White,
        onBackground = Color(0xFF212121),
        // ... your brand colors
    ),
    typography = MiruTypography(
        titleLarge = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold),
        titleMedium = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.SemiBold),
        bodyLarge = TextStyle(fontSize = 16.sp),
        bodyMedium = TextStyle(fontSize = 14.sp),
        // ... your typography
    )
) {
    // All Miru components inherit these values
    MiruButton(text = "Submit", onClick = { })
}
```

Access theme values anywhere inside the `MiruTheme` scope:

```kotlin
Text(
    text = "Hello",
    color = MiruTheme.colorScheme.primary,
    style = MiruTheme.typography.titleMedium
)
```

:::tip
See [Theming Guide](/docs/guides/theming) for a complete walkthrough.
:::

## MiruResourceView

Eliminates repetitive `when (resource)` boilerplate. Works with any `StateFlow<Resource<T>>`:

```kotlin
val usersResource by viewModel.users.collectAsStateWithLifecycle()

MiruResourceView(
    resource = usersResource,
    loadingMessage = "Loading users...",
    onRetry = { viewModel.loadUsers() }
) { users ->
    LazyColumn {
        items(users, key = { it.id }) { user ->
            UserCard(user)
        }
    }
}
```

### Custom Loading & Error

```kotlin
MiruResourceView(
    resource = feedResource,
    onRetry = { viewModel.refresh() },
    onLoading = { MyCustomShimmer() },
    onError = { message -> MyCustomErrorBanner(message) }
) { data ->
    FeedContent(data)
}
```

### Parameters

| Parameter | Type | Description |
|---|---|---|
| `resource` | `Resource<T>` | The resource state to render |
| `modifier` | `Modifier` | Optional modifier |
| `loadingMessage` | `String?` | Message shown during loading |
| `onRetry` | `(() -> Unit)?` | Retry callback for error state |
| `onLoading` | `(@Composable () -> Unit)?` | Custom loading composable |
| `onError` | `(@Composable (String) -> Unit)?` | Custom error composable |
| `content` | `@Composable (T) -> Unit` | Content rendered on success |

:::info
`MiruResourceView` is agnostic to the data source. Whether populated by `collectResource`, `collectFlow`, `collectFlowResource`, `execute`, or `collect` — it works the same.
:::

## Available Components

### Buttons

```kotlin
MiruButton(
    text = "Submit",
    onClick = { },
    enabled = true,
    loading = false
)
```

### Text Fields

```kotlin
MiruTextField(
    value = text,
    onValueChange = { text = it },
    label = "Email",
    placeholder = "Enter your email"
)

MiruPasswordField(
    value = password,
    onValueChange = { password = it },
    label = "Password"
)

MiruSearchField(
    value = query,
    onValueChange = { query = it },
    onSearch = { performSearch(query) }
)
```

### Top Bar

```kotlin
MiruTopBar(
    title = "Products",
    onBackClick = { navController.popBackStack() }
)

MiruSearchTopBar(
    query = searchQuery,
    onQueryChange = { searchQuery = it },
    onSearch = { performSearch() }
)
```

### Cards

```kotlin
MiruCard(onClick = { }) {
    Text("Card content")
}

MiruInfoCard(
    title = "Order Status",
    subtitle = "Your order is being processed"
)
```

### Dialogs

```kotlin
MiruAlertDialog(
    title = "Delete Item",
    message = "Are you sure?",
    onConfirm = { deleteItem() },
    onDismiss = { }
)

MiruConfirmationDialog(
    title = "Confirm Action",
    onConfirm = { },
    onCancel = { }
)

MiruLoadingDialog(message = "Processing...")
```

### Bottom Sheet

```kotlin
MiruBottomSheet(
    visible = showSheet,
    onDismiss = { showSheet = false }
) {
    // Sheet content
}
```

### Loading & Empty States

```kotlin
MiruFullScreenLoading(message = "Loading...")
MiruLoadingIndicator()
MiruShimmerEffect()
MiruEmptyView(message = "No items found")
MiruErrorView(message = "Something went wrong", onRetry = { })
```

### Images

```kotlin
MiruNetworkImage(
    url = "https://example.com/image.jpg",
    contentDescription = "Product image"
)
```

### Spacers

```kotlin
MiruSpacer(size = 16)           // Both directions
MiruVerticalSpacer(size = 8)    // Vertical only
MiruHorizontalSpacer(size = 12) // Horizontal only
```

## Component List

| Component | Description |
|---|---|
| `MiruButton` | Primary action button with loading state |
| `MiruTextField` | Text input field |
| `MiruPasswordField` | Password input with visibility toggle |
| `MiruSearchField` | Search input with action |
| `MiruTopBar` | App bar with back navigation |
| `MiruSearchTopBar` | App bar with search functionality |
| `MiruBottomSheet` | Modal bottom sheet |
| `MiruCard` | Clickable card container |
| `MiruInfoCard` | Card with title and subtitle |
| `MiruAlertDialog` | Alert dialog with confirm/dismiss |
| `MiruLoadingDialog` | Loading overlay dialog |
| `MiruConfirmationDialog` | Confirmation dialog |
| `MiruErrorView` | Error state with retry |
| `MiruEmptyView` | Empty state message |
| `MiruFullScreenLoading` | Full screen loading indicator |
| `MiruLoadingIndicator` | Inline loading indicator |
| `MiruShimmerEffect` | Shimmer placeholder effect |
| `MiruNetworkImage` | Coil-powered network image |
| `MiruResourceView` | Declarative Resource state renderer |
| `MiruSpacer` | Spacing utility |
