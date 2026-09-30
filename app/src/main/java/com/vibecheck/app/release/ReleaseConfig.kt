package com.vibecheck.app.release

/**
 * Mirror of the V1.2.0 Android release contract used by JVM release checks.
 * The authoritative package metadata remains in app/build.gradle.kts and the manifest.
 */
object ReleaseConfig {
    const val applicationId = "com.dbrckk.vibecheck"
    const val versionCode = 4
    const val versionName = "1.2.0"
    const val minSdk = 24
    const val targetSdk = 36
    const val allowBackup = false
    const val usesCleartextTraffic = false

    /**
     * V1.2.0 ships without ads, so an "ad removal" purchase must not be offered or initialized.
     */
    const val adRemovalPurchaseEnabled = false
}
