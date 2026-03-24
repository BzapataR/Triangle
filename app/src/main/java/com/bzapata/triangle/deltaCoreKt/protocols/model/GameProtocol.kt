package com.bzapata.triangle.deltaCoreKt.protocols.model

import android.net.Uri

sealed interface GameProtocol {
    var fileURL : Uri
    var gameSaveUri : Uri

    var type : GameType
}