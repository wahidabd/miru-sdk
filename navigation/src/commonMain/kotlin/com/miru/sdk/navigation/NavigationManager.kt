package com.miru.sdk.navigation

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

/**
 * Sealed class representing navigation commands that can be dispatched through the navigation system.
 */
sealed class NavigationCommand {
    /**
     * Navigate to a destination with optional pop-up behavior.
     *
     * @param route The destination route to navigate to
     * @param popUpTo Optional route to pop up to
     * @param inclusive Whether to include the popUpTo route in the pop operation
     * @param singleTop If true, prevents duplicate destinations on the back stack
     */
    data class NavigateTo(
        val route: String,
        val popUpTo: String? = null,
        val inclusive: Boolean = false,
        val singleTop: Boolean = false,
    ) : NavigationCommand()

    /**
     * Navigate back in the navigation stack.
     */
    data object NavigateBack : NavigationCommand()

    /**
     * Pop up to a specific route and optionally remove it from the stack.
     *
     * @param route The route to pop up to
     * @param inclusive Whether to include the route in the pop operation
     */
    data class PopUpTo(
        val route: String,
        val inclusive: Boolean = false,
    ) : NavigationCommand()

    /**
     * Navigate to a destination and clear the entire navigation stack.
     *
     * @param route The destination route to navigate to
     */
    data class NavigateAndClearStack(val route: String) : NavigationCommand()
}

/**
 * Interface for managing navigation within the application.
 * Provides methods to navigate between screens and manage the navigation stack.
 */
interface NavigationManager {
    /**
     * Get a flow of navigation commands.
     * Collectors of this flow will receive navigation events.
     */
    val navigationCommands: Flow<NavigationCommand>

    /**
     * Navigate to a destination.
     *
     * @param route The destination route to navigate to
     * @param popUpTo Optional route to pop up to
     * @param inclusive Whether to include the popUpTo route in the pop operation
     * @param singleTop If true, prevents duplicate destinations on the back stack
     */
    suspend fun navigate(
        route: String,
        popUpTo: String? = null,
        inclusive: Boolean = false,
        singleTop: Boolean = false,
    )

    /**
     * Navigate back in the navigation stack.
     */
    suspend fun goBack()

    /**
     * Pop up to a specific route.
     *
     * @param route The route to pop up to
     * @param inclusive Whether to include the route in the pop operation
     */
    suspend fun popUpTo(
        route: String,
        inclusive: Boolean = false,
    )

    /**
     * Navigate to a destination and clear the entire navigation stack.
     *
     * @param route The destination route to navigate to
     */
    suspend fun clearAndNavigate(route: String)
}

/**
 * Default implementation of [NavigationManager].
 * Uses a Channel to manage navigation commands as a stream of events.
 */
class NavigationManagerImpl : NavigationManager {
    private val navigationChannel = Channel<NavigationCommand>(Channel.BUFFERED)

    override val navigationCommands: Flow<NavigationCommand> = navigationChannel.receiveAsFlow()

    override suspend fun navigate(
        route: String,
        popUpTo: String?,
        inclusive: Boolean,
        singleTop: Boolean,
    ) {
        navigationChannel.send(
            NavigationCommand.NavigateTo(
                route = route,
                popUpTo = popUpTo,
                inclusive = inclusive,
                singleTop = singleTop,
            )
        )
    }

    override suspend fun goBack() {
        navigationChannel.send(NavigationCommand.NavigateBack)
    }

    override suspend fun popUpTo(
        route: String,
        inclusive: Boolean,
    ) {
        navigationChannel.send(
            NavigationCommand.PopUpTo(
                route = route,
                inclusive = inclusive,
            )
        )
    }

    override suspend fun clearAndNavigate(route: String) {
        navigationChannel.send(NavigationCommand.NavigateAndClearStack(route))
    }
}
