package com.bzapata.triangle.settings.Paths

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bzapata.triangle.R
import com.bzapata.triangle.emulatorScreen.presentation.components.RoundedListItem
import com.bzapata.triangle.settings.SettingsPageTemplate
import com.bzapata.triangle.settings.SubText
import com.bzapata.triangle.ui.theme.TriangleTheme
import com.bzapata.triangle.util.fileLaunchers.directoryPicker
import com.bzapata.triangle.util.fileLaunchers.openPath
import org.koin.androidx.compose.koinViewModel

@Composable
fun PathsRoot(
    goBack: () -> Unit = {},
    viewModel: PathsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Paths(
        state = state,
        onAction = viewModel::onAction,
        goBack = goBack
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun Paths(
    state: PathsState,
    onAction: (PathsActions) -> Unit,
    goBack: () -> Unit
) { // fix blur effect somehow
    val context = LocalContext.current

    val romDirectoryPicker = directoryPicker { uri ->
        if (uri != null) {
            onAction(PathsActions.SetRomsPath(uri))
        }
    }

    val triangleDirectoryPicker = directoryPicker { uri ->
        if (uri != null) {
            onAction(PathsActions.SetTrianglePath(uri))
        }
    }

    SettingsPageTemplate(
        goBack = { goBack() },
        modifier = Modifier.fillMaxSize().then(if (state.menuIndex != -1) Modifier.blur(8.dp) else Modifier),
        title = "Paths"
    ) {
        item {
            SubText("ROM Paths")
            Card {
                Column {
                    state.romPaths.forEachIndexed { index, uri ->
                        RoundedListItem(
                            onClick = {
                                onAction(PathsActions.OpenContextMenu(index))
                                Log.i("uri", "$uri")
                            },
                            leadingText = uri.path ?: "Unknown Path",
                            customTrailingContent = {
                                Box {
                                    val openDir = openPath(uri)
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.outline_keyboard_arrow_right_24),
                                        tint = Color.Gray,
                                        contentDescription = null
                                    )
                                    PathsContextMenu(
                                        expanded = state.menuIndex == index,
                                        expansionToggle = {
                                            onAction(
                                                PathsActions.OpenContextMenu(-1)
                                            )
                                        },
                                        removePath = {
                                            onAction(
                                                PathsActions.RemovePath(uri, context)
                                            )
                                        },
                                        openPath = openDir
                                    )
                                }
                            }
                        )
                        if (index < (state.romPaths.size - 1)) {
                            HorizontalDivider()
                        }
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                TextButton(
                    onClick = { romDirectoryPicker() },
                )
                {
                    Text(
                        text = "Add ROM path",
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.outline_add_circle_24),
                        contentDescription = "Add ROM Path",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }


        item {
            SubText("Triangle Path")
            Card {
                RoundedListItem(
                    leadingText = state.trianglePath?.path ?: "Unknown Path",
                    onClick = openPath(state.trianglePath ?: Uri.EMPTY)
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    thickness = 0.5.dp,
                    color = Color.Gray.copy(alpha = 0.2f)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                TextButton(
                    onClick = { triangleDirectoryPicker() },
                )
                {
                    Text(
                        text = "Change Triangle path",
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.outline_add_circle_24),
                        contentDescription = "Change Triangle Path",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

    }
}


@Preview
@Composable
private fun PathsPreview() {
    TriangleTheme {
        Paths(
            state = PathsState(
                trianglePath = "/tree/primary:Triangle".toUri(),
                romPaths = List<Uri>(size = 1, { "/tree/primary:Roms".toUri() })
            ),
            onAction = {},
            goBack = {}
        )
    }
}
