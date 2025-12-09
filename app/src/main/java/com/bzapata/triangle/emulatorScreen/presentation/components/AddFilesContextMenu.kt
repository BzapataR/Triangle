//
// AddFilesContextMenu.kt
// Triangle
//
// Created by Brian Zapata Resendiz on 11/24/2025


package com.bzapata.triangle.emulatorScreen.presentation.components

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.documentfile.provider.DocumentFile
import com.bzapata.triangle.R
import com.bzapata.triangle.fileOperations.directoryPicker
import com.bzapata.triangle.fileOperations.getRomFiles

@Composable
fun FileContextMenu(
    subMenuOpen:Boolean,
    fileMenuToggle : () -> Unit
) {


    DropdownMenu(
        onDismissRequest = { fileMenuToggle() },
        expanded = subMenuOpen,
        shape = MaterialTheme.shapes.medium,
    ) {
        Text(
            text = "Paths",
            fontSize = 8.sp,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp ),
            color = MaterialTheme.colorScheme.outline
        )
        HorizontalDivider()

        DropdownMenuItem(
            text = { Text("Change User Folder") },
            onClick = {
            },
            trailingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.outline_home_24),
                    contentDescription = "Edit Game Name"
                )
            }
        )

        HorizontalDivider()

        DropdownMenuItem(
            text = { Text("Change Application Folders") },
            onClick = {},
            trailingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.videogame_asset_24dp),
                    contentDescription = "Edit Game Name"
                )
            }
        )

        HorizontalDivider()

        DropdownMenuItem(
            text = { Text("Set Save Folder") },
            onClick = {},
            trailingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.outline_save_24),
                    contentDescription = "Edit Game Name"
                )
            }
        )
    }
}