package com.bzapata.triangle.intro.done

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bzapata.triangle.R
import com.bzapata.triangle.ui.theme.TriangleTheme

@Composable
fun Done() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.baseline_check_24),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(250.dp)
        )
        Text(
            text = "Done",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "You're all set. Its Gamer Time!",
            color = Color.White
        )
        Spacer(modifier= Modifier.size(32.dp))
        Button(
            onClick = {},
        ) {
            Text(
                text = "Continue",
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.size(width = 8.dp, height = 0.dp))
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.outline_arrow_forward_24),
                contentDescription = "Go to Emulator"
            )
        }
    }
}

@Preview
@Composable
fun DonePreview() {
    TriangleTheme {
        Done()
    }
}