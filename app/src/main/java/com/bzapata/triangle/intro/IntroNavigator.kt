package com.bzapata.triangle.intro

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.bzapata.triangle.intro.done.Done
import com.bzapata.triangle.intro.noTrianglePathDialog.NoTrianglePathDialog
import com.bzapata.triangle.intro.paths.PathRoot
import com.bzapata.triangle.intro.permissions.Permissions
import com.bzapata.triangle.intro.skipPermissionDialog.SkipPermissionDialog
import com.bzapata.triangle.intro.welcome.Welcome
import org.koin.androidx.compose.koinViewModel

@Composable
fun IntroNavigatorRoot(
    viewModel: IntroViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    IntroNavigator(
        state,
        onAction = viewModel::onAction
    )
}
@Composable
fun IntroNavigator(
    state : IntroState,
    onAction : (IntroActions) -> Unit
) {
    val introNavigator = rememberNavController()
    Box(Modifier
        .background(MaterialTheme.colorScheme.background)
        .fillMaxSize()){
        NavHost(
            navController = introNavigator,
            startDestination = IntroNavigation.IntroNavigationGraph
        ) {
            navigation<IntroNavigation.IntroNavigationGraph>(
                startDestination = IntroNavigation.Welcome
            ) {
                composable<IntroNavigation.Welcome> {
                    onAction(IntroActions.ChangePage(0))
                    Welcome(
                        next = {
                            introNavigator.navigate(IntroNavigation.Permissions)
                        }
                    )
                }
                composable<IntroNavigation.Permissions> {
                    onAction(IntroActions.ChangePage(1))
                    Permissions(state= state, onAction = onAction)
                }
                composable<IntroNavigation.Paths> {
                    onAction(IntroActions.ChangePage(2))
                    PathRoot()
                }
                composable<IntroNavigation.Done> {
                    onAction(IntroActions.ChangePage(3))
                    Done(toEmulator = {onAction(IntroActions.Finish)})
                }
                dialog<IntroNavigation.SkipPermissionDialog>{
                    SkipPermissionDialog(
                        onDismiss = {
                            introNavigator.popBackStack()
                        },
                        skip = {
                            introNavigator.navigate(
                            IntroNavigation.Paths
                            )
                        }
                    )
                }
                dialog<IntroNavigation.NoUserFolder> {
                    NoTrianglePathDialog {
                        introNavigator.popBackStack()
                    }
                }
            }
        }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(16.dp)
            ) {
                if (state.page > 0) {
                    TextButton(
                        onClick = {
                            introNavigator.popBackStack()
                            onAction(IntroActions.GoBack)
                        }
                    ) {
                        Text(
                            text = "Back",
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    if (state.page < 3) {
                        TextButton(
                            onClick = {
                                Log.i("Permission", "new composable state ${state}" )

                                when (state.page) {
                                        0 -> introNavigator.navigate(IntroNavigation.Permissions)
                                        1 -> {
                                            Log.i("permissions", "camera: ${state.hasCameraPermission} mic:${state.hasMicPermission} camera: ${state.hasCameraPermission} boolean: ${state.showSkipPermissionDialog}")

                                            if (state.showSkipPermissionDialog) {
                                                introNavigator.navigate(IntroNavigation.Paths)
                                            } else {
                                                introNavigator.navigate(IntroNavigation.SkipPermissionDialog)
                                            }

                                        }
                                        2 -> {
                                            if (state.triangleFolderEmpty)
                                                introNavigator.navigate(IntroNavigation.NoUserFolder)
                                            else
                                                introNavigator.navigate(IntroNavigation.Done)
                                        }
                                    }
                            },
                            modifier = Modifier.padding(start = 16.dp)
                        ) {
                            Text(
                                text = "Next",
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
        }
    }
}
