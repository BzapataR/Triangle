package com.bzapata.triangle.emulatorScreen.data.fileOperations

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.documentfile.provider.DocumentFile
private val ROM_EXTENSIONS = setOf(
    ".gbc", ".gb", ".gba", ".nes", ".smc", ".sfc",
    ".fig", ".ds", ".nds", ".n64", ".z64"
)
fun getRomFiles(context : Context, folderUri : Uri) : List<Uri> { //todo add cache file to avoid this, ~takes 10
    val foundRoms = mutableListOf<Uri>()
    val folder  = DocumentFile.fromTreeUri(context, folderUri)

    if (folder?.isDirectory == true) {
        val files = folder.listFiles()
        for (file in files) {
            if (file.isDirectory) {
                foundRoms.addAll(getRomFiles(context,file.uri))
            }
            else {
                val name = file.name ?: continue
                if (ROM_EXTENSIONS.any{ name.endsWith(it) }) {
                    foundRoms.add(file.uri)
                }
            }
        }
    }
    return foundRoms
}