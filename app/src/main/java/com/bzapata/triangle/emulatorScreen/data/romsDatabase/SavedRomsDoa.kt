package com.bzapata.triangle.emulatorScreen.data.romsDatabase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedRomsDoa {

    @Upsert
    suspend fun upsert(game: SavedRomsEntity)

    @Query("SELECT * FROM SavedRomsDb")
    fun getAllSavedRoms(): Flow<List<SavedRomsEntity>>?

    @Query("SElECT * FROM SavedRomsDb WHERE deviceHash = :hash ")
    suspend fun getFromHash(hash: String): SavedRomsEntity?

    @Delete
    suspend fun deleteRomsFromDb(game: SavedRomsEntity)

    @Query("DELETE FROM SavedRomsDb")
    fun deleteAll()

    @Query("UPDATE SavedRomsDb SET localCoverUri = :uri WHERE deviceHash = :hash")
    suspend fun updateCoverUri(hash: String, uri: String)

    @Query("SELECT * FROM SavedRomsDb WHERE deviceHash = :hash")
    suspend fun queryForDevice(hash : String) : SavedRomsEntity?

    @Query("UPDATE SavedRomsDb SET name = :newName WHERE deviceHash = :hash")
    suspend fun renameRom(hash : String, newName : String)

}
