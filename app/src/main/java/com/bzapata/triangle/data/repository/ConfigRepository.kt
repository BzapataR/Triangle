package com.bzapata.triangle.data.repository

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "config")

class ConfigRepository(private val context: Context) {

    private object PreferenceKeys {
        val TRIANGLE_DATA_URI = stringPreferencesKey("triangle_data_uri")
        val ROMS_URIS = stringPreferencesKey("roms_uris")

        val FIRST_LAUNCH = booleanPreferencesKey("first_launch")
    }

    val triangleDataUriFlow: Flow<Uri?> = context.dataStore.data //todo maybe remove null typing
        .map { preferences ->
            val uriString = preferences[PreferenceKeys.TRIANGLE_DATA_URI]
            uriString?.toUri()
        }

    suspend fun saveTriangleDataUri(uri: Uri?) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.TRIANGLE_DATA_URI] = uri.toString()
        }
    }

    val romUrisFlow: Flow<List<Uri>> = context.dataStore.data
        .map { preferences ->
            val rawString = preferences[PreferenceKeys.ROMS_URIS]
            if (rawString.isNullOrEmpty()) {
                emptyList()
            }
            else {
                rawString.split("\n").map { it.toUri() }
            }
        }

    suspend fun saveRomsUri(uri: Uri?) {
        context.dataStore.edit { preferences ->
            val rawString = preferences[PreferenceKeys.ROMS_URIS] ?: ""
            val currentList = if(rawString.isEmpty()) emptyList() else rawString.split("\n")

            if(!currentList.contains(uri.toString())) {
                val newList = currentList + uri.toString()
                preferences[PreferenceKeys.ROMS_URIS] = newList.joinToString("\n")
            }
        }
    }
    suspend fun removeRomPath(uri: Uri) {
        context.dataStore.edit { preferences ->
            val rawString = preferences[PreferenceKeys.ROMS_URIS] ?: ""
            val currentList = if (rawString.isEmpty()) emptyList() else rawString.split("\n")
            val newList = currentList.filter { it != uri.toString() }
            preferences[PreferenceKeys.ROMS_URIS] = newList.joinToString("\n")
        }
    }

    val isFirstLaunchFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[PreferenceKeys.FIRST_LAUNCH] ?: true
        }

    suspend fun changeFirstLaunch() {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.FIRST_LAUNCH] = false
        }
    }
}