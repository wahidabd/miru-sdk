---
sidebar_position: 2
title: Theming
---

# Theming Guide

Miru SDK's UI components use a centralized theming system based on `MiruTheme`. Customize colors, typography, and component styles per project.

## Setup

Wrap your root composable in `MiruTheme`:

```kotlin
@Composable
fun App() {
    MiruTheme(
        colorScheme = MiruColorScheme(
            primary = Color(0xFF1E88E5),
            secondary = Color(0xFFFF6F00),
            background = Color(0xFFFAFAFA),
            surface = Color(0xFFFFFFFF),
            error = Color(0xFFD32F2F),
            onPrimary = Color.White,
            onSecondary = Color.White,
            onBackground = Color(0xFF212121),
            onSurface = Color(0xFF212121),
            onError = Color.White
        ),
        typography = MiruTypography(
            titleLarge = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold),
            titleMedium = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.SemiBold),
            titleSmall = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium),
            bodyLarge = TextStyle(fontSize = 16.sp),
            bodyMedium = TextStyle(fontSize = 14.sp),
            bodySmall = TextStyle(fontSize = 12.sp),
            labelLarge = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium),
            labelSmall = TextStyle(fontSize = 11.sp)
        )
    ) {
        AppNavigation()
    }
}
```

## Accessing Theme Values

Inside any composable within the `MiruTheme` scope:

```kotlin
@Composable
fun MyComponent() {
    Text(
        text = "Hello",
        color = MiruTheme.colorScheme.primary,
        style = MiruTheme.typography.titleMedium
    )

    Box(
        modifier = Modifier.background(MiruTheme.colorScheme.surface)
    ) {
        // ...
    }
}
```

## Color Scheme

| Property | Usage |
|---|---|
| `primary` | Primary brand color — buttons, links, active states |
| `secondary` | Secondary brand color — FABs, accents |
| `background` | Page/screen background |
| `surface` | Card and dialog backgrounds |
| `error` | Error states, validation messages |
| `onPrimary` | Text/icon color on primary backgrounds |
| `onSecondary` | Text/icon color on secondary backgrounds |
| `onBackground` | Text/icon color on background |
| `onSurface` | Text/icon color on surfaces |
| `onError` | Text/icon color on error backgrounds |

## Typography

| Property | Typical Usage |
|---|---|
| `titleLarge` | Screen titles, hero text |
| `titleMedium` | Section headers, card titles |
| `titleSmall` | Subsection headers |
| `bodyLarge` | Primary body text |
| `bodyMedium` | Secondary body text |
| `bodySmall` | Captions, timestamps |
| `labelLarge` | Button text |
| `labelSmall` | Badges, chips |

## Dark Mode

Swap color schemes based on system preference:

```kotlin
@Composable
fun App() {
    val isDark = isSystemInDarkTheme()

    MiruTheme(
        colorScheme = if (isDark) darkColorScheme else lightColorScheme
    ) {
        AppNavigation()
    }
}

val lightColorScheme = MiruColorScheme(
    primary = Color(0xFF1E88E5),
    background = Color(0xFFFAFAFA),
    surface = Color.White,
    // ...
)

val darkColorScheme = MiruColorScheme(
    primary = Color(0xFF90CAF9),
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    // ...
)
```

## Component Styling

All Miru components automatically inherit theme values. For example, `MiruButton` uses `primary` for its background and `onPrimary` for its text. `MiruCard` uses `surface` as its background and `onSurface` for content.

If you need per-component overrides, most components accept standard Compose `Modifier` and style parameters.
