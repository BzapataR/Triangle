//
// AddFilesContextMenu.kt
// Triangle
//
// Created by Brian Zapata Resendiz on 11/24/2025


package com.bzapata.triangle.emulatorScreen.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuGroup
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.DropdownMenuPopup
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bzapata.triangle.R
import com.bzapata.triangle.ui.theme.TriangleTheme

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FileContextMenu(
    subMenuOpen: Boolean,
    fileMenuToggle: () -> Unit,
    onChangeUserFolder: () -> Unit,
    onChangeRomsFolder: () -> Unit
) {
    DropdownMenuPopup(
        onDismissRequest = { fileMenuToggle() },
        expanded = subMenuOpen,
    ) {
        DropdownMenuGroup(
            shapes = MenuDefaults.groupShape(0, 3)
        ) {
            MenuDefaults.Label {
                Text(
                    text = "Paths",
                    // modifier = Modifier.padding(horizontal = 12.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            HorizontalDivider(modifier = Modifier.padding(horizontal = 12.dp))

            DropdownMenuItem(
                text = { Text("Change User Folder") },
                onClick = {
                    onChangeUserFolder()
                    fileMenuToggle()
                },
                trailingIcon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.outline_home_24),
                        contentDescription = "Change User Folder"
                    )
                }
            )

            HorizontalDivider(modifier = Modifier.padding(horizontal = 12.dp))

            DropdownMenuItem(
                text = { Text("Change ROMs Folder") },
                onClick = {
                    onChangeRomsFolder()
                    fileMenuToggle()
                },
                trailingIcon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.videogame_asset_24dp),
                        contentDescription = "Change ROMs Folder"
                    )
                }
            )
        }
    }
}


@Preview
@Composable
fun FileContextMenuPreview() {
    TriangleTheme {
        FileContextMenu(
            subMenuOpen = true,
            {},
            {},
            {}
        )
    }
}