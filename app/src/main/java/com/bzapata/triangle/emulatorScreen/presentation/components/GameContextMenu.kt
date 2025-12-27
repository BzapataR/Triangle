//
// GameContextMenu.kt
// Triangle
//
// Created by Brian Zapata Resendiz on 11/22/2025
//
package com.bzapata.triangle.emulatorScreen.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bzapata.triangle.R
import com.bzapata.triangle.emulatorScreen.domain.Game
import com.bzapata.triangle.emulatorScreen.domain.GameUiExample
import com.bzapata.triangle.emulatorScreen.presentation.EmulatorActions
import com.bzapata.triangle.emulatorScreen.presentation.EmulatorState
import com.bzapata.triangle.emulatorScreen.presentation.components.fileLaunchers.shareFile
import com.bzapata.triangle.ui.theme.TriangleTheme

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun GameContextMenu(
    game: Game,
    expanded: Boolean,
    onActions: (EmulatorActions) -> Unit,
    state: EmulatorState,

    ) {
    var currentMenu by remember { mutableStateOf("main") }

    val launchShareSheet = shareFile(state.selectedGame?.path ?: return)

    val context = LocalContext.current

    // When the menu is closed, reset its state to the main menu.
    LaunchedEffect(expanded) {
        if (!expanded) {
            currentMenu = "main"
        }
    }

    DropdownMenuPopup(
        expanded = expanded,
        onDismissRequest = { onActions(EmulatorActions.ToggleGameContextMenu(null)) },
        // Use a rounded shape so the outer container doesn't stick out with sharp corners
        //shape = MaterialTheme.shapes.extraLarge,
    ) {
        when (currentMenu) {
            "main" -> {
                // Single Group for all items
                DropdownMenuGroup(
                    tonalElevation = 0.dp,
                    shadowElevation = 0.dp,
                    shapes = MenuDefaults.groupShape(
                        0,
                        1
                    ) // 0 of 1 group = Fully rounded top and bottom
                ) {
                    MenuDefaults.Label {
                        Text(
                            text = game.name,
                            //fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 12.dp))

                    DropdownMenuItem(
                        text = { Text("Rename") },
                        leadingIcon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.sharp_edit_24),
                                contentDescription = null
                            )
                        },
                        onClick = {
                            onActions(EmulatorActions.ToggleGameContextMenu(null))
                            onActions(EmulatorActions.ToggleRenameDialog)
                        }
                    )

                    DropdownMenuItem(
                        text = { Text("Change Artwork") },
                        leadingIcon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.sharp_image_24),
                                contentDescription = null
                            )
                        },
                        onClick = {
                            onActions(EmulatorActions.ToggleCoverActionSheet)
                            onActions(EmulatorActions.ToggleGameContextMenu(null))
                            onActions(EmulatorActions.SelectGame(game))
                        }
                    )

                    HorizontalDivider(modifier = Modifier.padding(horizontal = 12.dp))

                    DropdownMenuItem(
                        text = { Text("Share") },
                        leadingIcon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.outline_ios_share_24),
                                contentDescription = null
                            )
                        },
                        onClick = {
                            onActions(EmulatorActions.SelectGame(game))
                            launchShareSheet()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Create Shortcut") },
                        enabled = game.localCoverUri != null,
                        leadingIcon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.outline_jump_to_element_24),
                                contentDescription = null
                            )
                        },
                        onClick = {
                            if (game.localCoverUri != null)
                                createPinnedShortcut(
                                    context = context,
                                    shortcutId = game.hash,
                                    label = game.name,
                                    iconUri = game.localCoverUri
                                )
                        }
                    )

                    HorizontalDivider(modifier = Modifier.padding(horizontal = 12.dp))

                    DropdownMenuItem(
                        text = { Text("Game Settings") },
                        leadingIcon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.settings),
                                contentDescription = null
                            )
                        },
                        onClick = {}
                    )
                    DropdownMenuItem(
                        text = { Text("View Save States") },
                        leadingIcon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.outline_folder_open_24),
                                contentDescription = null
                            )
                        },
                        onClick = {}
                    )
                    DropdownMenuItem(
                        text = { Text("Manage Save Files") },
                        leadingIcon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.outline_folder_managed_24),
                                contentDescription = null
                            )
                        },
                        trailingIcon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.outline_keyboard_arrow_right_24),
                                contentDescription = "Next Menu"
                            )
                        },
                        onClick = { currentMenu = "saves" }
                    )

                    HorizontalDivider(modifier = Modifier.padding(horizontal = 12.dp))

                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "Delete ROM",
                                color = MaterialTheme.colorScheme.error,
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.outline_delete_24),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error
                            )
                        },
                        onClick = {}
                    )
                }

            }

            "saves" -> {
                DropdownMenuGroup(shapes = MenuDefaults.groupShape(0, 1)) {
                    DropdownMenuItem(
                        text = { Text("Back") },
                        leadingIcon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.outline_keyboard_arrow_left_24),
                                contentDescription = "Back"
                            )
                        },
                        onClick = { currentMenu = "main" }
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 12.dp))
                    DropdownMenuItem(
                        text = { Text("Import Saves") },
                        leadingIcon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.sharp_move_to_inbox_24),
                                contentDescription = null
                            )
                        },
                        onClick = {}
                    )
                    DropdownMenuItem(
                        text = { Text("Export Saves") },
                        leadingIcon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.outline_outbox_24),
                                contentDescription = null,
                            )
                        },
                        onClick = {}
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GameContextMenuPreview() {
    TriangleTheme {

        GameContextMenu(
            GameUiExample,
            expanded = true,
            onActions = {},
            state = EmulatorState(
                gameHashForContextMenu = GameUiExample.hash,
                selectedGame = GameUiExample
            ),
        )

    }
}
