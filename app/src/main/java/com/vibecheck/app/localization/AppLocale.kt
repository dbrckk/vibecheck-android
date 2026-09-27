package com.vibecheck.app.localization

import java.util.Locale

/**
 * VibeCheck language contract:
 * - French when the device/app primary locale language is French.
 * - English for every other language.
 */
object AppLocale {
    fun isFrench(locale: Locale = Locale.getDefault()): Boolean =
        locale.language.equals("fr", ignoreCase = true)

    fun pick(fr: String, en: String, locale: Locale = Locale.getDefault()): String =
        if (isFrench(locale)) fr else en

    fun pickForLanguage(language: String?, fr: String, en: String): String =
        if (language.equals("fr", ignoreCase = true)) fr else en
}
