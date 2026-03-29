package com.miru.sdk.navigation.transition

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.navigation.NavBackStackEntry

/**
 * Predefined transition specifications for common navigation patterns.
 * These can be used with NavHost to provide consistent animations across the app.
 */
object NavigationTransition {
    /**
     * Slide transition that moves content from right to left on enter, and left to right on exit.
     * Suitable for forward navigation in a stack.
     */
    fun slideFromRight(): ContentTransform {
        return slideInHorizontally { it } togetherWith slideOutHorizontally { -it }
    }

    /**
     * Slide transition that moves content from left to right on enter, and right to left on exit.
     * Suitable for backward navigation in a stack.
     */
    fun slideFromLeft(): ContentTransform {
        return slideInHorizontally { -it } togetherWith slideOutHorizontally { it }
    }

    /**
     * Slide transition that moves content from bottom to top on enter, and top to bottom on exit.
     * Suitable for modal or dialog-like navigation.
     */
    fun slideFromBottom(): ContentTransform {
        return slideInVertically { it } togetherWith slideOutVertically { it }
    }

    /**
     * Fade transition that fades in new content and fades out old content.
     * Suitable for cross-fade effects between screens.
     */
    fun fadeInOut(): ContentTransform {
        return fadeIn() togetherWith fadeOut()
    }

    /**
     * Scale transition that scales up new content while fading it in,
     * and scales down old content while fading it out.
     * Suitable for emphasis transitions.
     */
    fun scaleInOut(): ContentTransform {
        return scaleIn() togetherWith scaleOut()
    }

    /**
     * Combined transition: slide from right with fade.
     * Provides a smooth, modern navigation experience.
     */
    fun slideFromRightWithFade(): ContentTransform {
        return (slideInHorizontally { it } + fadeIn()) togetherWith (slideOutHorizontally { -it } + fadeOut())
    }

    /**
     * Combined transition: scale with fade for emphasis.
     * Good for modal-like navigation with visual emphasis.
     */
    fun scaleWithFade(): ContentTransform {
        return (scaleIn() + fadeIn()) togetherWith (scaleOut() + fadeOut())
    }
}

/**
 * Extension function for AnimatedContentTransitionScope to apply common enter transitions.
 *
 * @return An enter transition that slides from the right with a fade effect
 */
fun AnimatedContentTransitionScope<NavBackStackEntry>.slideFromRightEnter(): EnterTransition {
    return slideInHorizontally { it } + fadeIn()
}

/**
 * Extension function for AnimatedContentTransitionScope to apply common exit transitions.
 *
 * @return An exit transition that slides to the left with a fade effect
 */
fun AnimatedContentTransitionScope<NavBackStackEntry>.slideToLeftExit(): ExitTransition {
    return slideOutHorizontally { -it } + fadeOut()
}

/**
 * Extension function for AnimatedContentTransitionScope to apply pop-back enter transitions.
 *
 * @return An enter transition that slides from the left with a fade effect
 */
fun AnimatedContentTransitionScope<NavBackStackEntry>.slideFromLeftEnter(): EnterTransition {
    return slideInHorizontally { -it } + fadeIn()
}

/**
 * Extension function for AnimatedContentTransitionScope to apply pop-back exit transitions.
 *
 * @return An exit transition that slides to the right with a fade effect
 */
fun AnimatedContentTransitionScope<NavBackStackEntry>.slideToRightExit(): ExitTransition {
    return slideOutHorizontally { it } + fadeOut()
}

/**
 * Extension function for AnimatedContentTransitionScope to apply modal enter transitions.
 *
 * @return An enter transition that slides from the bottom with a fade effect
 */
fun AnimatedContentTransitionScope<NavBackStackEntry>.slideFromBottomEnter(): EnterTransition {
    return slideInVertically { it } + fadeIn()
}

/**
 * Extension function for AnimatedContentTransitionScope to apply modal exit transitions.
 *
 * @return An exit transition that slides to the bottom with a fade effect
 */
fun AnimatedContentTransitionScope<NavBackStackEntry>.slideToBottomExit(): ExitTransition {
    return slideOutVertically { it } + fadeOut()
}

/**
 * Extension function for AnimatedContentTransitionScope to apply scale transitions.
 *
 * @return A content transform that scales in and out with fade
 */
fun AnimatedContentTransitionScope<NavBackStackEntry>.scaleWithFadeTransition(): ContentTransform {
    return (scaleIn() + fadeIn()) togetherWith (scaleOut() + fadeOut())
}

/**
 * Extension function for AnimatedContentTransitionScope to apply fade-only transitions.
 *
 * @return A content transform that fades in and out
 */
fun AnimatedContentTransitionScope<NavBackStackEntry>.fadeOnlyTransition(): ContentTransform {
    return fadeIn() togetherWith fadeOut()
}
