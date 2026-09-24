package com.vibecheck.app.ui.theme

internal data class VibeThemeTokens(
    val background: Long,
    val surface: Long,
    val surfaceStrong: Long,
    val primary: Long,
    val onPrimary: Long,
    val secondary: Long,
    val tertiary: Long,
    val textPrimary: Long,
    val textSecondary: Long,
    val error: Long,
    val backdrop: List<Long>
)

internal fun themeTokens(style: VibeThemeStyle): VibeThemeTokens = when (style) {
    VibeThemeStyle.PREMIUM_DARK -> VibeThemeTokens(
        background = 0xFF0B0A0F,
        surface = 0xFF201B28,
        surfaceStrong = 0xFF2A2333,
        primary = 0xFFC9A7FF,
        onPrimary = 0xFF1A1124,
        secondary = 0xFF95C8FF,
        tertiary = 0xFF9BE6C1,
        textPrimary = 0xFFF8F5FC,
        textSecondary = 0xFFB6AEC2,
        error = 0xFFFFA3B1,
        backdrop = listOf(0xFF0B0A0F, 0xFF171020, 0xFF0B0A0F)
    )
    VibeThemeStyle.KAWAII -> VibeThemeTokens(
        background = 0xFFFFF5FA,
        surface = 0xFFFFFFFF,
        surfaceStrong = 0xFFFFE3F0,
        primary = 0xFFFF75AE,
        onPrimary = 0xFF3B1023,
        secondary = 0xFF9D8CFF,
        tertiary = 0xFF66CFC3,
        textPrimary = 0xFF2E1B27,
        textSecondary = 0xFF755D6B,
        error = 0xFFB3261E,
        backdrop = listOf(0xFFFFF5FA, 0xFFFFEAF4, 0xFFF3EEFF)
    )
    VibeThemeStyle.POP -> VibeThemeTokens(
        background = 0xFF101229,
        surface = 0xFF1B1F42,
        surfaceStrong = 0xFF292E5C,
        primary = 0xFFFFD84D,
        onPrimary = 0xFF241C00,
        secondary = 0xFFFF6B8A,
        tertiary = 0xFF5CE1E6,
        textPrimary = 0xFFFFFFFF,
        textSecondary = 0xFFC8CBE8,
        error = 0xFFFF8A80,
        backdrop = listOf(0xFF101229, 0xFF24164A, 0xFF0D2940)
    )
    VibeThemeStyle.ELEGANT -> VibeThemeTokens(
        background = 0xFF12100D,
        surface = 0xFF211E19,
        surfaceStrong = 0xFF302B24,
        primary = 0xFFE4C77A,
        onPrimary = 0xFF2A2108,
        secondary = 0xFFC8B99C,
        tertiary = 0xFF98B69B,
        textPrimary = 0xFFF8F1E5,
        textSecondary = 0xFFC4BAA9,
        error = 0xFFE7A09B,
        backdrop = listOf(0xFF12100D, 0xFF211A11, 0xFF0D1210)
    )
    VibeThemeStyle.STREET -> VibeThemeTokens(
        background = 0xFF0C1012,
        surface = 0xFF192025,
        surfaceStrong = 0xFF263138,
        primary = 0xFFB8FF3D,
        onPrimary = 0xFF142000,
        secondary = 0xFFFF7A2F,
        tertiary = 0xFF54D6FF,
        textPrimary = 0xFFF4F7F8,
        textSecondary = 0xFFADB9BF,
        error = 0xFFFF8B8B,
        backdrop = listOf(0xFF0C1012, 0xFF14251C, 0xFF111A22)
    )
    VibeThemeStyle.MINIMAL -> VibeThemeTokens(
        background = 0xFFF4F4F2,
        surface = 0xFFFFFFFF,
        surfaceStrong = 0xFFE6E7E4,
        primary = 0xFF202124,
        onPrimary = 0xFFFFFFFF,
        secondary = 0xFF5F6368,
        tertiary = 0xFF64766A,
        textPrimary = 0xFF202124,
        textSecondary = 0xFF666A6E,
        error = 0xFFB3261E,
        backdrop = listOf(0xFFF4F4F2, 0xFFECEDE9, 0xFFF7F7F5)
    )
}
