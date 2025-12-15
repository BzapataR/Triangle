package com.bzapata.triangle.emulatorScreen.domain

import kotlinx.coroutines.flow.Flow

interface GameRepository {
    fun scanRoms(): Flow<List<Game>>
    suspend fun getGames() : Flow<List<Game>>
}