package com.bzapata.triangle.emulatorScreen.domain

import kotlinx.coroutines.flow.Flow

interface GameRepository {
    fun readAllGames(): Flow<List<Game>>
}