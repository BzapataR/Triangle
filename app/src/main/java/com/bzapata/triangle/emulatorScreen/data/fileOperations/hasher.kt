package com.bzapata.triangle.emulatorScreen.data.fileOperations

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.documentfile.provider.DocumentFile
import java.io.InputStream
import java.security.MessageDigest
import java.util.Locale.getDefault

fun hasher(context: Context, path: Uri): String {
    return try {
        val inputStream: InputStream? = context.contentResolver.openInputStream(path)
        inputStream?.use { stream ->
            val digest = MessageDigest.getInstance("SHA-1")
            val buffer = ByteArray(8192)
            var bytesRead: Int

            while (stream.read(buffer).also { bytesRead = it } != -1) {
                digest.update(buffer, 0, bytesRead)
            }

            val finalHash =
                digest.digest().joinToString("") { "%02x".format(it) }.uppercase(getDefault())
            Log.i(
                "hasher",
                "SHA1 Hash: $finalHash for file ${DocumentFile.fromSingleUri(context, path)?.name}"
            )
            finalHash
        } ?: ""
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}