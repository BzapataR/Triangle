package com.bzapata.triangle.fileOperations

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable

@Composable
fun selectRomFolder() : Uri {
    var romFolder: Uri = Uri.EMPTY
    directoryPicker { uri ->
        if (uri != null) {
            Log.i("selectRomFolder", "ROM Folder: $uri")
            romFolder = uri
        }
    }
    return romFolder
}