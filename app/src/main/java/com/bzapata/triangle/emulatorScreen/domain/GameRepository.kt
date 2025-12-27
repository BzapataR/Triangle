package com.bzapata.triangle.emulatorScreen.domain

import android.net.Uri
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    suspend fun scanRoms()
    fun getGames(): Flow<List<Game>>
    fun databaseBomb()
    suspend fun saveCover(uri: Uri, gameHash: String)
    suspend fun getCoverFromClipboard(gameHash: String)
    suspend fun addSingleGame(uri: Uri)
    suspend fun renameGame(newName: String, gameHash: String)
}
