package com.bzapata.triangle.emulatorScreen.data.fileOperations

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.core.graphics.drawable.toBitmap
import androidx.documentfile.provider.DocumentFile
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import okio.IOException

suspend fun downloadCover(
    context: Context,
    gameHash: String,
    imageUri: Uri,
    userFolder: Uri
): Uri? {
    val userDirectory = DocumentFile.fromTreeUri(context, userFolder)

    val coversDirectory = userDirectory?.findFile("covers")
        ?: userDirectory?.createDirectory("covers")

    if (coversDirectory == null || !coversDirectory.isDirectory) {
        Log.e("download Cover", "Failed to find or create the covers directory")
        return null
    }

    if (coversDirectory.findFile(".nomedia") == null) {
        coversDirectory.createFile("application/octet-stream", ".nomedia")
        Log.i("downloadCover", "Created .nomedia file in covers directory.")
    }

    val fileName = "$gameHash.png"

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
                }
                Log.i(
                    "download Cover",
                    "Successfully saved ${bitMap.width}x${bitMap.height} cover for $gameHash ${newCoverFile.uri.path}"
                )
                return newCoverFile.uri
            }
        } catch (e: IOException) {
            Log.e("download Cover", "Failed to save image for $gameHash ", e)
            return null
        }
    } else {
        Log.e("download Cover", " Failed to download image for $gameHash from $imageUri")
    }
    return null
}

suspend fun downloadCover(
    context: Context,
    gameHash: String,
    imageUris: List<Uri>,
    userFolder: Uri
): Uri? {
    val userDirectory = DocumentFile.fromTreeUri(context, userFolder)

    val coversDirectory = userDirectory?.findFile("covers")
        ?: userDirectory?.createDirectory("covers")

    if (coversDirectory == null || !coversDirectory.isDirectory) {
        Log.e("download Cover", "Failed to find or create the covers directory")
        return null
    }

    if (coversDirectory.findFile(".nomedia") == null) {
        coversDirectory.createFile("application/octet-stream", ".nomedia")
        Log.i("downloadCover", "Created .nomedia file in covers directory.")
    }

    val fileName = "$gameHash.png"
    val imageResults = mutableListOf<Bitmap>()
    val imageLoader = ImageLoader(context)

    for (image in imageUris) {
        val request = ImageRequest.Builder(context)
            .data(image)
            .allowHardware(false)
            .build()
        val result = imageLoader.execute(request)

        if (result is SuccessResult) {
            val bitMap = result.drawable.toBitmap()
            imageResults.add(bitMap)
        }
    }
    val bestQualityBitmap = imageResults.maxByOrNull { it.width * it.height } ?: return null

    try {
        val existingFile = coversDirectory.findFile(fileName)
        existingFile?.delete()
        val newCoverFile = coversDirectory.createFile("image/png", fileName)

        if (newCoverFile != null) {
            context.contentResolver.openOutputStream(newCoverFile.uri)?.use { outputStream ->
                bestQualityBitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream)
            }
            Log.i("download Cover", "Successfully save cover to ${newCoverFile.uri.path}")
            return newCoverFile.uri
        }
    } catch (e: IOException) {
        Log.e("download Cover", "Failed to save image for $gameHash ", e)
        return null
    }
    return null
}
