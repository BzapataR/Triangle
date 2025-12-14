package com.bzapata.triangle.emulatorScreen.data.fileOperations

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import com.bzapata.triangle.emulatorScreen.domain.Consoles

fun fileMapper(context: Context, path: Uri): Consoles {
    val fileName = DocumentFile.fromSingleUri(context, path)?.name
        ?: throw IllegalArgumentException("Invalid URI, cannot get filename from $path")
    return when {
        fileName.endsWith(".gbc", false) -> Consoles.GB
        fileName.endsWith(".gb", false) -> Consoles.GB
        fileName.endsWith(".gba", false) -> Consoles.GBA
        fileName.endsWith(".nes", false) -> Consoles.NES
        fileName.endsWith(".smc", false) -> Consoles.SNES
        fileName.endsWith(".sfc", false) -> Consoles.SNES
        fileName.endsWith(".fig", false) -> Consoles.SNES
        fileName.endsWith(".ds", false) -> Consoles.DS
        fileName.endsWith(".nds", false) -> Consoles.DS
        fileName.endsWith(".n64", false) -> Consoles.N64
        fileName.endsWith(".z64", false) -> Consoles.N64
        else -> throw IllegalArgumentException("Unknown console for file: $fileName")
    }
}
