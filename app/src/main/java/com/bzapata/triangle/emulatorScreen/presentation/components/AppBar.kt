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
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bzapata.triangle.R
import com.bzapata.triangle.data.controller.ControllerManager
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
    windowWidth: WindowWidthSizeClass,
    controllerType: ControllerManager.ControllerType? = null,
) {

    val isWideScreen = windowWidth != WindowWidthSizeClass.Compact

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
    )
    {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = currentEmulatorName,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = { settingsToggle() },
                    modifier = Modifier.focusProperties { canFocus = false }
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.settings),
                        contentDescription = "Settings",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            },
            actions = {
                IconButton(
                    onClick = { fileToggle() },
                    modifier = Modifier.focusProperties { canFocus = false }
                ) {
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
                if (isWideScreen) {
                    SearchField(
                        modifier = Modifier
                            .width(275.dp)
                            .focusProperties { canFocus = true },
                        onSearch = { onQuery(it) },
                        controllerType = controllerType
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )
        if (!isWideScreen) {
            SearchField(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .focusProperties { canFocus = true }, // todo make false when controller connected but still make it accessible by pressing Triangle button(or xbox/nintendo equivalent)

                onSearch = { onQuery(it) },
                controllerType = controllerType
            )
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
            onQuery = {},
        )
    }
}
