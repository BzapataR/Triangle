package com.bzapata.triangle.emulatorScreen.domain

import android.net.Uri


data class Game(
    val name: String,
    val romID: Int,
    val path: Uri,
//val settings : ROOMDB SETTINGS
    val coverDownloaderUri: List<Uri?>,
    val localCoverUri: Uri?,
    val consoles: Consoles,
    val hash : String
)
