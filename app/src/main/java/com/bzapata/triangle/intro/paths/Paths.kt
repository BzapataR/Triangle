package com.bzapata.triangle.intro.paths

import android.net.Uri
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bzapata.triangle.R
import com.bzapata.triangle.fileOperations.directoryPicker
import com.bzapata.triangle.ui.theme.TriangleTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun PathRoot(
    viewModel: PathsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Path(
        state = state,
        onActions = viewModel::onAction
    )
}

@Composable
fun Path(
    state: PathsState,
    onActions: (PathsActions) -> Unit
) {
    val selectTrianglePathLauncher = directoryPicker(state.trianglePath) { uri ->
        onActions(PathsActions.SetTrianglePath(uri))
    }

    val selectRomsPathLauncher = directoryPicker(state.romPath) { uri ->
        onActions(PathsActions.SetRomsPath(uri))
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.outline_folder_24),
            contentDescription = null,
            modifier = Modifier.size(250.dp),
            tint = Color.White
        )
        Text(
            text = "Data Folder",
            color = Color.White,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Select data folders",
            textAlign = TextAlign.Center,
            color = Color.White
        )
        Text(
            text = "(User folder is required)",
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
                onClick = { selectTrianglePathLauncher() },
                modifier = Modifier.fillMaxWidth(),
                enabled = state.trianglePath == null
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.outline_home_24),
                    contentDescription = "Select User Folder"
                )
                Text(
                    text = "Select User Folder",
                    modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
                    textAlign = TextAlign.Center
                )
            }
            Button(
                onClick = { selectRomsPathLauncher() },
                modifier = Modifier.fillMaxWidth(),
                enabled = state.romPath == null
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.videogame_asset_24dp),
                    contentDescription = "Select Applications folder"
                )
                Text(
                    text = "Applications",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview
@Composable
fun PathsPreview() {
    TriangleTheme {
        Path(state = PathsState(), onActions = {})
    }
}
