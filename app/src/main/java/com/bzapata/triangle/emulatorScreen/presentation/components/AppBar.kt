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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
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
    onChangeRomsFolder: () -> Unit
) {
    Column(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = currentEmulatorName,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
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
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )
        SearchField {}
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)


    }
}

@Preview
@Composable
private fun TopAppBarPreview() {
    TriangleTheme {
        AppBar(
            settingsToggle = {},
            isMenuOpen = true,
            fileToggle = {},
            currentEmulatorName = "GBA",
            onChangeUserFolder = {},
            onChangeRomsFolder = {}
        )
    }
}
