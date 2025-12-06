package com.bzapata.triangle.intro.welcome

import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bzapata.triangle.R
import com.bzapata.triangle.ui.theme.TriangleTheme

@Composable
fun Welcome(next: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.trianglelogo),
            contentDescription = "Ripoff",
            tint = Color.Unspecified,
            modifier = Modifier.size(250.dp)
        )
        Spacer(modifier = Modifier.size(50.dp))
        Text(
            "Welcome!",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
        )
        Text(
            text = "Triangle is an emulator based on the IOS app Delta",
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.size(50.dp))
        Button(
            onClick = { next() }
        ) {
            Text("Get Started")
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.outline_keyboard_arrow_right_24),
                contentDescription = "Next Page"
            )
        }
    }
}

@Preview
@Composable
fun WelcomePreview() {
    TriangleTheme {
        Welcome({})
    }
}
