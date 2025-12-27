package com.bzapata.triangle.emulatorScreen.data.fileOperations

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.documentfile.provider.DocumentFile

fun copyFile(context: Context, sourceUri: Uri, destinationUri: Uri): Uri? {
    return try {
        val documentFile = DocumentFile.fromSingleUri(context, sourceUri) ?: return null
        val fileName = documentFile.name ?: hasher(context, sourceUri)

        val destinationDir = DocumentFile.fromTreeUri(context, destinationUri)
        val importedDir =
            destinationDir?.findFile("imported") ?: destinationDir?.createDirectory("imported")

        val targetFile =
            importedDir?.createFile("application/octet-stream", fileName) ?: return null

        //copying file contents
        context.contentResolver.openInputStream(sourceUri)?.use { input ->
            context.contentResolver.openOutputStream(targetFile.uri)?.use { output ->
                input.copyTo(output)
            }
        }

        return targetFile.uri
    } catch (e: Exception) {
        Log.e("GameRepo", "Failed to copy external Rom", e)
        null
    }
}