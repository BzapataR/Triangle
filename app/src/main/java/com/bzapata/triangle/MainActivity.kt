package com.bzapata.triangle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bzapata.triangle.data.repository.ConfigRepository
import com.bzapata.triangle.emulatorScreen.presentation.EmulatorHomePageRoot
import com.bzapata.triangle.intro.IntroNavigator
import com.bzapata.triangle.intro.IntroNavigatorRoot
import com.bzapata.triangle.ui.theme.TriangleTheme
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val config : ConfigRepository by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.dark(
                scrim = 0
            )

        )
        setContent {
            TriangleTheme {
                val isFirstLaunch = config.isFirstLaunchFlow.collectAsStateWithLifecycle(null)
                splashScreen.setOnExitAnimationListener { isFirstLaunch.value == null }
                if (isFirstLaunch.value== true)
                    IntroNavigatorRoot()
                else
                    EmulatorHomePageRoot()
            }
        }
    }
}
