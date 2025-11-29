//
// AddFilesContextMenu.kt
// Triangle
//
// Created by Brian Zapata Resendiz on 11/24/2025


package com.bzapata.triangle.ui.components

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
import com.bzapata.triangle.fileOperations.selectRomFolder

@Composable
fun FileContextMenu(
    subMenuOpen:Boolean,
    fileMenuToggle : () -> Unit
) {
    var Roms : List<DocumentFile>
    val context = LocalContext.current
    val romFolderPicker = directoryPicker { uri ->
        if (uri != null) {
            Roms = getRomFiles(context, uri)
            Roms.forEach { Log.i("Roms", "${it.name}") }
        }
    }
    var romUri = Uri.EMPTY

    DropdownMenu(
        onDismissRequest = { fileMenuToggle() },
        expanded = subMenuOpen,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = "Import From...",
            fontSize = 8.sp,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp ),
            color = MaterialTheme.colorScheme.outline
        )
        HorizontalDivider()

        DropdownMenuItem(
            text = { Text("Device") },
            onClick = {
                romFolderPicker()
            },
            trailingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.outline_folder_open_24),
                    contentDescription = "Edit Game Name"
                )
            }
        )

        HorizontalDivider()

        DropdownMenuItem(
            text = { Text("Cloud") },
            onClick = {},
            trailingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.outline_cloud_24),
                    contentDescription = "Edit Game Name"
                )
            }
        )
    }
}