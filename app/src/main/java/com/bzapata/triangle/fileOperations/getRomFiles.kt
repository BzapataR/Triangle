package com.bzapata.triangle.fileOperations

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile

fun getRomFiles(context : Context, folderUri : Uri) : List<DocumentFile> {
    val folder  = DocumentFile.fromTreeUri(context, folderUri) ?: return emptyList()

    return folder.listFiles().filter { file ->
        file.isFile && (
                file.name?.endsWith(".gba", true) == true ||
                file.name?.endsWith(".gbc", true) == true ||
                file.name?.endsWith(".gb",true) == true
                )
    }
}