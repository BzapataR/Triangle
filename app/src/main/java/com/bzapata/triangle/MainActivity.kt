package com.bzapata.triangle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.bzapata.triangle.emulatorScreen.presentation.EmulatorHomePageRoot
import com.bzapata.triangle.ui.theme.TriangleTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            TriangleTheme {
                EmulatorHomePageRoot()
            }
        }
    }
}
