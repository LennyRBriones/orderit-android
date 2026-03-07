package com.orderit.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val OrderItColorScheme = lightColorScheme(
    primary = SidebarDark,
    onPrimary = Color.White,
    secondary = StatusNew,
    onSecondary = Color.White,
    background = BackgroundGray,
    onBackground = TextPrimary,
    surface = SurfaceWhite,
    onSurface = TextPrimary,
    surfaceVariant = CardSurface,
    onSurfaceVariant = TextSecondary,
    error = ErrorRed,
    onError = Color.White,
    outline = DividerColor
)

@Composable
fun OrderItTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = OrderItColorScheme,
        typography = Typography,
        content = content
    )
}
