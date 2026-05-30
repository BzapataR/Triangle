package com.bzapata.triangle.deltaCoreKt.deltaCore.cores

import android.net.Uri
import androidx.core.net.toFile
import androidx.core.net.toUri
import com.bzapata.triangle.deltaCoreKt.Delta
import com.bzapata.triangle.deltaCoreKt.protocols.inputs.Input
import com.bzapata.triangle.deltaCoreKt.types.GameType
import org.koin.java.KoinJavaComponent.get
import java.io.File
import java.nio.file.Files.exists

interface DeltaCoreProtocol {
    //General
    val name : String
    val identifier : String
    val version : String? get() = null

    val gameType : GameType
    val gameSaveFileExtension : String

    val gameInputType : Input.Type

    //rendering
    val audioFormat : AVAudioFormat
    val videoFormat : VideoFormat

    // cheats
    val supportedCheatFormats : Set<CheatFormat>

    //Emulation
    val emulatorBridge : EmulatorBridging
    val resourceBundle : Bundle
    val directoryURI : Uri get() {
        val delta : Delta = get(Delta::class.java)
        val baseFile = File(delta.coresDirectoryUri.path ?: "")
        val coreDir = File(baseFile, name).apply {
            if (!exists())
                mkdirs()
        }
        return coreDir.toUri()
    }
    val description : String get() = "$name ($identifier)"
}
