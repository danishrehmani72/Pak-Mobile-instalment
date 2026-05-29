package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = NeonMint,
    secondary = GoldAccent,
    tertiary = LightGold,
    background = EmeraldDarkBg,
    surface = EmeraldCardBg,
    onPrimary = Color(0xFF071F16),
    onSecondary = Color(0xFF071F16),
    onBackground = Color(0xFFE8F5E9),
    onSurface = Color(0xFFE8F5E9),
    surfaceVariant = Color(0xFF11382A),
    onSurfaceVariant = Color(0xFFC8E6C9)
)

private val LightColorScheme = lightColorScheme(
    primary = GreenPrimary,
    secondary = GreenSecondary,
    tertiary = GoldAccent,
    background = MintBg,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color(0xFF2A2A2A),
    onBackground = Color(0xFF111E16),
    onSurface = Color(0xFF111E16),
    surfaceVariant = Color(0xFFE8F5E9),
    onSurfaceVariant = Color(0xFF1B5E20)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Keep dynamicColor false by default to showcase our gorgeous Pakistan-themed colors!
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
