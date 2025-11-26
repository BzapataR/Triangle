package com.bzapata.triangle

import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector

data class Game(
    val name : String,
    //val romID : String,
    //val path : String,
    //val settings : ROOMDB SETTINGS
    val cover : Painter,
)