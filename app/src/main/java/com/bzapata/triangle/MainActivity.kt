package com.bzapata.triangle

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bzapata.triangle.data.controller.ControllerManager
import com.bzapata.triangle.data.repository.ConfigRepository
import com.bzapata.triangle.emulatorScreen.domain.Consoles
import com.bzapata.triangle.emulatorScreen.presentation.EmulatorActions
import com.bzapata.triangle.emulatorScreen.presentation.EmulatorHomePageRoot
import com.bzapata.triangle.emulatorScreen.presentation.EmulatorViewModel
import com.bzapata.triangle.intro.IntroNavigatorRoot
import com.bzapata.triangle.ui.theme.TriangleTheme
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.io.File

class MainActivity : ComponentActivity() {
    private val config: ConfigRepository by inject()
    private val controllerManagerClass : ControllerManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT)
        )
        window.isNavigationBarContrastEnforced = false

        if (intent?.action == Intent.ACTION_VIEW || intent?.action == Intent.ACTION_SEND) {
            handleExternalRomLaunch(intent)
        }

        setContent {
            TriangleTheme {
                val isFirstLaunch by config.isFirstLaunchFlow.collectAsStateWithLifecycle(null)

                // Keep splash screen visible while loading the first launch state
                splashScreen.setKeepOnScreenCondition { isFirstLaunch == null }

                when (isFirstLaunch) {
                    true -> IntroNavigatorRoot()
                    false -> EmulatorHomePageRoot()
                    null -> {
                        //splash screen
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        if (intent.action == Intent.ACTION_VIEW
            || intent.action == Intent.ACTION_SEND
        ) {
            handleExternalRomLaunch(intent)
        }
    }
    override fun onKeyDown(keyCode : Int, event : KeyEvent) : Boolean {
        val vm = getViewModel<EmulatorViewModel>()
        vm.updateRecentController(controllerManagerClass.inputDeviceDetection(event))
        val action = controllerManagerClass.buttonActionMapping(event)
        if (action != null) {
            vm.onAction(action)
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun dispatchGenericMotionEvent(event: MotionEvent): Boolean {
        val vm = getViewModel<EmulatorViewModel>()
        val action = controllerManagerClass.motionActionMapping(event)
        if (action != null) {
            vm.onAction(action)
            return true
        }
        //vm.updateRecentController(controllerManagerClass.inputDeviceDetection(ev))
        return super.dispatchGenericMotionEvent(event)
    }
    private fun handleExternalRomLaunch(intent: Intent) {
        val uri: Uri? =
            if (intent.action == Intent.ACTION_SEND) intent.getParcelableExtra(Intent.EXTRA_STREAM) else intent.data
        val viewModel: EmulatorViewModel = getViewModel()
        if (uri != null) {
            val fileName = DocumentFile.fromSingleUri(this, uri)?.name ?: return
            val extension = File(fileName).extension
            val console = Consoles.fromExtension(extension)
            Log.i("FileAssociation", " extension: $extension, console: $console")
            if (console != null) {
                Log.i("FileAssociation", "Opening ROM: $uri")
                viewModel.onAction(EmulatorActions.LaunchExternalRom(uri))
            } else {
                val toast = Toast.makeText(this, "Incorrect file extension", Toast.LENGTH_SHORT)
                toast.show()
                Log.e("FileAssociation", "File with incorrect file extension: $console")
                this.finishAndRemoveTask()
            }
        }
    }
}
