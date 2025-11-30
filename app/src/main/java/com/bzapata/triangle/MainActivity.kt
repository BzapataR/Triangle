package com.bzapata.triangle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bzapata.triangle.emulatorScreen.presentation.EmulatorHomePageRoot
import com.bzapata.triangle.emulatorScreen.presentation.EmulatorViewModel
import com.bzapata.triangle.ui.theme.TriangleTheme


class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            TriangleTheme {
//                var blurBackground by remember { mutableStateOf(false) }
                val isSettingsOpen by mainViewModel.isSettingsOpen.collectAsStateWithLifecycle()
                val fileMenuOpen by mainViewModel.fileMenuOpen.collectAsStateWithLifecycle()
//                val consoles = Consoles.entries.toTypedArray()
//                val pagerState = rememberPagerState(
//                    pageCount = {consoles.size}
//                )
//                val currentEmulatorName = consoles[pagerState.currentPage].name
                EmulatorHomePageRoot(EmulatorViewModel() )
            }
        }
    }
}
