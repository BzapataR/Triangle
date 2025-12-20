package com.bzapata.triangle.emulatorScreen.presentation.components

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bzapata.triangle.R
import com.bzapata.triangle.emulatorScreen.presentation.EmulatorActions
import com.bzapata.triangle.emulatorScreen.presentation.EmulatorState
import com.bzapata.triangle.ui.theme.TriangleTheme
import kotlin.collections.mutableListOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatabaseCoverSelector(
    state : EmulatorState,
    onAction : (EmulatorActions) -> Unit
) {
    val listOfUri = remember { mutableListOf<Uri>() }
    var isExpanded by remember { mutableStateOf(false) }

    val textFieldState = rememberTextFieldState()
    val searchBarState = rememberSearchBarState()

    val scope = rememberCoroutineScope()
    val modalBottomSheetProperties = remember {
        ModalBottomSheetProperties(
            isAppearanceLightStatusBars = false,
            isAppearanceLightNavigationBars = false,
        )
    }
    val listState = rememberLazyListState()

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    val headerAlpha by remember {
        derivedStateOf {
            if (listState.firstVisibleItemIndex > 0) 1f
            else (listState.firstVisibleItemScrollOffset / 120f).coerceIn(0f, 1f)
        }
    }

    if (state.isCoverDbSelectorOpen) {
        ModalBottomSheet(
            modifier = Modifier
            //.fillMaxHeight(.9f)
            // .padding(top = 50.dp)
            ,
            dragHandle = { },
            onDismissRequest = { onAction(EmulatorActions.ToggleDbCover) },
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
                                    .graphicsLayer {
                                        alpha = headerAlpha
                                        translationY = (1f - headerAlpha) * -12f
                                    }
                                    .align(Alignment.Center)
                            )

                            TextButton(
                                onClick = {
                                    onAction(EmulatorActions.ToggleDbCover)
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
//                        SearchBar(
//                            modifier = Modifier.align(Alignment.CenterHorizontally)
//                                .semantics { traversalIndex = 0f },
//                            state = searchBarState,
//                            inputField = {
//                                SearchBarDefaults.InputField(
//                                    query = textFieldState.text.toString(),
//                                    onQueryChange = {
//                                        textFieldState.edit {
//                                            replace(
//                                                0,
//                                                length,
//                                                it
//                                            )
//                                        }
//                                    },
//                                    onSearch = {
//                                        onAction(EmulatorActions.QueryCovers(textFieldState.text.toString()))
//                                        isExpanded = false
//                                    },
//                                    expanded = isExpanded,
//                                    onExpandedChange = { isExpanded = it },
//                                    placeholder = { Text("Search") }
//                                )
//                            }
//                        )
//                    }
                        GameSearch() {
                            onAction(EmulatorActions.QueryCovers(it))
                        }
                    }
                }
                items(items = state.queriedCovers.toList(), key = null) {(uri, title)  ->
                    ListItem(
                        modifier = Modifier
                            .height(100.dp)
                            .clickable(onClick = {}),
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                        leadingContent = {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(uri) // Pass the Uri object here
                                    .fallback(R.drawable.deltaicon)
                                    .error(R.drawable.deltaicon)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "Cover",
                                contentScale = ContentScale.Fit,
                            )
                        },
                        headlineContent = {Text(title ?: "")}
                    )
                    HorizontalDivider()
                }
//                items(20){
//                    ListItem(
//                        modifier = Modifier
//                            .height(100.dp)
//                            .clickable(onClick = {}),
//                        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
//                        leadingContent = {
//                            AsyncImage(
//                                model = ImageRequest.Builder(LocalContext.current)
//                                //.data(game.localCoverUri) // Pass the Uri object here
//                                .fallback(R.drawable.deltaicon)
//                                .error(R.drawable.deltaicon)
//                                .crossfade(true)
//                                .build(),
//                                contentDescription = "Cover",
//                                contentScale = ContentScale.Fit,
//                            )
//                                          },
//                        headlineContent = {Text("Text")}
//                    )
//                    HorizontalDivider()
//                }
            }
        }
    }
}

@Preview
@Composable
private fun DataBaseCoverSelectorPreview() {
    TriangleTheme {
        DatabaseCoverSelector(state = EmulatorState(isCoverDbSelectorOpen = true)) { }
    }
}