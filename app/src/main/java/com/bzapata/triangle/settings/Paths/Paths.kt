package com.bzapata.triangle.settings.Paths

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bzapata.triangle.R
import com.bzapata.triangle.emulatorScreen.presentation.components.RoundedListItem
import com.bzapata.triangle.settings.SubText
import com.bzapata.triangle.ui.theme.TriangleTheme
import com.bzapata.triangle.util.fileLaunchers.directoryPicker
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
) {
    val listState = rememberLazyListState()

    val headerAlpha by remember {
        derivedStateOf {
            if (listState.firstVisibleItemIndex > 0) 1f
            else (listState.firstVisibleItemScrollOffset / 120f).coerceIn(0f, 1f)
        }
    }

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

    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    )
    {
        stickyHeader {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xff1c1c1e))
                    .padding(vertical = 8.dp),
            ) {
                Text(
                    text = stringResource(R.string.paths),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .graphicsLayer {
                            alpha = headerAlpha
                            translationY = (1f - headerAlpha) * -12f
                        }
                        .align(Alignment.Center)
                )

                TextButton(
                    onClick = { goBack() },
                    modifier = Modifier.align(Alignment.CenterStart),
                    contentPadding = PaddingValues(horizontal = 0.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.outline_keyboard_arrow_left_24),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = stringResource(R.string.settings),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }
        }
        item {
            Text(
                text = stringResource(R.string.paths),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.size(24.dp))
        }

        item {
                SubText(stringResource(R.string.rom_paths))
                Card {
                    Column {
                        state.romPaths.forEachIndexed { index, uri ->
                            RoundedListItem(
                                leadingText = uri.path ?: "Unknown Path",
                            )
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
                SubText(stringResource(R.string.triangle_path))
                Card {
                    Column {
                        val trianglePath = state.trianglePath
                        if (trianglePath != null) {
                            RoundedListItem(
                                leadingText = trianglePath.path ?: "Unknown Path",
                                onClick = { /* TODO: Context menu */ }
                            )
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                thickness = 0.5.dp,
                                color = Color.Gray.copy(alpha = 0.2f)
                            )
                        }
                    }
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
            state = PathsState(trianglePath = "/tree/primary:Triangle".toUri(), romPaths = List<Uri>(size = 1, {"/tree/primary:Roms".toUri()}) ),
            onAction = {},
            goBack = {}
        )
    }
}
