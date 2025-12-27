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
    suspend fun renameRom(newName: String, gameHash: String)
    suspend fun queryCovers(query :String) : Map<Uri, String?>
    suspend fun querySavedRoms(query : String): List<Game>
}
