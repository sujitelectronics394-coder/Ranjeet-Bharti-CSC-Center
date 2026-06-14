package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = TrustIndigo80,
    secondary = AccentTeal80,
    tertiary = AmberGold80,
    background = DeepCharcoal,
    surface = DeepCharcoal,
    onPrimary = DeepCharcoal,
    onSecondary = DeepCharcoal,
    onTertiary = DeepCharcoal
  )

private val LightColorScheme =
  lightColorScheme(
    primary = HighDensityActionBlue,
    secondary = HighDensityHeaderBg,
    tertiary = HighDensityIconBg,
    background = HighDensityBg,
    surface = CleanWhite,
    onPrimary = CleanWhite,
    onSecondary = HighDensityTitleBlue,
    onTertiary = HighDensityText,
    onBackground = HighDensityText,
    onSurface = HighDensityText,
    surfaceVariant = HighDensityNavBg,
    onSurfaceVariant = HighDensityMutedGrey,
    outline = HighDensityBorderGrey,
    error = HighDensityAlertRed
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = false, // Force Light High Density Theme as requested
  // Disable dynamic color to maintain distinct branding color identity
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
