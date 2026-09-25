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
import androidx.compose.runtime.Composable
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

private data class VibePalette(
    val background: Color,
    val backgroundMid: Color,
    val surface: Color,
    val surfaceStrong: Color,
    val primary: Color,
    val primaryContainer: Color,
    val secondary: Color,
    val tertiary: Color,
    val topGlow: Color,
    val bottomGlow: Color,
)

private fun paletteFor(style: VibeThemeStyle): VibePalette = when (style) {
    VibeThemeStyle.PREMIUM_DARK -> VibePalette(
        background = Color(0xFF0B0A0F),
        backgroundMid = Color(0xFF171020),
        surface = Color(0xFF201B28),
        surfaceStrong = Color(0xFF2A2333),
        primary = Color(0xFFC9A7FF),
        primaryContainer = Color(0xFF4B2E6B),
        secondary = Color(0xFF95C8FF),
        tertiary = Color(0xFF9BE6C1),
        topGlow = Color(0x4D8D4DFF),
        bottomGlow = Color(0x2648B5FF),
    )
    VibeThemeStyle.KAWAII -> VibePalette(
        background = Color(0xFF160F1C),
        backgroundMid = Color(0xFF2A1727),
        surface = Color(0xFF2D2033),
        surfaceStrong = Color(0xFF3A2942),
        primary = Color(0xFFFFB8DF),
        primaryContainer = Color(0xFF643452),
        secondary = Color(0xFFB9D9FF),
        tertiary = Color(0xFFFFD39F),
        topGlow = Color(0x55FF7DC9),
        bottomGlow = Color(0x33A6C8FF),
    )
    VibeThemeStyle.POP -> VibePalette(
        background = Color(0xFF101018),
        backgroundMid = Color(0xFF231A10),
        surface = Color(0xFF24232F),
        surfaceStrong = Color(0xFF31303E),
        primary = Color(0xFFFFD44D),
        primaryContainer = Color(0xFF635018),
        secondary = Color(0xFF70D7FF),
        tertiary = Color(0xFFFF7A90),
        topGlow = Color(0x55FFD22E),
        bottomGlow = Color(0x3354D6FF),
    )
    VibeThemeStyle.ELEGANT -> VibePalette(
        background = Color(0xFF0D0D0D),
        backgroundMid = Color(0xFF171512),
        surface = Color(0xFF211F1C),
        surfaceStrong = Color(0xFF302D27),
        primary = Color(0xFFD8C29D),
        primaryContainer = Color(0xFF594C39),
        secondary = Color(0xFFB8B0A5),
        tertiary = Color(0xFFE4D3B3),
        topGlow = Color(0x44D7B779),
        bottomGlow = Color(0x2AB9A88C),
    )
    VibeThemeStyle.STREET -> VibePalette(
        background = Color(0xFF090D0B),
        backgroundMid = Color(0xFF11231A),
        surface = Color(0xFF17251F),
        surfaceStrong = Color(0xFF21372C),
        primary = Color(0xFFB8FF3D),
        primaryContainer = Color(0xFF395B18),
        secondary = Color(0xFF7FE7D2),
        tertiary = Color(0xFFFF9A3D),
        topGlow = Color(0x4AB8FF3D),
        bottomGlow = Color(0x337FE7D2),
    )
    VibeThemeStyle.MINIMAL -> VibePalette(
        background = Color(0xFF0F1115),
        backgroundMid = Color(0xFF171A20),
        surface = Color(0xFF20242B),
        surfaceStrong = Color(0xFF2A3038),
        primary = Color(0xFFE7EAF0),
        primaryContainer = Color(0xFF444A54),
        secondary = Color(0xFFAEB7C5),
        tertiary = Color(0xFFC8D0DC),
        topGlow = Color(0x2FE7EAF0),
        bottomGlow = Color(0x2299A6B8),
    )
}

private fun schemeFor(style: VibeThemeStyle) = paletteFor(style).let { palette ->
    darkColorScheme(
        primary = palette.primary,
        onPrimary = Color(0xFF141218),
        primaryContainer = palette.primaryContainer,
        onPrimaryContainer = Color(0xFFF8F5FC),
        secondary = palette.secondary,
        tertiary = palette.tertiary,
        background = palette.background,
        onBackground = VibeColors.TextPrimary,
        surface = palette.surface,
        onSurface = VibeColors.TextPrimary,
        surfaceVariant = palette.surfaceStrong,
        onSurfaceVariant = Color(0xFFC8C2CF),
        error = VibeColors.Rose,
    )
}

private val VibeTypography = Typography(
    headlineLarge = TextStyle(
        fontSize = 32.sp,
        lineHeight = 36.sp,
        fontWeight = FontWeight.Black,
        letterSpacing = (-0.4).sp
    ),
    headlineMedium = TextStyle(
        fontSize = 26.sp,
        lineHeight = 31.sp,
        fontWeight = FontWeight.ExtraBold
    ),
    titleLarge = TextStyle(
        fontSize = 20.sp,
        lineHeight = 25.sp,
        fontWeight = FontWeight.Bold
    ),
    titleMedium = TextStyle(
        fontSize = 17.sp,
        lineHeight = 22.sp,
        fontWeight = FontWeight.Bold
    ),
    bodyLarge = TextStyle(
        fontSize = 16.sp,
        lineHeight = 23.sp,
        fontWeight = FontWeight.Medium
    ),
    bodyMedium = TextStyle(
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    labelLarge = TextStyle(
        fontSize = 14.sp,
        lineHeight = 18.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.2.sp
    )
)

private val VibeShapes = Shapes(
    small = RoundedCornerShape(14.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(30.dp)
)

@Composable
fun VibeCheckTheme(
    style: VibeThemeStyle = VibeThemeStyle.PREMIUM_DARK,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = schemeFor(style),
        typography = VibeTypography,
        shapes = VibeShapes,
        content = content
    )
}

@Composable
fun VibeBackdrop(
    style: VibeThemeStyle = VibeThemeStyle.PREMIUM_DARK,
    content: @Composable () -> Unit,
) {
    val palette = paletteFor(style)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        palette.background,
                        palette.backgroundMid,
                        palette.background,
                    )
                )
            )
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 110.dp, y = (-120).dp)
                .size(360.dp)
                .background(
                    Brush.radialGradient(
                        listOf(palette.topGlow, Color.Transparent)
                    ),
                    CircleShape
                )
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = (-130).dp, y = 120.dp)
                .size(330.dp)
                .background(
                    Brush.radialGradient(
                        listOf(palette.bottomGlow, Color.Transparent)
                    ),
                    CircleShape
                )
        )
        content()
    }
}
