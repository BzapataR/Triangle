package com.bzapata.triangle.deltaCoreKt

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import com.bzapata.triangle.deltaCoreKt.protocols.model.ControllerSkinProtocol
import com.bzapata.triangle.deltaCoreKt.types.GameType
import java.io.File

class Delta(private val context : Context) {
    private val _registeredCores = mutableMapOf< GameType , ControllerSkinProtocol>()
    val registeredCores : Map<GameType, ControllerSkinProtocol> get() = _registeredCores

    fun register(core : ControllerSkinProtocol) {
        _registeredCores[core.gameType] = core
    }

    fun unregister(core : ControllerSkinProtocol) {
        val registeredCore = _registeredCores[core.gameType]
        if (registeredCore == core) {
            _registeredCores.remove(core.gameType)
        }
    }

    fun core(forGameType : GameType) : ControllerSkinProtocol?  = _registeredCores[forGameType]
    val coresDirectoryUri : Uri by lazy{
        val coresDir = File(context.filesDir, "Cores").apply {
            if (!exists())
                mkdirs()
        }
        coresDir.toUri()
    }
}
