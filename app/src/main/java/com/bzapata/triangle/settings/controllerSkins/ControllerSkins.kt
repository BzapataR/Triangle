package com.bzapata.triangle.settings.controllerSkins

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bzapata.triangle.R
import com.bzapata.triangle.emulatorScreen.presentation.components.PdfImage
import com.bzapata.triangle.settings.SettingsPageTemplate
import com.bzapata.triangle.settings.SubText
import com.bzapata.triangle.ui.theme.TriangleTheme

@Composable
fun ControllerSkinsRoot(goBack: () -> Unit = {}) {
    ControllerSkins(goBack)
}

@Composable
fun ControllerSkins(goBack: () -> Unit) {
    SettingsPageTemplate(
        goBack = {goBack() },
        title = "NES", // placeholder
    ) {
        item {
            SubText("PORTRAIT")
            PdfImage(
                assetFileName = "iphone_edgetoedge_portrait.pdf",
                modifier = Modifier.fillMaxSize()
            )
        }
        item {
            SubText("LANDSCAPE")
            PdfImage(
                assetFileName = "iphone_edgetoedge_landscape.pdf",
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Preview
@Composable
fun ControllerSkinsPreview() {
    TriangleTheme {
        ControllerSkins {}
    }
}
