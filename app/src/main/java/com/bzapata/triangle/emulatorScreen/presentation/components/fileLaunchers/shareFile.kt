package com.bzapata.triangle.emulatorScreen.presentation.components.fileLaunchers

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun shareFile(uri: Uri): () -> Unit {
    val context = LocalContext.current
    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        type = "application/octet-stream"
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    return {
        context.startActivity(Intent.createChooser(sendIntent, "Share ROMs"))
    }
}