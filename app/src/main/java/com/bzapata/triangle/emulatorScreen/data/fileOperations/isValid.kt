package com.bzapata.triangle.emulatorScreen.data.fileOperations

import android.content.Context
import android.net.Uri

fun Uri.isValid(context : Context) : Boolean {
    return try {
        context.contentResolver.openOutputStream(this)?.close()
        true
    }
    catch (_ : Exception) {
        false
    }
}