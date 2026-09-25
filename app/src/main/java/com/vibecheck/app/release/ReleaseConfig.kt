package com.vibecheck.app.release

/**
 * Mirror of the V1 Android release contract used by JVM release checks.
 * The authoritative package metadata remains in app/build.gradle.kts and the manifest.
 */
object ReleaseConfig {
    const val versionCode = 1
    const val versionName = "1.0.0"
    const val minSdk = 24
    const val targetSdk = 36
    const val allowBackup = false
}
