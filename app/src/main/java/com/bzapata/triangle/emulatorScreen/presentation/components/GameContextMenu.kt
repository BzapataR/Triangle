//
// GameContextMenu.kt
// Triangle
//
// Created by Brian Zapata Resendiz on 11/22/2025
//
package com.bzapata.triangle.emulatorScreen.presentation.emulators.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bzapata.triangle.R
import com.bzapata.triangle.emulatorScreen.domain.Game
import com.bzapata.triangle.emulatorScreen.domain.GameUiExample
import com.bzapata.triangle.emulatorScreen.presentation.EmulatorActions
import com.bzapata.triangle.emulatorScreen.presentation.EmulatorState
import com.bzapata.triangle.ui.theme.TriangleTheme

@Composable
fun GameContextMenu(
    game: Game,
    expanded: Boolean,
    onActions: (EmulatorActions) -> Unit,
    state: EmulatorState,

    ) {
    var currentMenu by remember { mutableStateOf("main") }

    // When the menu is closed, reset its state to the main menu.
    LaunchedEffect(expanded) {
        if (!expanded) {
            currentMenu = "main"
        }
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { onActions(EmulatorActions.ToggleGameContextMenu(null)) },
        shape = MaterialTheme.shapes.medium,
    ) {
        when (currentMenu) {
            "main" -> {
                Text(
                    text = game.name,
                    fontSize = 8.sp,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp),
                    color = MaterialTheme.colorScheme.outline
                )
                HorizontalDivider()

                DropdownMenuItem(
                    text = { Text("Rename") },
                    onClick = {},
                    trailingIcon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.sharp_edit_24),
                            contentDescription = "Edit Game Name"
                        )
                    }
                )
                HorizontalDivider()

                DropdownMenuItem(
                    text = { Text("Change Artwork") },
                    onClick = {
                        onActions(EmulatorActions.ToggleCoverActionSheet)
                        onActions(EmulatorActions.ToggleGameContextMenu(null))
                        onActions(EmulatorActions.SelectGame(game))
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.sharp_image_24),
                            contentDescription = "Edit Artwork"
                        )
                    }
                )
                HorizontalDivider()

                DropdownMenuItem(
                    text = { Text("Share") },
                    onClick = {},
                    trailingIcon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.outline_ios_share_24),
                            contentDescription = null
                        )
                    }
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.background, thickness = 8.dp)


                DropdownMenuItem(
                    text = { Text("Game Settings") },
                    onClick = {},
                    trailingIcon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.settings),
                            contentDescription = "Change Game Settings"
                        )
                    }
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.background, thickness = 8.dp)


                DropdownMenuItem(
                    text = { Text("View Save States") },
                    onClick = {},
                    trailingIcon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.outline_folder_open_24),
                            contentDescription = "View Saves States"
                        )
                    }
                )
                HorizontalDivider()

                DropdownMenuItem(
                    text = { Text("Manage Save Files") },
                    onClick = { currentMenu = "saves" },
                    leadingIcon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.outline_keyboard_arrow_right_24),
                            contentDescription = null
                        )
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.outline_folder_managed_24),
                            contentDescription = "Mange Save Files"
                        )
                    }
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.background, thickness = 8.dp)


                DropdownMenuItem(
                    text = {
                        Text(
                            text = "Delete ROM",
                            color = MaterialTheme.colorScheme.error,
                        )
                    },
                    onClick = {},
                    trailingIcon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.outline_delete_24),
                            contentDescription = "Delete Game",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                )
            }

            "saves" -> {
                DropdownMenuItem(
                    text = { Text("Back") },
                    onClick = { currentMenu = "main" },
                    leadingIcon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.outline_keyboard_arrow_left_24),
                            contentDescription = "Back"
                        )
                    }
                )
                HorizontalDivider()
                DropdownMenuItem(
                    text = { Text("Import Saves") },
                    onClick = {},
                    trailingIcon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.sharp_move_to_inbox_24),
                            contentDescription = "Import saves"
                        )
                    }
                )
                DropdownMenuItem(
                    text = { Text("Export Saves") },
                    onClick = {},
                    trailingIcon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.outline_outbox_24),
                            contentDescription = "Export Saves",
                        )
                    }
                )
            }
        }
    }
}


@Preview
@Composable
fun GameContextMenuPreview() {
    TriangleTheme {
        GameContextMenu(
            GameUiExample,
            expanded = true,
            onActions = {},
            state = EmulatorState(),
        )
    }
}
