package com.bzapata.triangle.data.repository

import android.content.Context
import android.net.Uri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.core.net.toUri

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "config")

class ConfigRepository(private val context: Context) {

    private object PreferenceKeys{
        val TRIANGLE_DATA_URI = stringPreferencesKey("triangle_data_uri")
        val ROMS_URI = stringPreferencesKey("roms_uri")

        val FIRST_LAUNCH = booleanPreferencesKey("first_launch")
    }

    val triangleDataUriFlow : Flow<Uri?> = context.dataStore.data
        .map{ preferences ->
            val uriString = preferences[PreferenceKeys.TRIANGLE_DATA_URI]
            uriString?.toUri()
        }
    suspend fun saveTriangleDataUri(uri : Uri?) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.TRIANGLE_DATA_URI] = uri.toString()
        }
    }

    val romUriFlow : Flow<Uri?> = context.dataStore.data
        .map{ preferences ->
            val uriString = preferences[PreferenceKeys.ROMS_URI]
            uriString?.toUri()
        }
    suspend fun saveRomsUri(uri : Uri?) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.ROMS_URI] = uri.toString()
        }
    }

    val isFirstLaunchFlow : Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[PreferenceKeys.FIRST_LAUNCH] ?: true
        }

    suspend fun changeFirstLaunch() {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.FIRST_LAUNCH] = false
        }
    }
}