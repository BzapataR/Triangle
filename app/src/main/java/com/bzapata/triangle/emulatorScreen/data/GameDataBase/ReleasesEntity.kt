package com.bzapata.triangle.emulatorScreen.data.GameDataBase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "RELEASES")
data class ReleaseEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "releaseID")
    val releaseId: Int,

    // INTEGER
    @ColumnInfo(name = "romID")
    val romId: Int?,

    // TEXT
    @ColumnInfo(name = "releaseTitleName")
    val releaseTitleName: String?,

    // INTEGER
    @ColumnInfo(name = "regionLocalizedID")
    val regionLocalizedID: Int?,

    // TEXT
    @ColumnInfo(name = "TEMPregionLocalizedName")
    val TEMPregionLocalizedName: String?,

    // TEXT
    @ColumnInfo(name = "TEMPsystemShortName")
    val TEMPsystemShortName: String?,

    // TEXT
    @ColumnInfo(name = "TEMPsystemName")
    val TEMPsystemName: String?,

    // TEXT
    @ColumnInfo(name = "releaseCoverFront")
    val releaseCoverFront: String?,

    // TEXT
    @ColumnInfo(name = "releaseCoverBack")
    val releaseCoverBack: String?,

    // TEXT
    @ColumnInfo(name = "releaseCoverCart")
    val releaseCoverCart: String?,

    // TEXT
    @ColumnInfo(name = "releaseCoverDisc")
    val releaseCoverDisc: String?,

    // TEXT
    @ColumnInfo(name = "releaseDescription")
    val releaseDescription: String?,

    // TEXT
    @ColumnInfo(name = "releasePublisher")
    val releasePublisher: String?,

    // TEXT
    @ColumnInfo(name = "releaseDeveloper")
    val releaseDeveloper: String?,

    // TEXT
    @ColumnInfo(name = "releaseGenre")
    val releaseGenre: String?,

    // TEXT
    @ColumnInfo(name = "releaseDate")
    val releaseDate: String?,

    // TEXT
    @ColumnInfo(name = "releaseReferenceURL")
    val releaseReferenceURL: String?,

    // TEXT
    @ColumnInfo(name = "releaseReferenceImageURL")
    val releaseReferenceImageURL: String?
)