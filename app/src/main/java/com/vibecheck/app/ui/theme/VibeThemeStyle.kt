package com.vibecheck.app.ui.theme

enum class VibeThemeStyle(
    val id: String,
    val label: String
) {
    PREMIUM_DARK("premium_dark", "Premium Dark"),
    KAWAII("kawaii", "Kawaii"),
    POP("pop", "Pop"),
    ELEGANT("elegant", "Elegant"),
    STREET("street", "Street"),
    MINIMAL("minimal", "Minimal");

    companion object {
        fun fromId(id: String?): VibeThemeStyle =
            entries.firstOrNull { it.id == id } ?: PREMIUM_DARK
    }
}
