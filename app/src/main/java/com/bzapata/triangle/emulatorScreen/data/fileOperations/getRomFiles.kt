package com.bzapata.triangle.emulatorScreen.data.fileOperations

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow

private val ROM_EXTENSIONS = setOf(
    ".gbc", ".gb", ".gba", ".nes", ".smc", ".sfc",
    ".fig", ".ds", ".nds", ".n64", ".z64"
)

fun getRomFiles(context: Context, folderUri: Uri): Flow<Uri> = channelFlow {
    val folder = DocumentFile.fromTreeUri(context, folderUri)

    val directoryStack = ArrayDeque<DocumentFile>()
    if (folder?.isDirectory == true) {
        directoryStack.add(folder)
    }

    while (directoryStack.isNotEmpty()) {
        val currentDirectory = directoryStack.removeFirst()
        val files = currentDirectory.listFiles()

        for (file in files) {
            if (file.isDirectory) {
                directoryStack.add(file)
            } else {
                val name = file.name ?: continue
                if (ROM_EXTENSIONS.any { name.endsWith(it, ignoreCase = true) }) {
                    send(file.uri)
                }
            }
        }
    }
}
