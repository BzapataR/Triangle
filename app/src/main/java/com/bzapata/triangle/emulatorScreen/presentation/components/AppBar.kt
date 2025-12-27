//
// AppBar.kt
// Triangle
//
// Created by Brian Zapata Resendiz on 11/19/2025
//
// Starting Point
package com.bzapata.triangle.emulatorScreen.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bzapata.triangle.R
import com.bzapata.triangle.ui.theme.TriangleTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    settingsToggle: () -> Unit,
    fileToggle: () -> Unit,
    isMenuOpen: Boolean,
    currentEmulatorName: String,
    onChangeUserFolder: () -> Unit,
    onChangeRomsFolder: () -> Unit,
    onQuery :(String) -> Unit,
    windowWidth: WindowWidthSizeClass
) {

    val isExpanded = windowWidth == WindowWidthSizeClass.Expanded

    Column(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = currentEmulatorName,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    //modifier = Modifier.height(40.dp)
                )
            },
            navigationIcon = {
                IconButton(onClick = { settingsToggle() }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.settings),
                        contentDescription = "Settings",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            },
            actions = {
                IconButton(onClick = { fileToggle() }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.outline_add_24),
                        contentDescription = "Add Games",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                FileContextMenu(
                    subMenuOpen = isMenuOpen,
                    fileMenuToggle = { fileToggle() },
                    onChangeUserFolder = onChangeUserFolder,
                    onChangeRomsFolder = onChangeRomsFolder
                )
                if (isExpanded) {
                    SearchField(modifier = Modifier.width(275.dp)) {
                        onQuery(it)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )
        if (!isExpanded) {
            SearchField(Modifier.padding(bottom = 8.dp)) {
                onQuery(it)
            }
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)


    }
}

@Preview
@Composable
private fun TopAppBarPreview() {
    TriangleTheme {
        AppBar(
            settingsToggle = {},
            fileToggle = {},
            isMenuOpen = true,
            currentEmulatorName = "GBA",
            onChangeUserFolder = {},
            onChangeRomsFolder = {},
            windowWidth = WindowWidthSizeClass.Compact,
            onQuery = {}
        )
    }
}
