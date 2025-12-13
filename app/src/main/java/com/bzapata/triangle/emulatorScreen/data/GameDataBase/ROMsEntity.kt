package com.bzapata.triangle.emulatorScreen.data.GameDataBase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ROMs")
data class ROMsEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "romID")
    val romID :Int? = null,

    @ColumnInfo(name = "systemID")
    val systemID : Int? = null,

    @ColumnInfo(name = "regionID")
    val regionID : Int? = null,

    @ColumnInfo(name = "romHashCRC")
    val romHashCRC : String? = null,

    @ColumnInfo(name = "romHashMD5")
    val romHashMD5 : String? = null,

    @ColumnInfo(name = "romHashSHA1")
    val romHashSHA1 : String? = null,

    @ColumnInfo(name = "romSize")
    val romSize : Int? = null,

    @ColumnInfo(name = "romFileName")
    val romFileName : String? = null,

    @ColumnInfo("romExtensionlessFileName")
    val romExtensionlessFileName : String? = null,

    @ColumnInfo(name = "romParent")
    val romParent : String? = null,

    @ColumnInfo(name = "romSerial")
    val romSerial : String? = null,

    @ColumnInfo(name = "romHeader")
    val romHeader : String? = null,

    @ColumnInfo(name = "romLanguage")
    val romLanguage : String? = null,

    @ColumnInfo(name = "TEMPromRegion")
    val TEMPromRegion : String? = null,

    @ColumnInfo(name = "romDumpSource")
    val romDumpSource : String? = null

    )
