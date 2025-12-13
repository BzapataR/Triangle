package com.bzapata.triangle.emulatorScreen.data.fileOperations

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.compose.ui.unit.IntOffset
import androidx.core.graphics.drawable.toBitmap
import androidx.documentfile.provider.DocumentFile
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import okio.IOException

suspend fun downloadCover(
    context: Context,
    gameName: String,
    romID : Int,
    imageUri: Uri,
    userFolder : Uri
) : Uri? {
    val userDirectory= DocumentFile.fromTreeUri(context, userFolder)

    val coversDirectory = userDirectory?.findFile("covers")
        ?: userDirectory?.createDirectory("covers")

    if (coversDirectory == null || !coversDirectory.isDirectory) {
        Log.e("download Cover", "Failed to find or create the covers directory")
        return null
    }

    val fileName = "$romID.png"

    val imageLoader = ImageLoader(context)
    val request = ImageRequest.Builder(context)
        .data(imageUri)
        .allowHardware(false)
        .build()
    val result = imageLoader.execute(request)

    if (result is SuccessResult) {
        val bitMap = result.drawable.toBitmap()

        try {
            val existingFile = coversDirectory.findFile(fileName)
            existingFile?.delete()
            val newCoverFile = coversDirectory.createFile("image/png", fileName)

            if (newCoverFile != null) {
                context.contentResolver.openOutputStream(newCoverFile.uri)?.use { outputStream ->
                    bitMap.compress(Bitmap.CompressFormat.PNG, 90, outputStream)
                    Log.i("download Cover", "Successfully save cover to ${newCoverFile.uri.path}")
                    return newCoverFile.uri
                }
            }
        } catch (e: IOException) {
            Log.e("download Cover", "Failed to save image for $gameName ", e)
            return null
        }
    }
    else {
        Log.e("download Cover", " Failed to download image for $gameName from $imageUri")
    }
    return null
}