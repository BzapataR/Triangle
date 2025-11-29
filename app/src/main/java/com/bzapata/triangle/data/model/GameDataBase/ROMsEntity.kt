package com.bzapata.triangle.data.model.GameDataBase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ROMs")
data class ROMsEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "romID")
    val romID :Int,

    @ColumnInfo(name = "systemID")
    val systemID : Int,

    @ColumnInfo(name = "regionID")
    val regionID : Int,

    @ColumnInfo(name = "romHashCRC")
    val romHashCRC : String?,

    @ColumnInfo(name = "romHashMD5")
    val romHashMD5 : String?,

    @ColumnInfo(name = "romHashSHA1")
    val romHashSHA1 : String?,

    @ColumnInfo(name = "romSize")
    val romSize : Int?,

    @ColumnInfo(name = "romFileName")
    val romFileName : String,

    @ColumnInfo("romExtensionlessFileName")
    val romExtensionlessFileName : String,

    @ColumnInfo(name = "romParent")
    val romParent : String?,

    @ColumnInfo(name = "romSerial")
    val romSerial : String?,

    @ColumnInfo(name = "romHeader")
    val romHeader : String?,

    @ColumnInfo(name = "romLanguage")
    val romLanguage : String?,

    @ColumnInfo(name = "TEMPromRegion")
    val TEMPromRegion : String?,

    @ColumnInfo(name = "romDumpSource")
    val romDumpSource : String,

)
