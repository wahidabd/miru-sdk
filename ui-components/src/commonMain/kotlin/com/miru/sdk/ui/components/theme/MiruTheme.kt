package com.miru.sdk.ui.components.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

/**
 * CMP color scheme configuration.
 */
data class MiruColorScheme(
    val primary: Color,
    val secondary: Color,
    val background: Color,
    val surface: Color,
    val error: Color,
    val onPrimary: Color,
    val onSecondary: Color,
    val onBackground: Color,
    val onSurface: Color,
    val onError: Color,
)

/**
 * CMP typography configuration wrapping Material3 TextStyles.
 */
data class MiruTypography(
    val displayLarge: TextStyle,
    val displayMedium: TextStyle,
    val displaySmall: TextStyle,
    val headlineLarge: TextStyle,
    val headlineMedium: TextStyle,
    val headlineSmall: TextStyle,
    val titleLarge: TextStyle,
    val titleMedium: TextStyle,
    val titleSmall: TextStyle,
    val bodyLarge: TextStyle,
    val bodyMedium: TextStyle,
    val bodySmall: TextStyle,
    val labelLarge: TextStyle,
    val labelMedium: TextStyle,
    val labelSmall: TextStyle,
)

/**
 * CMP shapes configuration wrapping RoundedCornerShapes.
 */
data class MiruShapes(
    val small: RoundedCornerShape = RoundedCornerShape(8.dp),
    val medium: RoundedCornerShape = RoundedCornerShape(12.dp),
    val large: RoundedCornerShape = RoundedCornerShape(16.dp),
    val extraLarge: RoundedCornerShape = RoundedCornerShape(24.dp),
)

/**
 * Default light color scheme.
 */
fun lightMiruColors(): MiruColorScheme = MiruColorScheme(
    primary = Color(0xFF6200EE),
    secondary = Color(0xFF03DAC6),
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFFAFAFA),
    error = Color(0xFFB00020),
    onPrimary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFF000000),
    onBackground = Color(0xFF1F1F1F),
    onSurface = Color(0xFF1F1F1F),
    onError = Color(0xFFFFFFFF),
)

/**
 * Default dark color scheme.
 */
fun darkMiruColors(): MiruColorScheme = MiruColorScheme(
    primary = Color(0xFFBB86FC),
    secondary = Color(0xFF03DAC6),
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    error = Color(0xFFCF6679),
    onPrimary = Color(0xFF000000),
    onSecondary = Color(0xFF000000),
    onBackground = Color(0xFFFFFFFF),
    onSurface = Color(0xFFFFFFFF),
    onError = Color(0xFF000000),
)

/**
 * Default typography.
 */
fun defaultMiruTypography(): MiruTypography = MiruTypography(
    displayLarge = TextStyle(),
    displayMedium = TextStyle(),
    displaySmall = TextStyle(),
    headlineLarge = TextStyle(),
    headlineMedium = TextStyle(),
    headlineSmall = TextStyle(),
    titleLarge = TextStyle(),
    titleMedium = TextStyle(),
    titleSmall = TextStyle(),
    bodyLarge = TextStyle(),
    bodyMedium = TextStyle(),
    bodySmall = TextStyle(),
    labelLarge = TextStyle(),
    labelMedium = TextStyle(),
    labelSmall = TextStyle(),
)

/**
 * CompositionLocal for CMP colors.
 */
val LocalMiruColors: ProvidableCompositionLocal<MiruColorScheme> = compositionLocalOf {
    lightMiruColors()
}

/**
 * CompositionLocal for CMP typography.
 */
val LocalMiruTypography: ProvidableCompositionLocal<MiruTypography> = compositionLocalOf {
    defaultMiruTypography()
}

/**
 * CompositionLocal for CMP shapes.
 */
val LocalMiruShapes: ProvidableCompositionLocal<MiruShapes> = compositionLocalOf {
    MiruShapes()
}

/**
 * CMP Theme composable that wraps Material3 MaterialTheme.
 *
 * @param colorScheme The color scheme to use, defaults to system dark mode detection
 * @param typography The typography to use, defaults to Material3 default typography
 * @param shapes The shapes to use, defaults to CMP default shapes
 * @param darkTheme Whether to use dark theme, defaults to system setting
 * @param content The content to apply the theme to
 */
@Composable
fun MiruTheme(
    colorScheme: MiruColorScheme = if (isSystemInDarkTheme()) darkMiruColors() else lightMiruColors(),
    typography: MiruTypography = defaultMiruTypography(),
    shapes: MiruShapes = MiruShapes(),
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val material3ColorScheme = if (darkTheme) {
        darkColorScheme(
            primary = colorScheme.primary,
            secondary = colorScheme.secondary,
            background = colorScheme.background,
            surface = colorScheme.surface,
            error = colorScheme.error,
            onPrimary = colorScheme.onPrimary,
            onSecondary = colorScheme.onSecondary,
            onBackground = colorScheme.onBackground,
            onSurface = colorScheme.onSurface,
            onError = colorScheme.onError,
        )
    } else {
        lightColorScheme(
            primary = colorScheme.primary,
            secondary = colorScheme.secondary,
            background = colorScheme.background,
            surface = colorScheme.surface,
            error = colorScheme.error,
            onPrimary = colorScheme.onPrimary,
            onSecondary = colorScheme.onSecondary,
            onBackground = colorScheme.onBackground,
            onSurface = colorScheme.onSurface,
            onError = colorScheme.onError,
        )
    }

    val material3Shapes = Shapes(
        small = shapes.small,
        medium = shapes.medium,
        large = shapes.large,
    )

    MaterialTheme(
        colorScheme = material3ColorScheme,
        shapes = material3Shapes,
        typography = Typography(
            displayLarge = typography.displayLarge,
            displayMedium = typography.displayMedium,
            displaySmall = typography.displaySmall,
            headlineLarge = typography.headlineLarge,
            headlineMedium = typography.headlineMedium,
            headlineSmall = typography.headlineSmall,
            titleLarge = typography.titleLarge,
            titleMedium = typography.titleMedium,
            titleSmall = typography.titleSmall,
            bodyLarge = typography.bodyLarge,
            bodyMedium = typography.bodyMedium,
            bodySmall = typography.bodySmall,
            labelLarge = typography.labelLarge,
            labelMedium = typography.labelMedium,
            labelSmall = typography.labelSmall,
        ),
    ) {
        CompositionLocalProvider(
            LocalMiruColors provides colorScheme,
            LocalMiruTypography provides typography,
            LocalMiruShapes provides shapes,
            content = content,
        )
    }
}

/**
 * Object providing access to current CMP theme values.
 */
object MiruTheme {
    val colors: MiruColorScheme
        @Composable
        get() = LocalMiruColors.current

    val typography: MiruTypography
        @Composable
        get() = LocalMiruTypography.current

    val shapes: MiruShapes
        @Composable
        get() = LocalMiruShapes.current
}
