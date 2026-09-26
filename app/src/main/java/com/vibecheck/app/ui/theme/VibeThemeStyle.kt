package com.vibecheck.app.ui.theme

import com.vibecheck.app.localization.AppLocale

enum class VibeThemeStyle(
    val id: String,
    private val labelFr: String,
    private val labelEn: String,
) {
    PASTEL_DRAWN("pastel_drawn", "Pastel dessiné", "Drawn pastel"),
    PREMIUM_DARK("premium_dark", "Premium Dark", "Premium Dark"),
    KAWAII("kawaii", "Kawaii", "Kawaii"),
    POP("pop", "Pop", "Pop"),
    ELEGANT("elegant", "Élégant", "Elegant"),
    STREET("street", "Street", "Street"),
    MINIMAL("minimal", "Minimal", "Minimal");

    val label: String get() = AppLocale.pick(labelFr, labelEn)

    companion object {
        fun fromStoredId(raw: String?): VibeThemeStyle =
            entries.firstOrNull { it.id == raw } ?: PASTEL_DRAWN
    }
}
