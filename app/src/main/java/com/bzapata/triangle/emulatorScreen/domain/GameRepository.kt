package com.bzapata.triangle.emulatorScreen.domain

import kotlinx.coroutines.flow.Flow

interface GameRepository {
    suspend fun scanRoms()
    fun getGames() : Flow<List<Game>>
    fun databaseBomb()
}
