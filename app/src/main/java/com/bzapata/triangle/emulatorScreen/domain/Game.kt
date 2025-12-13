package com.bzapata.triangle.emulatorScreen.domain

import android.graphics.Bitmap
import android.net.Uri


data class Game(
    val name : String,
    val romID : Int,
    val path : Uri,
//val settings : ROOMDB SETTINGS
    val coverDownloaderUri : Uri?,
    val cover : Uri?,
    val consoles: Consoles
)
