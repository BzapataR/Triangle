package com.bzapata.triangle

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bzapata.triangle.data.repository.ConfigRepository
import com.bzapata.triangle.emulatorScreen.presentation.EmulatorHomePageRoot
import com.bzapata.triangle.intro.IntroNavigatorRoot
import com.bzapata.triangle.ui.theme.TriangleTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val config: ConfigRepository by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT)
        )
        window.isNavigationBarContrastEnforced = false
        setContent {
            TriangleTheme {
                val isFirstLaunch = config.isFirstLaunchFlow.collectAsStateWithLifecycle(null)
                splashScreen.setOnExitAnimationListener { isFirstLaunch.value == null }
                when (isFirstLaunch.value) {
                    true -> IntroNavigatorRoot()
                    false -> {
                        EmulatorHomePageRoot()
                    }
                    else -> {}
                }
//
//                if (isFirstLaunch.value == true)
//                    IntroNavigatorRoot()
//                else
//                    EmulatorHomePageRoot()
            }
        }
    }
}
