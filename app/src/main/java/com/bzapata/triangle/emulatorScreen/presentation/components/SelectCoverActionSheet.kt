package com.bzapata.triangle.emulatorScreen.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bzapata.triangle.emulatorScreen.presentation.EmulatorActions
import com.bzapata.triangle.emulatorScreen.presentation.EmulatorState
import com.bzapata.triangle.ui.theme.TriangleTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectCoverActionSheet(state : EmulatorState, onAction : (EmulatorActions) -> Unit) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    if (state.isCoverActionSheetOpen) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { onAction(EmulatorActions.ToggleCoverActionSheet) },
            containerColor = Color.Transparent,
            dragHandle = null,
            scrimColor = Color.Black.copy(alpha = 0.4f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .navigationBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Main Actions Group
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFF1C1C1E),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        ActionItem(text = "Clipboard") { /* TODO */ }
                        HorizontalDivider(color = Color.Gray.copy(alpha = 0.2f), thickness = 0.5.dp)
                        ActionItem(text = "Photo Library") { /* TODO */ }
                        HorizontalDivider(color = Color.Gray.copy(alpha = 0.2f), thickness = 0.5.dp)
                        ActionItem(text = "Local Database") {
                            scope.launch {
                                sheetState.hide()
                            }.invokeOnCompletion {
                                if (!sheetState.isVisible)
                                    onAction(EmulatorActions.ToggleCoverActionSheet)
                                    onAction(EmulatorActions.ToggleDbCover)
                            }
                        }
                        HorizontalDivider(color = Color.Gray.copy(alpha = 0.2f), thickness = 0.5.dp)
                        ActionItem(text = "Files") { /* TODO */ }
                    }
                }

                // Cancel Button Group
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFF1C1C1E),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    ActionItem(text = "Cancel") {
                        scope.launch {
                            sheetState.hide()
                        }.invokeOnCompletion {
                            if (!sheetState.isVisible)
                                onAction(EmulatorActions.ToggleCoverActionSheet)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ActionItem(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun SelectCoverActionSheetPreview() {
    TriangleTheme {
        SelectCoverActionSheet(EmulatorState(), onAction = {})
    }
}
