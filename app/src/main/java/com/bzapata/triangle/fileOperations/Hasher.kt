package com.bzapata.triangle.fileOperations

import android.content.Context
import androidx.documentfile.provider.DocumentFile
import java.io.InputStream
import java.security.MessageDigest

fun hasher(context : Context, file : DocumentFile) : String {
    return try {
        val inputStream : InputStream? = context.contentResolver.openInputStream(file.uri)
        inputStream?.use { stream ->
            val digest = MessageDigest.getInstance("SHA-1")
            val buffer = ByteArray(8192)
            var bytesRead : Int

            while (stream.read(buffer).also { bytesRead = it } != 1) {
                digest.update(buffer, 0, bytesRead)
            }

            digest.digest().joinToString("") { "02x".format(it) }
        } ?: ""
    }
    catch (e : Exception) {
        e.printStackTrace()
        ""
    }
}