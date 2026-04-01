package com.bzapata.triangle.deltaCoreKt.protocols.model

import android.net.Uri
import com.bzapata.triangle.deltaCoreKt.Delta
import com.bzapata.triangle.deltaCoreKt.types.GameType
import androidx.core.net.toUri

sealed interface GameProtocol {
    var fileURL : Uri
    val gameSaveUri : Uri get()  {
        val fileExtension = Delta.core(this.type)?.gameSaveFileExtension ?: "sav"
        val path = fileURL.toString()

        val lastDotIndex = path.lastIndexOf('.')
        val newPath = if (lastDotIndex != -1) {
            path.substring(0,lastDotIndex) + "." + fileExtension
        }
        else {
            "$path.$fileExtension"
        }
        return newPath.toUri()
    }

    var type : GameType
}