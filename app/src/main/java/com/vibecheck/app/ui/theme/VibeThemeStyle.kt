package com.vibecheck.app.ui.theme

enum class VibeThemeStyle(
    val id: String,
    val label: String,
) {
    PASTEL_DRAWN("pastel_drawn", "Pastel dessiné"),
    PREMIUM_DARK("premium_dark", "Premium Dark"),
    KAWAII("kawaii", "Kawaii"),
    POP("pop", "Pop"),
    ELEGANT("elegant", "Elegant"),
    STREET("street", "Street"),
    MINIMAL("minimal", "Minimal");

    companion object {
        fun fromStoredId(raw: String?): VibeThemeStyle =
            entries.firstOrNull { it.id == raw } ?: PASTEL_DRAWN
    }
}
