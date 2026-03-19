package com.bzapata.triangle.settings.mainSettingsPage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bzapata.triangle.R
import com.bzapata.triangle.emulatorScreen.presentation.components.RoundedListItem
import com.bzapata.triangle.settings.SettingsPageTemplate
import com.bzapata.triangle.settings.SubText
import com.bzapata.triangle.ui.theme.TriangleTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(
    dismiss: () -> Unit,
    toControllerSettings: () -> Unit = {},
    toControllerSkins: () -> Unit = {},
    toPaths: () -> Unit = {}
) {

    val uriHandler = LocalUriHandler.current

    var sliderPositions by remember { mutableFloatStateOf(70f) }
    var isChecked by remember { mutableStateOf(true) }

    SettingsPageTemplate(goBack = { dismiss() }, title = "Settings", rootPage = true) {
        item {
            SubText("CONTROLLER")
            Card(
                modifier = Modifier
            ) {
                RoundedListItem(
                    leadingText = "Player 1",
                    trailingText = "Touch Screen",
                    onClick = { toControllerSettings() }
                )
                HorizontalDivider()
                RoundedListItem(
                    leadingText = "Player 2",
                    trailingText = "",
                    onClick = {}
                )
                HorizontalDivider()
                RoundedListItem(
                    leadingText = "Player 3",
                    trailingText = "",
                    onClick = {}
                )
                HorizontalDivider()
                RoundedListItem(
                    leadingText = "Player 4",
                    trailingText = "",
                    onClick = {}
                )
            }
            Spacer(modifier = Modifier.size(24.dp))
        }


        item {

            SubText("CONTROLLER SKINS")
            Card(
                modifier = Modifier
            ) {
                RoundedListItem(
                    leadingText = "Nintendo",
                    trailingText = "Touch Screen",
                    onClick = { toControllerSkins() }
                )
                HorizontalDivider()
                RoundedListItem(
                    leadingText = "Super Nintendo",
                    onClick = {}
                )
                HorizontalDivider()
                RoundedListItem(
                    leadingText = "Nintendo 64",
                    onClick = {}
                )
                HorizontalDivider()
                RoundedListItem(
                    leadingText = "GameBoy Color",
                    onClick = {}
                )
                HorizontalDivider()

                RoundedListItem(
                    leadingText = "GameBoy Advance",
                    onClick = {}
                )
                HorizontalDivider()
                RoundedListItem(
                    leadingText = "Nintendo DS",
                    onClick = {}
                )
            }
            SubText("Customize the appearance of each system. Learn more...")
            Spacer(modifier = Modifier.size(24.dp))
        }


        item {
            SubText("CONTROLLER OPACITY")
            Card {
                RoundedListItem(
                    leadingText = "${sliderPositions.toInt()}%",
                    customTrailingContent = {
                        Slider(
                            thumb = {
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .background(Color.White, CircleShape)
                                )
                            },
                            modifier = Modifier.fillMaxWidth(.90f),
                            steps = 99,
                            valueRange = 0f..100f,
                            value = sliderPositions,
                            onValueChange = { sliderPositions = it }
                        )
                    }
                )
            }
            Spacer(modifier = Modifier.size(24.dp))
        }

        item {
            SubText("HAPTIC FEEDBACK")
            Card {
                RoundedListItem(
                    leadingText = "Buttons",
                    customTrailingContent = {
                        Switch(
                            colors = SwitchDefaults.colors(checkedTrackColor = MaterialTheme.colorScheme.primary),
                            checked = isChecked,
                            onCheckedChange = {
                                isChecked = !isChecked
                            },
                        )
                    }
                )
                HorizontalDivider()
                RoundedListItem(
                    leadingText = "Control Sticks",
                    customTrailingContent = {
                        Switch(
                            colors = SwitchDefaults.colors(checkedTrackColor = MaterialTheme.colorScheme.primary),
                            checked = isChecked,
                            onCheckedChange = {
                                isChecked = !isChecked
                            },
                        )
                    }
                )
            }
            SubText("When enabled, your device will vibrate in response to touch screen controls")
            Spacer(modifier = Modifier.size(24.dp))
        }

        item {
            SubText(text = "File Paths")
            Card {
                RoundedListItem(
                    leadingText = "Saved Paths",
                    onClick = { toPaths() }
                )
            }
            Spacer(modifier = Modifier.size(24.dp))
        }

        item {
            SubText(text = "TRIANGLE SYNC")
            Card(
                modifier = Modifier
            ) {
                RoundedListItem(
                    leadingText = "Services",
                    onClick = {}
                )
            }
            SubText("Sync your games, save data, save states, and cheats between devices")
            Spacer(modifier = Modifier.size(24.dp))
        }


        item {
            SubText("GESTURES")
            Card {
                RoundedListItem(
                    leadingText = "Menu Button Gestures",
                    customTrailingContent = {
                        Switch(
                            colors = SwitchDefaults.colors(checkedTrackColor = MaterialTheme.colorScheme.primary),
                            checked = isChecked,
                            onCheckedChange = {
                                isChecked = !isChecked
                            },
                        )
                    }
                )
            }
            SubText("Use gestures while holding the menu botton to perform quick actions")
            Spacer(modifier = Modifier.size(24.dp))
            SubText("Menu + Horizontal Swipe: Fast Forward")
            SubText("Menu + Controller Input: Hold Input")
            SubText("Menu + Double Tap: Quick Save")
            SubText("Menu + Long Press: Quick Load")
            Spacer(modifier = Modifier.size(24.dp))
        }


        item {
            SubText("Google Cast")
            Card {
                RoundedListItem(
                    leadingText = "Display Full Screen",
                    customTrailingContent = {
                        Switch(
                            colors = SwitchDefaults.colors(checkedTrackColor = MaterialTheme.colorScheme.primary),
                            checked = isChecked,
                            onCheckedChange = {
                                isChecked = !isChecked
                            },
                        )
                    }
                )
                HorizontalDivider()
                ListItem(
                    colors = ListItemDefaults.colors(containerColor = Color(0xff2c2c2e)),
                    headlineContent = { Text("Top Screen Only") },
                    supportingContent = { Text("Nintendo DS") },
                    trailingContent = {
                        Switch(
                            colors = SwitchDefaults.colors(checkedTrackColor = MaterialTheme.colorScheme.primary),
                            checked = isChecked,
                            onCheckedChange = {
                                isChecked = !isChecked
                            },
                        )
                    }
                )
            }
            SubText("When Casting DS games, only the top screen will appear on the external display")
            Spacer(modifier = Modifier.size(24.dp))
        }


        item {
            SubText("Context Menus")
            Card {
                RoundedListItem(
                    leadingText = "Home Screen Shortcuts",
                    onClick = {}
                )
                HorizontalDivider()
                RoundedListItem(
                    leadingText = "Context Menu Previews",
                    customTrailingContent = {
                        Switch(
                            colors = SwitchDefaults.colors(checkedTrackColor = MaterialTheme.colorScheme.primary),
                            checked = isChecked,
                            onCheckedChange = {
                                isChecked = !isChecked
                            },
                        )
                    }
                )
            }
            SubText("Preview games and save states when using context menus")
            Spacer(Modifier.size(24.dp))
        }


        item {
            SubText("CORE SETTINGS")
            Card {
                RoundedListItem(
                    leadingText = "Nintendo DS",
                    trailingText = "melonDS",
                    onClick = {}
                )
            }
            SubText("Manage Settings for individual emulation cores")
            Spacer(Modifier.size(24.dp))
        }


        item {
            SubText("ADVANCED")
            Card {
                ListItem(
                    modifier = Modifier.clickable {

                    },
                    colors = ListItemDefaults.colors(containerColor = Color(0xff2c2c2e)),
                    headlineContent = {
                        Text(
                            text = "Export Error Logs",
                            color = MaterialTheme.colorScheme.primary,
                        )
                    },
                    trailingContent = {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.outline_keyboard_arrow_right_24),
                            contentDescription = null
                        )
                    }
                )
            }
            Spacer(Modifier.size(24.dp))
        }

        item {
            SubText("Boring Stuff")
            Card {
                RoundedListItem(
                    leadingText = "Licenses",
                    onClick = {}
                )
                HorizontalDivider()
                RoundedListItem(
                    leadingText = "Privacy Policy",
                    onClick = {}
                )
            }
            Spacer(Modifier.size(24.dp))
        }


        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Sources",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = Color.White
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconButton(
                        onClick = { uriHandler.openUri("https://github.com/rileytestut/Delta") },
                        modifier = Modifier.size(44.dp)
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.deltaicon),
                            contentDescription = "THE OG",
                            modifier = Modifier.size(36.dp),
                            tint = Color.White
                        )
                    }
                    IconButton(
                        onClick = { uriHandler.openUri("https://github.com/BzapataR/Triangle") }
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.github_mark_white),
                            contentDescription = "Source Code",
                            tint = Color.White
                        )
                    }
                }
                Spacer(Modifier.size(24.dp))
                SubText("Triangle 0.1.0-alpha")
            }
        }
    }
}

@Preview
@Composable
fun SettingsPreview() {
    TriangleTheme {
        Settings({})
    }
}