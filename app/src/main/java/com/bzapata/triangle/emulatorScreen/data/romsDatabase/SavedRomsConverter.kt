package com.bzapata.triangle.emulatorScreen.data.romsDatabase

import android.net.Uri
import androidx.core.net.toUri
import com.bzapata.triangle.emulatorScreen.domain.Game

fun SavedRomsEntity.toGame(): Game {
    return Game(
        name = this.name,
        romID = this.romId,
        path = this.path.toUri(),
        localCoverUri = this.localCoverUri?.toUri(),
        consoles = this.console,
        hash = this.deviceHash,
        coverTimeStamp = this.coverTimeStamp

    )
}

fun Game.toSavedRomsEntity(lastModified: Long, coverUrl : List<Uri>): SavedRomsEntity {
    return SavedRomsEntity(
        deviceHash = this.hash,
        name = this.name,
        coverDownloadUri = coverUrl.map { it.toString() },
        localCoverUri = this.localCoverUri.toString(),
        path = this.path.toString(),
        romId = this.romID,
        console = this.consoles,
        lastModified = lastModified,
        coverTimeStamp = this.coverTimeStamp
    )
}

fun List<SavedRomsEntity>.toGame(): List<Game> {
    return this.map { it.toGame() }
}