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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object VibeColors {
    val Ink = Color(0xFF2F2937)
    val InkRaised = Color(0xFF443B4D)
    val Surface = Color(0xFFFFFBF6)
    val SurfaceStrong = Color(0xFFF3EAF7)
    val Purple = Color(0xFFCBB7EA)
    val PurpleBright = Color(0xFFE9DDFF)
    val Orange = Color(0xFFFFC8AD)
    val Green = Color(0xFFBFE5D0)
    val Blue = Color(0xFFC6DDF4)
    val Rose = Color(0xFFECAFC3)
    val TextPrimary = Color(0xFF302938)
    val TextSecondary = Color(0xFF756B7D)
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
    val light: Boolean = false,
)

private fun paletteFor(style: VibeThemeStyle): VibePalette = when (style) {
    VibeThemeStyle.PASTEL_DRAWN -> VibePalette(
        background = Color(0xFFFFF8EE), backgroundMid = Color(0xFFF9EEF8),
        surface = Color(0xFFFFFCF8), surfaceStrong = Color(0xFFF1E6F4),
        primary = Color(0xFF8D72B5), primaryContainer = Color(0xFFE7D9F5),
        secondary = Color(0xFFD88FA8), tertiary = Color(0xFF72A88A),
        topGlow = Color(0x66E7D6F6), bottomGlow = Color(0x66FFD1BC), light = true,
    )
    VibeThemeStyle.PREMIUM_DARK -> VibePalette(Color(0xFF0B0A0F), Color(0xFF171020), Color(0xFF201B28), Color(0xFF2A2333), Color(0xFFC9A7FF), Color(0xFF4B2E6B), Color(0xFF95C8FF), Color(0xFF9BE6C1), Color(0x4D8D4DFF), Color(0x2648B5FF))
    VibeThemeStyle.KAWAII -> VibePalette(Color(0xFF160F1C), Color(0xFF2A1727), Color(0xFF2D2033), Color(0xFF3A2942), Color(0xFFFFB8DF), Color(0xFF643452), Color(0xFFB9D9FF), Color(0xFFFFD39F), Color(0x55FF7DC9), Color(0x33A6C8FF))
    VibeThemeStyle.POP -> VibePalette(Color(0xFF101018), Color(0xFF231A10), Color(0xFF24232F), Color(0xFF31303E), Color(0xFFFFD44D), Color(0xFF635018), Color(0xFF70D7FF), Color(0xFFFF7A90), Color(0x55FFD22E), Color(0x3354D6FF))
    VibeThemeStyle.ELEGANT -> VibePalette(Color(0xFF0D0D0D), Color(0xFF171512), Color(0xFF211F1C), Color(0xFF302D27), Color(0xFFD8C29D), Color(0xFF594C39), Color(0xFFB8B0A5), Color(0xFFE4D3B3), Color(0x44D7B779), Color(0x2AB9A88C))
    VibeThemeStyle.STREET -> VibePalette(Color(0xFF090D0B), Color(0xFF11231A), Color(0xFF17251F), Color(0xFF21372C), Color(0xFFB8FF3D), Color(0xFF395B18), Color(0xFF7FE7D2), Color(0xFFFF9A3D), Color(0x4AB8FF3D), Color(0x337FE7D2))
    VibeThemeStyle.MINIMAL -> VibePalette(Color(0xFF0F1115), Color(0xFF171A20), Color(0xFF20242B), Color(0xFF2A3038), Color(0xFFE7EAF0), Color(0xFF444A54), Color(0xFFAEB7C5), Color(0xFFC8D0DC), Color(0x2FE7EAF0), Color(0x2299A6B8))
}

private fun schemeFor(style: VibeThemeStyle) = paletteFor(style).let { p ->
    if (p.light) lightColorScheme(
        primary = p.primary, onPrimary = Color.White, primaryContainer = p.primaryContainer,
        onPrimaryContainer = VibeColors.TextPrimary, secondary = p.secondary, tertiary = p.tertiary,
        background = p.background, onBackground = VibeColors.TextPrimary, surface = p.surface,
        onSurface = VibeColors.TextPrimary, surfaceVariant = p.surfaceStrong,
        onSurfaceVariant = VibeColors.TextSecondary, error = Color(0xFFB85C70),
    ) else darkColorScheme(
        primary = p.primary, onPrimary = Color(0xFF141218), primaryContainer = p.primaryContainer,
        onPrimaryContainer = Color(0xFFF8F5FC), secondary = p.secondary, tertiary = p.tertiary,
        background = p.background, onBackground = Color(0xFFF8F5FC), surface = p.surface,
        onSurface = Color(0xFFF8F5FC), surfaceVariant = p.surfaceStrong,
        onSurfaceVariant = Color(0xFFC8C2CF), error = Color(0xFFFFA3B1),
    )
}

private val VibeTypography = Typography(
    headlineLarge = TextStyle(fontSize = 32.sp, lineHeight = 36.sp, fontWeight = FontWeight.Black, letterSpacing = (-0.4).sp),
    headlineMedium = TextStyle(fontSize = 26.sp, lineHeight = 31.sp, fontWeight = FontWeight.ExtraBold),
    titleLarge = TextStyle(fontSize = 20.sp, lineHeight = 25.sp, fontWeight = FontWeight.Bold),
    titleMedium = TextStyle(fontSize = 17.sp, lineHeight = 22.sp, fontWeight = FontWeight.Bold),
    bodyLarge = TextStyle(fontSize = 16.sp, lineHeight = 23.sp, fontWeight = FontWeight.Medium),
    bodyMedium = TextStyle(fontSize = 14.sp, lineHeight = 20.sp),
    labelLarge = TextStyle(fontSize = 14.sp, lineHeight = 18.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.2.sp),
)

private val VibeShapes = Shapes(
    small = RoundedCornerShape(18.dp), medium = RoundedCornerShape(26.dp), large = RoundedCornerShape(36.dp)
)

@Composable
fun VibeCheckTheme(style: VibeThemeStyle = VibeThemeStyle.PASTEL_DRAWN, content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = schemeFor(style), typography = VibeTypography, shapes = VibeShapes, content = content)
}

@Composable
fun VibeBackdrop(style: VibeThemeStyle = VibeThemeStyle.PASTEL_DRAWN, content: @Composable () -> Unit) {
    val p = paletteFor(style)
    Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(p.background, p.backgroundMid, p.background)))) {
        Box(Modifier.align(Alignment.TopEnd).offset(x = 110.dp, y = (-120).dp).size(360.dp).background(Brush.radialGradient(listOf(p.topGlow, Color.Transparent)), CircleShape))
        Box(Modifier.align(Alignment.BottomStart).offset(x = (-130).dp, y = 120.dp).size(330.dp).background(Brush.radialGradient(listOf(p.bottomGlow, Color.Transparent)), CircleShape))
        content()
    }
}
