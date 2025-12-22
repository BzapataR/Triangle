package com.bzapata.triangle.emulatorScreen.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun ErrorDialog(
    errorMessage: String,
    onDismiss: () -> Unit
) { // todo actually add error implementation
    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(
                onClick = { onDismiss() },
            ) {
                Text(text = "Ok")
            }
        },
//        dismissButton = {
//            TextButton(
//                onClick = {  }
//            ) {
//                Text(text = "Cancel")
//            }
//        },
        text = {
            Column {
                Text(text = errorMessage)
            }
        },
        title = { Text(text = "Error") },
    )
}