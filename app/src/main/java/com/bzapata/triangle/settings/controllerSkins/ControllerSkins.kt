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
import com.bzapata.triangle.settings.SubText
import com.bzapata.triangle.emulatorScreen.presentation.components.PdfImage
import com.bzapata.triangle.ui.theme.TriangleTheme

@Composable
fun ControllerSkinsRoot(goBack : () -> Unit = {}) {
    ControllerSkins(goBack)
}

@Composable
fun ControllerSkins(goBack: () -> Unit) {
    val listState = rememberLazyListState()

    val headerAlpha by remember {
        derivedStateOf {
            if (listState.firstVisibleItemIndex > 0) 1f
            else (listState.firstVisibleItemScrollOffset / 120f).coerceIn(0f, 1f)
        }
    }
    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    )
    {
        stickyHeader {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xff1c1c1e))
                    .padding(vertical = 8.dp),
            ) {
                Text(
                    text = "NES",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .graphicsLayer {
                            alpha = headerAlpha
                            translationY = (1f - headerAlpha) * -12f
                        }
                        .align(Alignment.Center)
                )

                TextButton(
                    onClick = { goBack() },
                    modifier = Modifier.align(Alignment.CenterStart),
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.outline_keyboard_arrow_left_24),
                        contentDescription = null
                    )
                    Text(
                        text = "Settings",
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.End,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }
        }
        item {
            Text(
                text = "NES",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )

            Spacer(modifier = Modifier.size(24.dp))

        }
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
        ControllerSkins({})
    }
}
