package com.bzapata.triangle.emulatorScreen.presentation.components

import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Icon
import android.net.Uri
import androidx.core.content.getSystemService
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.bzapata.triangle.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun createPinnedShortcut(context: Context, shortcutId: String, label: String, iconUri: Uri) {
    val shortcutManager = context.getSystemService<ShortcutManager>()
    if (shortcutManager != null && shortcutManager.isRequestPinShortcutSupported) {
        CoroutineScope(Dispatchers.IO).launch {
            val loader = ImageLoader(context)
            val request = ImageRequest.Builder(context)
                .data(iconUri)
                .allowHardware(false)
                .build()
            val result = loader.execute(request)
            var bitmap = if (result is SuccessResult) {
                result.drawable.toBitmap()
            } else null
            if (bitmap != null)
                bitmap = getSquareCroppedBitmap(bitmap)


            val intent = Intent(context, MainActivity::class.java).apply {
                action = "LAUNCH_GAME_FROM_SHORTCUT"
                putExtra("game_hash", shortcutId)
                // Add flags to ensure it opens correctly from launcher
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }


            val pinShortcutInfoBuilder = ShortcutInfo.Builder(context, shortcutId)
                .setShortLabel(label)
                .setIntent(intent)

            // Set the bitmap icon if successfully loaded, otherwise fallback to app icon
            if (bitmap != null) {
                pinShortcutInfoBuilder.setIcon(Icon.createWithBitmap(bitmap))
            }

            val pinShortcutInfo = pinShortcutInfoBuilder.build()

            // 4. Request the system to pin
            shortcutManager.requestPinShortcut(pinShortcutInfo, null)
        }
    }
}

private fun getSquareCroppedBitmap(bitmap: Bitmap): Bitmap {
    val size = Math.min(bitmap.width, bitmap.height)
    val output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(output)

    val srcRect = if (bitmap.width > bitmap.height) {
        // Landscape: crop sides
        val left = (bitmap.width - bitmap.height) / 2
        Rect(left, 0, left + size, size)
    } else {
        // Portrait: crop top/bottom
        val top = (bitmap.height - bitmap.width) / 2
        Rect(0, top, size, top + size)
    }

    val dstRect = Rect(0, 0, size, size)
    canvas.drawBitmap(bitmap, srcRect, dstRect, null)
    return output
}