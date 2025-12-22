package com.bzapata.triangle.emulatorScreen.data.GameDataBase

import androidx.room.Dao
import androidx.room.Query

@Dao
interface GamesDbDoa {

    @Query("SELECT romID FROM ROMs WHERE romHashSHA1 = :hash")
    suspend fun getRomId(hash: String): Int?

    @Query("SELECT romID FROM ROMs WHERE romExtensionlessFileName = :fileName")
    suspend fun getRomIdFromName(fileName: String?): Int?

    @Query("SELECT releaseTitleName FROM RELEASES WHERE romID = :romID")
    suspend fun getName(romID: Int): String?

    @Query("SELECT releaseCoverFront FROM RELEASES WHERE romID = :romID")
    suspend fun getCoverURI(romID: Int): List<String>?

    @Query("SELECT releaseCoverFront FROM RELEASES WHERE romID = :romID AND regionLocalizedID =21")
    suspend fun getUSACoverURI(romID: Int): String? // sometimes the us version has a higher quality image. function above is for a fallback

    @Query(
        """
        SELECT DISTINCT releaseCoverFront, releaseTitleName
        FROM RELEASES 
        WHERE releaseTitleName LIKE '%' || :titleName || '%'
        AND releaseCoverFront IS NOT NULL
    """
    )
    suspend fun queryCover(titleName: String): List<CoveryQueryResult>
}
