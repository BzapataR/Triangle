package com.bzapata.triangle.emulatorScreen.domain

import androidx.compose.ui.graphics.painter.Painter

data class Game(
    val name : String,
    //val romID : String,
    //val path : String,
    //val settings : ROOMDB SETTINGS
    val cover : Painter,
)