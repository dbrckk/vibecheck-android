package com.vibecheck.app

import android.os.Bundle
import androidx.test.runner.AndroidJUnitRunner
import java.util.Locale

class VibeCheckTestRunner : AndroidJUnitRunner() {
    override fun onCreate(arguments: Bundle?) {
        Locale.setDefault(Locale.FRANCE)
        super.onCreate(arguments)
    }
}
