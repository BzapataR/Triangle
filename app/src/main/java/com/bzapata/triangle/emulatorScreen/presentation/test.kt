//package com.bzapata.triangle.emulatorScreen.presentation
//
//import android.util.Log
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.lazy.grid.GridCells
//import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
//import androidx.compose.foundation.lazy.grid.items
//import androidx.compose.foundation.lazy.grid.itemsIndexed
//import androidx.compose.foundation.pager.HorizontalPager
//import androidx.compose.foundation.pager.rememberPagerState
//import androidx.compose.material3.Scaffold
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.snapshotFlow
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import com.bzapata.triangle.emulatorScreen.data.fileOperations.directoryPicker
//import com.bzapata.triangle.emulatorScreen.presentation.components.EmulatorAppBar
//import com.bzapata.triangle.emulatorScreen.presentation.components.PagerIndicator
//import com.bzapata.triangle.emulatorScreen.presentation.emulators.components.GameCover
//import com.bzapata.triangle.emulatorScreen.presentation.emulators.components.GameGrid
//import kotlinx.coroutines.flow.distinctUntilChanged
//
//@Composable
//fun test() {
//    val pagerState = rememberPagerState(
//        initialPage = 0,
//        pageCount = { 1 }
//    )
//
//
//
//    Scaffold(
//        modifier = Modifier
//            .fillMaxSize()
//            ,//.blur(if (state.isBackgroundBlurred) 8.dp else 0.dp),
//        topBar = {
//
//
//
//        },
//        bottomBar = {
//        }
//    ) { innerPadding ->
//            HorizontalPager(
//                modifier = Modifier.padding(innerPadding),
//                state = pagerState,
//            ) { page ->
//                LazyVerticalGrid(
//                    columns = GridCells.Adaptive(90.dp),
//                    modifier = Modifier.fillMaxSize(),
//                    contentPadding = PaddingValues(24.dp),
//                    verticalArrangement = Arrangement.spacedBy(16.dp),
//                    horizontalArrangement = Arrangement.spacedBy(16.dp),
//                )
//                {
//                    itemsIndexed() {game -> //todo fix one rom glitch
//                        GameCover(
//                            game = game,
//                            isContextMenuShown = state.gameHashForContextMenu == game.hash,
//                            onShowContextMenu = {
//                                onGameFocus(game.hash)
//                                Log.i("Page", "state hash: ${state.gameHashForContextMenu} and hash ${game.hash}")
//                            },
//                            onDismissContextMenu = {
//                                Log.i("Page", "dismiss context menu")
//                                onGameFocus(null)
//                            }
//                        )
//                    }
//            }
//        }
//    }
//}