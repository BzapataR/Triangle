package com.bzapata.triangle.emulatorScreen.presentation.components

import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.createBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

@Composable
fun PdfImage(
    assetFileName: String,
    modifier: Modifier = Modifier,
    pageIndex: Int = 0
) {
    val context = LocalContext.current

    val imageBitmap by produceState<ImageBitmap?>(initialValue = null, assetFileName, pageIndex) {
        value = withContext(Dispatchers.IO) {
            try {
                val file = File(context.cacheDir, assetFileName).apply {
                    if (!exists()) {
                        context.assets.open(assetFileName).use { input ->
                            outputStream().use { output ->
                                input.copyTo(output)
                            }
                        }
                    }
                }
                val fileDescriptor =
                    ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
                val pdfRenderer = PdfRenderer(fileDescriptor)

                pdfRenderer.use { renderer ->
                    val page = renderer.openPage(pageIndex)
                    val bitmap = createBitmap(page.width, page.height)
                    page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                    page.close()
                    bitmap.asImageBitmap()
                }
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }
    }

    if (imageBitmap != null) {
        Image(
            bitmap = imageBitmap!!,
            contentDescription = "PDF Page: $assetFileName",
            modifier = modifier
        )
    } else {
        Box(modifier = modifier.background(Color.LightGray)) {
            // You can show a loading indicator or an error message here
        }
    }
}
