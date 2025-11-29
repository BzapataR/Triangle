package com.bzapata.triangle.data.model.GameDataBase

import androidx.room.Dao
import androidx.room.Query

@Dao
interface GamesDbDoa {
    @Query("SELECT romHashSHA1 FROM ROMs WHERE romExtensionlessFileName = :name")
    fun getSHA(name :String) : String

}