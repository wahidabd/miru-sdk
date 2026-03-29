package com.miru.sdk.ui.components.spacer

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Vertical spacer composable.
 *
 * @param height The height of the spacer
 */
@Composable
fun VerticalSpacer(height: Dp) {
    Spacer(modifier = Modifier.height(height))
}

/**
 * Horizontal spacer composable.
 *
 * @param width The width of the spacer
 */
@Composable
fun HorizontalSpacer(width: Dp) {
    Spacer(modifier = Modifier.width(width))
}

/**
 * Tiny vertical spacer (4dp).
 */
@Composable
fun SpacerTiny() {
    VerticalSpacer(height = 4.dp)
}

/**
 * Small vertical spacer (8dp).
 */
@Composable
fun SpacerSmall() {
    VerticalSpacer(height = 8.dp)
}

/**
 * Medium vertical spacer (16dp).
 */
@Composable
fun SpacerMedium() {
    VerticalSpacer(height = 16.dp)
}

/**
 * Large vertical spacer (24dp).
 */
@Composable
fun SpacerLarge() {
    VerticalSpacer(height = 24.dp)
}

/**
 * Extra large vertical spacer (32dp).
 */
@Composable
fun SpacerExtraLarge() {
    VerticalSpacer(height = 32.dp)
}
