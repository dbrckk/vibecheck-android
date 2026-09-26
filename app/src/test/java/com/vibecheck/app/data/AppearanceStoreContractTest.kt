package com.vibecheck.app.data

import androidx.datastore.core.DataStore
import org.junit.Assert.assertTrue
import org.junit.Test

class AppearanceStoreContractTest {
    @Test
    fun `appearance store owns a datastore persistence backend`() {
        val hasDataStore = AppearanceStore::class.java.declaredFields.any { field ->
            DataStore::class.java.isAssignableFrom(field.type)
        }

        assertTrue("AppearanceStore must persist appearance with AndroidX DataStore", hasDataStore)
    }
}
