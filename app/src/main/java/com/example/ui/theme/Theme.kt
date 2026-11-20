package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme =
  darkColorScheme(
    primary = StoreBlueLight,
    onPrimary = StorePillInk,
    primaryContainer = StoreBlueDark,
    onPrimaryContainer = StoreNavPill,
    secondary = StoreNavPill,
    onSecondary = StorePillInk,
    secondaryContainer = StoreSurfaceContainerDark,
    onSecondaryContainer = StoreNavPill,
    background = StoreBgDark,
    onBackground = StoreInkDark,
    surface = StoreBgDark,
    onSurface = StoreInkDark,
    surfaceVariant = StoreSurfaceDark,
    onSurfaceVariant = StoreMutedDark,
    surfaceContainer = StoreSurfaceContainerDark,
    outline = StoreOutlineDark,
    error = StoreBadgeRed,
    onError = Color.White,
  )

private val LightColorScheme =
  lightColorScheme(
    primary = StoreBlue,
    onPrimary = Color.White,
    primaryContainer = StoreNavPill,
    onPrimaryContainer = StorePillInk,
    secondary = StoreBlue,
    onSecondary = Color.White,
    secondaryContainer = StoreNavPill,
    onSecondaryContainer = StorePillInk,
    background = StoreBgLight,
    onBackground = StoreInkLight,
    surface = StoreBgLight,
    onSurface = StoreInkLight,
    surfaceVariant = StoreSurfaceLight,
    onSurfaceVariant = StoreMutedLight,
    surfaceContainer = StoreSurfaceContainerLight,
    outline = StoreOutlineLight,
    error = StoreBadgeRed,
    onError = Color.White,
  )

@Composable
fun AsPlusStoreTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
