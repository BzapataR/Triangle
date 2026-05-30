package com.bzapata.triangle.deltaCoreKt.deltaCore.cores

import android.net.Uri
typealias TimeInterval = Double
interface EmulatorBridging {
    val gameURL : Uri
    val frameDuration : TimeInterval
    val audioRenderer : AudioRendering?
    val videoRenderer : VideoRendering?
    val saveUpdateHandler : (() -> Unit)

    fun start(withGameURL : Uri)
    fun stop()
    fun pause()
    fun resume()

    //Game Loop
    fun runFrame(processVideo: Boolean)

    ///Inputs
    fun activateInput(input : Int, value : Double, playerIndex : Int)
    fun deactivateInput(input : Int, playerIndex : Int)
    fun resetInputs()

    //Save States
    fun saveStateToUri(to: Uri)
    fun loadSaveState(from : Uri)

    //Game Saves
    fun saveGameSave(to : Uri)
    fun loadGameSave(from : Uri)

    //Cheats
    fun addCheatCode(cheatCode : String, type : String) : Boolean
    fun resetCheats()
    fun updateCheats()

    //Memory
    fun readMemory(at : Int, size : Int) : ByteArray? = null
}