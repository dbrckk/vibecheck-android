package com.vibecheck.app.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object VibeColors {
    val Ink = Color(0xFF0B0A0F)
    val InkRaised = Color(0xFF15121B)
    val Surface = Color(0xFF201B28)
    val SurfaceStrong = Color(0xFF2A2333)
    val Purple = Color(0xFFC9A7FF)
    val PurpleBright = Color(0xFFE9DDFF)
    val Orange = Color(0xFFFFB88A)
    val Green = Color(0xFF9BE6C1)
    val Blue = Color(0xFF95C8FF)
    val Rose = Color(0xFFFFA3B1)
    val TextPrimary = Color(0xFFF8F5FC)
    val TextSecondary = Color(0xFFB6AEC2)
}

private val LocalVibeThemeStyle = staticCompositionLocalOf { VibeThemeStyle.PREMIUM_DARK }

val currentVibeThemeStyle: VibeThemeStyle
    @Composable get() = LocalVibeThemeStyle.current

private val VibeTypography = Typography(
    headlineLarge = TextStyle(fontSize = 32.sp, lineHeight = 36.sp, fontWeight = FontWeight.Black, letterSpacing = (-0.4).sp),
    headlineMedium = TextStyle(fontSize = 26.sp, lineHeight = 31.sp, fontWeight = FontWeight.ExtraBold),
    titleLarge = TextStyle(fontSize = 20.sp, lineHeight = 25.sp, fontWeight = FontWeight.Bold),
    titleMedium = TextStyle(fontSize = 17.sp, lineHeight = 22.sp, fontWeight = FontWeight.Bold),
    bodyLarge = TextStyle(fontSize = 16.sp, lineHeight = 23.sp, fontWeight = FontWeight.Medium),
    bodyMedium = TextStyle(fontSize = 14.sp, lineHeight = 20.sp),
    labelLarge = TextStyle(fontSize = 14.sp, lineHeight = 18.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.2.sp)
)

private val VibeShapes = Shapes(
    small = RoundedCornerShape(14.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(30.dp)
)

@Composable
fun VibeCheckTheme(
    style: VibeThemeStyle? = null,
    content: @Composable () -> Unit
) {
    val inheritedStyle = LocalVibeThemeStyle.current
    val resolvedStyle = style ?: inheritedStyle
    val token = themeTokens(resolvedStyle)
    val scheme = if (resolvedStyle == VibeThemeStyle.KAWAII || resolvedStyle == VibeThemeStyle.MINIMAL) {
        lightColorScheme(
            primary = Color(token.primary), onPrimary = Color(token.onPrimary),
            primaryContainer = Color(token.surfaceStrong), onPrimaryContainer = Color(token.textPrimary),
            secondary = Color(token.secondary), tertiary = Color(token.tertiary),
            background = Color(token.background), onBackground = Color(token.textPrimary),
            surface = Color(token.surface), onSurface = Color(token.textPrimary),
            surfaceVariant = Color(token.surfaceStrong), onSurfaceVariant = Color(token.textSecondary),
            error = Color(token.error)
        )
    } else {
        darkColorScheme(
            primary = Color(token.primary), onPrimary = Color(token.onPrimary),
            primaryContainer = Color(token.surfaceStrong), onPrimaryContainer = Color(token.textPrimary),
            secondary = Color(token.secondary), tertiary = Color(token.tertiary),
            background = Color(token.background), onBackground = Color(token.textPrimary),
            surface = Color(token.surface), onSurface = Color(token.textPrimary),
            surfaceVariant = Color(token.surfaceStrong), onSurfaceVariant = Color(token.textSecondary),
            error = Color(token.error)
        )
    }

    CompositionLocalProvider(LocalVibeThemeStyle provides resolvedStyle) {
        MaterialTheme(colorScheme = scheme, typography = VibeTypography, shapes = VibeShapes, content = content)
    }
}

@Composable
fun VibeBackdrop(content: @Composable () -> Unit) {
    val token = themeTokens(currentVibeThemeStyle)
    Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(token.backdrop.map(::Color)))) {
        Box(modifier = Modifier.align(Alignment.TopEnd).offset(x = 110.dp, y = (-120).dp).size(360.dp).background(Brush.radialGradient(listOf(Color(token.primary).copy(alpha = 0.22f), Color.Transparent)), CircleShape))
        Box(modifier = Modifier.align(Alignment.BottomStart).offset(x = (-130).dp, y = 120.dp).size(330.dp).background(Brush.radialGradient(listOf(Color(token.secondary).copy(alpha = 0.14f), Color.Transparent)), CircleShape))
        content()
    }
}
