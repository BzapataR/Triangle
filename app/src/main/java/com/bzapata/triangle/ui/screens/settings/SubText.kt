package com.bzapata.triangle.ui.screens.settings

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SubText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = Color.Gray,
        modifier = Modifier.padding(2.dp)
    )
}