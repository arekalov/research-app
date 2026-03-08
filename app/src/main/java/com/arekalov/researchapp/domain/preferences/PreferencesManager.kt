package com.arekalov.researchapp.domain.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.arekalov.researchapp.domain.model.PaginationMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val PAGINATION_MODE_KEY = stringPreferencesKey("pagination_mode")
    }
    
    val paginationMode: Flow<PaginationMode> = dataStore.data.map { preferences ->
        val modeName = preferences[PAGINATION_MODE_KEY] ?: PaginationMode.SEAMLESS.name
        try {
            PaginationMode.valueOf(modeName)
        } catch (e: IllegalArgumentException) {
            PaginationMode.SEAMLESS
        }
    }
    
    suspend fun setPaginationMode(mode: PaginationMode) {
        dataStore.edit { preferences ->
            preferences[PAGINATION_MODE_KEY] = mode.name
        }
    }
}
