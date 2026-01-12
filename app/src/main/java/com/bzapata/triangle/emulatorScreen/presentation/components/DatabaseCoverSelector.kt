package com.bzapata.triangle.emulatorScreen.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.request.ImageRequest
import com.bzapata.triangle.R
import com.bzapata.triangle.emulatorScreen.domain.Game
import com.bzapata.triangle.emulatorScreen.domain.GameUiExample
import com.bzapata.triangle.emulatorScreen.presentation.EmulatorActions
import com.bzapata.triangle.emulatorScreen.presentation.EmulatorState
import com.bzapata.triangle.ui.theme.TriangleTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DatabaseCoverSelector(
    game: Game,
    state: EmulatorState,
    onAction: (EmulatorActions) -> Unit
) {

    val scope = rememberCoroutineScope()
    val modalBottomSheetProperties = remember {
        ModalBottomSheetProperties(
            isAppearanceLightStatusBars = false,
            isAppearanceLightNavigationBars = false,
        )
    }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    if (state.isCoverDbSelectorOpen) {
        ModalBottomSheet(
            modifier = Modifier
            //.fillMaxHeight(.9f)
            // .padding(top = 50.dp)
            ,
            dragHandle = { },
            onDismissRequest = {
                onAction(EmulatorActions.ToggleDbCover)
                onAction(EmulatorActions.SelectGame(null))
            },
            sheetState = sheetState,
            containerColor = Color(0xff1c1c1e),
            sheetGesturesEnabled = false,
            properties = modalBottomSheetProperties

        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight(.94f)
                    .padding(horizontal = 8.dp)
            ) {
                stickyHeader {
                    Column(modifier = Modifier.background(Color(0xff1c1c1e))) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                        ) {
                            Text(
                                text = "Game Database",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier
                                    .align(Alignment.Center)
                            )

                            TextButton(
                                onClick = {
                                    scope.launch {
                                        sheetState.hide()
                                    }.invokeOnCompletion {
                                        if (!sheetState.isVisible)
                                            onAction(EmulatorActions.ToggleDbCover)
                                    }
                                },
                                modifier = Modifier.align(Alignment.CenterEnd),
                            ) {
                                Text(
                                    text = "Done",
                                    color = MaterialTheme.colorScheme.primary,
                                    textAlign = TextAlign.End,
                                    style = MaterialTheme.typography.titleMedium,
                                )
                            }
                        }
                        SearchField(
                            modifier = Modifier.padding(bottom = 4.dp),
                            initialText = game.name
                        ) {
                            onAction(EmulatorActions.QueryCovers(it))
                        }
                    }
                }
                if (state.coverQuery.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier.fillParentMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Games Database",
                                color = Color.Gray,
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                text = "To search the database, type the name of a game int the search bar.",
                                color = Color.Gray,
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 24.dp)
                            )
                        }
                    }
                } else {
                    items(items = state.queriedCovers.toList(), key = null) { (uri, title) ->
                        ListItem(
                            modifier = Modifier
                                .height(100.dp)
                                .clickable(onClick = {
                                    onAction(
                                        EmulatorActions.SaveCover(
                                            uri = uri,
                                            gameHash = game.hash
                                        )
                                    )
                                    scope.launch {
                                        sheetState.hide()
                                    }.invokeOnCompletion {
                                        if (!sheetState.isVisible)
                                            onAction(EmulatorActions.ToggleDbCover)
                                    }
                                }),
                            colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                            leadingContent = {
                                var imageLoading by remember { mutableStateOf(false) }
                                AsyncImage(
                                    onState = { imageState ->
                                        imageLoading = imageState is AsyncImagePainter.State.Loading
                                    },
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(uri) // Pass the Uri object here
                                        .fallback(R.drawable.deltaicon)
                                        .error(R.drawable.deltaicon)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = "Cover",
                                    contentScale = ContentScale.Fit,
                                )
                                if (imageLoading) {
                                    LoadingIndicator()
                                }
                            },
                            headlineContent = { Text(title ?: "") }
                        )
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun DataBaseCoverSelectorPreview() {
    TriangleTheme {
        DatabaseCoverSelector(
            state = EmulatorState(isCoverDbSelectorOpen = true),
            game = GameUiExample
        ) { }
    }
}