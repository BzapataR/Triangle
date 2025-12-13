package com.bzapata.triangle.emulatorScreen.data.GameDataBase

import androidx.room.Dao
import androidx.room.Query
import java.net.URI

@Dao
interface GamesDbDoa {

    @Query("SELECT romID FROM ROMs WHERE romHashSHA1 = :hash")
    fun getRomId(hash : String) : Int?

    @Query("SELECT romID FROM ROMs WHERE romExtensionlessFileName = :fileName")
    fun getRomIdFromName(fileName : String?) : Int?

    @Query("SELECT releaseTitleName FROM RELEASES WHERE romID = :romID")
    fun getName(romID: Int) : String?

    @Query("SELECT releaseCoverFront FROM RELEASES WHERE romID = :romID")
    fun getCoverURI(romID: Int) : List<String>?

    @Query("SELECT releaseCoverFront FROM RELEASES WHERE romID = :romID AND regionLocalizedID =21")
    fun getUSACoverURI(romID: Int) : String? // sometimes the us version has a higher quality image. function above is for a fallback
}