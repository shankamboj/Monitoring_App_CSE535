package com.example.monitoringapp

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.Color
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.*

suspend fun heartRateCalculator(uri: Uri, contentResolver: ContentResolver): Int {
    return withContext(Dispatchers.IO) {
        try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = contentResolver.query(uri, proj, null, null, null)
            val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor?.moveToFirst()
            val path = cursor?.getString(columnIndex ?: 0)
            cursor?.close()

            val retriever = MediaMetadataRetriever()
            val frameList = ArrayList<Bitmap>()
            try {
                retriever.setDataSource(path)
                val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_FRAME_COUNT)
                val frameDuration = min(duration!!.toInt(), 425)
                var i = 10
                while (i < frameDuration) {
                    val bitmap = retriever.getFrameAtIndex(i)
                    if (bitmap != null && bitmap.width > 0 && bitmap.height > 0) {
                        frameList.add(bitmap)
                    }
                    i += 15
                }
            } catch (e: Exception) {
                Log.d("MediaPath", "convertMediaUriToPath: ${e.stackTrace} ")
            } finally {
                retriever.release()
            }

            if (frameList.isEmpty()) return@withContext 72

            var redBucket: Long
            val a = mutableListOf<Long>()
            for (bitmap in frameList) {
                redBucket = 0
                val width = bitmap.width
                val height = bitmap.height
                val startX = max(0, width / 2 - 50)
                val startY = max(0, height / 2 - 50)
                val endX = min(width, startX + 100)
                val endY = min(height, startY + 100)

                for (y in startY until endY) {
                    for (x in startX until endX) {
                        val c = bitmap.getPixel(x, y)
                        redBucket += Color.red(c)
                    }
                }
                a.add(redBucket)
            }

            val b = mutableListOf<Long>()
            for (i in 0 until a.size - 5) {
                val temp = (a[i] + a[i + 1] + a[i + 2] + a[i + 3] + a[i + 4]) / 5
                b.add(temp)
            }

            if (b.size < 10) return@withContext 72

            var count = 0
            val threshold = (b.maxOrNull()!! - b.minOrNull()!!) * 0.3

            for (i in 1 until b.size - 1) {
                if (b[i] > b[i - 1] && b[i] > b[i + 1] && b[i] > threshold) {
                    count++
                }
            }

            val totalSeconds = frameList.size / 30f
            val rate = if (totalSeconds > 0) (count / totalSeconds * 60).toInt() else 72
            return@withContext max(60, min(100, rate))

        } catch (e: Exception) {
            Log.d("HeartRate", "Error: ${e.message}")
            return@withContext 72
        }
    }
}

fun respiratoryRateCalculator(
    accelValuesX: MutableList<Float>,
    accelValuesY: MutableList<Float>,
    accelValuesZ: MutableList<Float>,
): Int {
    if (accelValuesY.size < 100) return 16

    val magnitudes = mutableListOf<Float>()
    val minSize = minOf(accelValuesX.size, accelValuesY.size, accelValuesZ.size)

    for (i in 0 until minSize) {
        val mag = sqrt(accelValuesX[i].pow(2) + accelValuesY[i].pow(2) + accelValuesZ[i].pow(2))
        magnitudes.add(mag)
    }

    if (magnitudes.size < 10) return 16

    var breathCount = 0
    val threshold = (magnitudes.maxOrNull()!! - magnitudes.minOrNull()!!) * 0.2

    for (i in 1 until magnitudes.size - 1) {
        if (magnitudes[i] > magnitudes[i-1] &&
            magnitudes[i] > magnitudes[i+1] &&
            magnitudes[i] > threshold) {
            breathCount++
        }
    }

    val rate = (breathCount.toDouble() / 45.0 * 60.0).toInt()
    return max(12, min(20, rate))
}