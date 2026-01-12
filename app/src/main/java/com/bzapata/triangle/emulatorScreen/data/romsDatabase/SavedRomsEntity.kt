package com.bzapata.triangle.emulatorScreen.data.romsDatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bzapata.triangle.emulatorScreen.domain.Consoles

@Entity(tableName = "SavedRomsDb")
data class SavedRomsEntity(
    @PrimaryKey val deviceHash: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "coverDownloadUri") val coverDownloadUri: List<String?>,
    @ColumnInfo(name = "localCoverUri") val localCoverUri: String?,
    @ColumnInfo(name = "path") val path: String,
    @ColumnInfo(name = "romId") val romId: Int,
    @ColumnInfo(name = "console") val console: Consoles,
    @ColumnInfo(name = "lastModified") val lastModified: Long,
    @ColumnInfo(name = "coverTimeStamp") val coverTimeStamp : Long?,
)