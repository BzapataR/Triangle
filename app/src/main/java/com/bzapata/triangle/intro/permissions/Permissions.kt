package com.bzapata.triangle.intro.permissions

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bzapata.triangle.R
import com.bzapata.triangle.intro.IntroActions
import com.bzapata.triangle.intro.IntroState
import com.bzapata.triangle.intro.IntroViewModel
import com.bzapata.triangle.ui.theme.TriangleTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun PermissionRoot(
    viewModel: IntroViewModel = koinViewModel(),
    skip : () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Permissions(
        state = state,
        onAction = viewModel::onAction
    )
    if(!(state.hasNotificationPermission && state.hasMicPermission && state.hasCameraPermission)) {
        skip()
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Permissions(
    state: IntroState,
    onAction: (IntroActions) -> Unit
) {
    val snackBarHost = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val notificationPermissionState =
        rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
    val micPermissionsState =
        rememberPermissionState(permission = Manifest.permission.RECORD_AUDIO)
    val cameraPermissionState =
        rememberPermissionState(permission = Manifest.permission.CAMERA)


    suspend fun snackBar() {
        val result = snackBarHost.showSnackbar(
            message = "Permission denied.",
            actionLabel = "Settings",
            duration = SnackbarDuration.Short
        )
        if (result == SnackbarResult.ActionPerformed) {
            val intent = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", context.packageName, null)
            ).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (!granted) {
            scope.launch {
                snackBar()
            }
        }
    }


    LaunchedEffect(notificationPermissionState.status) {
        onAction(
            IntroActions.PermissionStatusChange(
                notificationPermissionState.permission,
                notificationPermissionState.status.isGranted
            )
        )
    }
    LaunchedEffect(micPermissionsState.status) {
        onAction(
            IntroActions.PermissionStatusChange(
                micPermissionsState.permission,
                micPermissionsState.status.isGranted
            )
        )
    }
    LaunchedEffect(cameraPermissionState.status) {
        onAction(
            IntroActions.PermissionStatusChange(
                cameraPermissionState.permission,
                cameraPermissionState.status.isGranted
            )
        )
    }


    Scaffold(snackbarHost = { SnackbarHost(snackBarHost) }) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.outline_shield_24),
                contentDescription = null,
                modifier = Modifier.size(250.dp),
                tint = Color.White
            )
            Text(
                text = "Permissions",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Grant optional permissions to use specific features of some emulators",
                textAlign = TextAlign.Center,
                color = Color.White
            )

            Spacer(modifier = Modifier.size(32.dp))

            Column(
                modifier = Modifier.width(IntrinsicSize.Max),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Button(
                    onClick = {
                        permissionLauncher.launch(notificationPermissionState.permission)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.hasNotificationPermission
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.notifications_24dp),
                        contentDescription = "Grant Notification Permission"
                    )
                    Text(
                        text = if (state.hasNotificationPermission) "Notifications Granted" else "Notifications",
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                        textAlign = TextAlign.Center
                    )
                }

                Button(
                    onClick = { permissionLauncher.launch(micPermissionsState.permission) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.hasMicPermission
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.mic_24dp),
                        contentDescription = "Grant Microphone Permission"
                    )
                    Text(
                        text = "Microphone",
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                }

                Button(
                    onClick = { permissionLauncher.launch(cameraPermissionState.permission) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.hasCameraPermission
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.photo_camera_24dp),
                        contentDescription = "Grant Camera Permission"
                    )
                    Text(
                        text = "Camera",
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}



@Preview
@Composable
fun PermissionPreview() {
    TriangleTheme {
        Permissions(state = IntroState()) {}
    }
}
