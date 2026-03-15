package com.bzapata.triangle.settings.Paths

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuGroup
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.DropdownMenuPopup
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
fun PathsContextMenu(
        expanded: Boolean,
        expansionToggle : () -> Unit,
        removePath : () -> Unit,
        openPath : () -> Unit

        ) {
    DropdownMenuPopup(
        onDismissRequest = { expansionToggle() },
        expanded = expanded,
    ) {
        DropdownMenuGroup(
            shapes = MenuDefaults.groupShape(0, 2)
        ) {
//            MenuDefaults.Label {
//                Text(
//                    text = "Paths",
//                    // modifier = Modifier.padding(horizontal = 12.dp),
//                    color = MaterialTheme.colorScheme.primary
//                )
//            }
//            HorizontalDivider(modifier = Modifier.padding(horizontal = 12.dp))

            DropdownMenuItem(
                text = { Text("Open Directory") },
                onClick = {
                    openPath()
                    expansionToggle()
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
                text = { Text("Remove from ROM list") },
                onClick = {
                    removePath()
                    expansionToggle()
                },
                trailingIcon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.outline_delete_24),
                        contentDescription = "Change ROMs Folder"
                    )
                }
            )
        }
    }
}

@Preview
@Composable
private fun PathsContextMenuPreview() {
    TriangleTheme {
        PathsContextMenu(true, {}, {}, {})
    }
}