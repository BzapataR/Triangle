package com.bzapata.triangle.util.fileLaunchers

import android.content.Intent
import android.net.Uri
import android.provider.DocumentsContract
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

//this function can't tell the difference between a cloud uri from say NextCloud (tested) and on device uri.
@Composable
fun openPath(uri: Uri): () -> Unit {
    val context = LocalContext.current
    val tag = "Open Path"
    return {
        try {
            val targetUri = if (DocumentsContract.isTreeUri(uri)) {
                val documentId = DocumentsContract.getTreeDocumentId(uri)
                DocumentsContract.buildDocumentUriUsingTree(uri, documentId)
            } else {
                uri
            }
            Log.i(tag, "$targetUri")
            try {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    setDataAndType(targetUri, DocumentsContract.Document.MIME_TYPE_DIR)
                }
                context.startActivity(intent)
            } catch (e: Exception) {
                Log.e(tag, "Failed to open directory, tying fallback method", e)
                val fallbackIntent = Intent(Intent.ACTION_VIEW).apply {
                    setData(uri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(fallbackIntent)
            }
        }
        catch (e: Exception) {
            Log.e(tag, "Failure to open URI", e)
        }
    }
}