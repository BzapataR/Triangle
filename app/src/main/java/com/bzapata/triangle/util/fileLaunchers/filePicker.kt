package com.bzapata.triangle.util.fileLaunchers


import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun filePicker(
    onFileSelected: (Uri?) -> Unit
): () -> Unit { // return function to make it possible to be called on composable (i.e. onClick() )
    val context = LocalContext.current
    val contentResolver = context.contentResolver

    val filePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            uri?.let {
                val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                contentResolver.takePersistableUriPermission(it, takeFlags)
            }
            onFileSelected(uri)
            Log.i("directory picker", "path selected: ${uri?.path}")
        }
    )
    return {
        filePicker.launch(arrayOf("image/*"))
    }
}
