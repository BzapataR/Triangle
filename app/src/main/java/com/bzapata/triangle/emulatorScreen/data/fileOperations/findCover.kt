package com.bzapata.triangle.emulatorScreen.data.fileOperations

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.documentfile.provider.DocumentFile

fun findCover(context: Context, userFolder: Uri, romID: Int): Uri? {
    val userDirectory = DocumentFile.fromTreeUri(context, userFolder)

    val coversDirectory = userDirectory?.findFile("covers")
        ?: userDirectory?.createDirectory("covers")

    if (coversDirectory == null || !coversDirectory.isDirectory) {
        Log.e("download Cover", "Failed to find or create the covers directory")
        return null
    }

    val fileName = "$romID.png"

    val existingFile = coversDirectory.findFile(fileName)
    if (existingFile != null) {
        Log.i("find cover", "Found Game Cover: $fileName")
        return existingFile.uri
    }
    Log.i("find cover", "No cover found")
    return null


}