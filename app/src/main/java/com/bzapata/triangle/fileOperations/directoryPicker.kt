package com.bzapata.triangle.fileOperations

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun directoryPicker(onDirectorySelected: (Uri?) -> Unit): () -> Unit { // return function to make it possible to be called on composable (i.e. onClick() )
    val context = LocalContext.current
    val contentResolver = context.contentResolver

    val directoryPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree(),
        onResult = { uri ->
            uri?.let {
                val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                contentResolver.takePersistableUriPermission(it, takeFlags)
            }
            onDirectorySelected(uri)
        }
    )
    return {
        directoryPickerLauncher.launch(null)
    }
}