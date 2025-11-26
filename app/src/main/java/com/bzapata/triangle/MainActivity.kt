package com.bzapata.triangle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bzapata.triangle.ui.components.EmulatorAppBar
import com.bzapata.triangle.ui.components.EmulatorPages
import com.bzapata.triangle.ui.components.GameGrid
import com.bzapata.triangle.ui.components.PagerIndicator
import com.bzapata.triangle.ui.components.Settings
import com.bzapata.triangle.ui.theme.TriangleTheme


class MainActivity : ComponentActivity() {
    private val gameViewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            TriangleTheme {
                var blurBackground by remember { mutableStateOf(false) }
                val isSettingsOpen by gameViewModel.isSettingsOpen.collectAsStateWithLifecycle()
                val fileMenuOpen by gameViewModel.fileMenuOpen.collectAsStateWithLifecycle()
                val consoles = Consoles.entries.toTypedArray()
                val pagerState = rememberPagerState(
                    pageCount = {consoles.size}
                )
                val currentEmulatorName = consoles[pagerState.currentPage].name

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .blur(if (blurBackground) 8.dp else 0.dp),
                    topBar = {
                        Column {
                            EmulatorAppBar(
                                settingsToggle = { gameViewModel.toggleSettings() },
                                fileToggle = { gameViewModel.toggleFileMenu() },
                                isMenuOpen = fileMenuOpen,
                                currentEmulatorName = currentEmulatorName
                            )
                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        }
                    },
                    bottomBar = {
                        Column {
                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                            PagerIndicator(pagerState = pagerState)
                        }
                    }
                ) { innerPadding ->
                    EmulatorPages(
                        modifier = Modifier.padding(innerPadding),
                        pagerState = pagerState,
                        consoles = consoles,
                        blurBackgroundToggle = {blurBackground = !blurBackground}
                    )
//                    GameGrid(
//                        modifier = Modifier.padding(innerPadding),
//                        focusedGame = selectedGameIndex,
//                        onGameFocus = { gameViewModel.onGameFocus(it) },
//                        console = Consoles.DS
//                    )
                    Settings(dismissAction = { gameViewModel.toggleSettings() }, isOpen = isSettingsOpen)
                }
            }
        }
    }
}
