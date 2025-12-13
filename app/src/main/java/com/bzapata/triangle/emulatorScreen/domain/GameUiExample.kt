package com.bzapata.triangle.emulatorScreen.domain

import android.net.Uri
import androidx.compose.ui.graphics.painter.Painter

 val GameUiExample = Game(
    name = "Mole Mania",
    romID =-1,
     path = Uri.EMPTY,
     coverDownloaderUri = emptyList(),
    localCoverUri = Uri.EMPTY,
    consoles = Consoles.GB
)