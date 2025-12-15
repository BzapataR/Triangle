package com.bzapata.triangle.emulatorScreen.data.romsDatabase

import android.net.Uri
import androidx.core.net.toUri
import com.bzapata.triangle.emulatorScreen.data.GameDataBase.ROMsEntity
import com.bzapata.triangle.emulatorScreen.domain.Game

fun SavedRomsEntity.toGame() : Game {
    return Game(
        name = this.name,
        romID = this.romId,
        path = this.path.toUri(),
        coverDownloaderUri = this.coverDownloadUri.map { it?.toUri() },
        localCoverUri = this.localCoverUri?.toUri(),
        consoles = this.console,
        hash = this.deviceHash
    )
}

fun Game.toSavedRomsEntity(lastModified : Long) : SavedRomsEntity {
    return SavedRomsEntity(
        deviceHash = this.hash,
        name = this.name,
        coverDownloadUri = this.coverDownloaderUri.map { it.toString() },
        localCoverUri = this.localCoverUri.toString(),
        path = this.path.toString(),
        romId = this.romID,
        console = this.consoles,
        lastModified = lastModified
    )
}

fun List<SavedRomsEntity>.toGame() : List<Game> {
    return this.map { it.toGame() }
}