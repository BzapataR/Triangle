package com.bzapata.triangle.emulatorScreen.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bzapata.triangle.emulatorScreen.domain.Game
import com.bzapata.triangle.emulatorScreen.domain.GameUiExample
import com.bzapata.triangle.ui.theme.TriangleTheme

@Composable
fun RenameDialog(
    game: Game,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    // Local state to hold the text input
    var newName by remember { mutableStateOf(game.name) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xff1c1c1e), // Matching your app's dark theme
        title = {
            Text(
                text = "Rename ROM",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )
        },
        text = {
            Column {
                Text(
                    text = "Enter a new name for: ${game.name}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = newName,
                    onValueChange = { newName = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
                    label = { Text("ROM Name") }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (newName.isNotBlank()) {
                        onConfirm(newName)
                        onDismiss()
                    }
                }
            ) {
                Text("Rename", color = MaterialTheme.colorScheme.primary)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color.Gray)
            }
        }
    )
}

@Preview
@Composable
fun RenameDialogPreview() {
    TriangleTheme {
        RenameDialog(
            game = GameUiExample,
            onDismiss = {},
            onConfirm = {}
        )
    }
}