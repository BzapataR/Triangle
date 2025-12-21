package com.bzapata.triangle.emulatorScreen.data.fileOperations

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import android.util.Log

fun getUriFromClipboard(context: Context): Uri {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
    val clipData: ClipData? = clipboard?.primaryClip

    if (clipData != null && clipData.itemCount > 0) {
        val item: ClipData.Item = clipData.getItemAt(0)
        val imageUri: Uri? = item.uri

        if (imageUri != null) {
            val contentResolver = context.contentResolver
            val uriMimeType: String? = contentResolver.getType(imageUri)
            
            // Verify if the URI points to an image
            if (uriMimeType?.startsWith("image/") == true) {
                Log.i("Clipboard" ," Image found in Clipboard with URI: $imageUri")
                return imageUri
            } else {
                Log.w("Clipboard", "Clip item is not an image. MimeType: $uriMimeType")
            }
        } else {
            Log.w("Clipboard", "Clip item does not contain a Uri")
        }
    }
    throw IllegalStateException("No image found in clipboard")
}
