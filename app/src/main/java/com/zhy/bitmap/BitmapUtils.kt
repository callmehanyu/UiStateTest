package com.zhy.bitmap

import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.view.View
import androidx.exifinterface.media.ExifInterface
import java.io.*

/**
 * 图像的旋转方向是0
 */
private const val ROTATE0: Int = 0

/**
 * 图像的旋转方向是90
 */
private const val ROTATE90: Int = 90

/**
 * 图像的旋转方向是180
 */
private const val ROTATE180: Int = 180

/**
 * 图像的旋转方向是270
 */
private const val ROTATE270: Int = 270


private const val DEFAULT_JPEG_QUALITY: Int = 90


/**
 * 解析图片的旋转方向
 *
 * @param path 图片的路径
 * @return 旋转角度
 */
private fun decodeImageDegree(path: String): Int {
    return try {
        val exifInterface = ExifInterface(path)
        val orientation = exifInterface.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> ROTATE90
            ExifInterface.ORIENTATION_ROTATE_180 -> ROTATE180
            ExifInterface.ORIENTATION_ROTATE_270 -> ROTATE270
            else -> ROTATE0
        }
    } catch (e: Exception) {
        e.printStackTrace()
        ROTATE0
    }
}

/**
 * 根据从数据中读到的方向旋转图片
 *
 * @param orientation 图片方向
 * @param bitmap      要旋转的bitmap
 * @return 旋转后的图片
 */
private fun rotateBitmap(orientation: Float, bitmap: Bitmap): Bitmap {
    val m = Matrix()
    return if (orientation == 0f) {
        bitmap
    } else {
        m.setRotate(orientation)
        Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, m, true)
    }
}

private fun closeQuietly(closeable: Closeable?) {
    if (closeable != null) {
        try {
            closeable.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

/**
 * 压缩文件存储到本地
 */
fun Bitmap.compressToFile(
    path: String, format: CompressFormat = CompressFormat.JPEG,
    quality: Int = DEFAULT_JPEG_QUALITY
): Boolean {
    val f = File(path)
    var fos: FileOutputStream? = null
    var result = false
    try {
        if (f.exists()) {
            f.deleteRecursively()
        }
        fos = FileOutputStream(f)
        compress(format, quality, fos)
        fos.flush()
        result = true
    } catch (e: IOException) {
        e.printStackTrace()
        // 异常时删除保存失败的文件
        try {
            if (f.exists() && f.isFile) {
                f.delete()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    } catch (ex: Exception) {
        try {
            if (f.exists() && f.isFile) {
                f.delete()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    } finally {
        closeQuietly(fos)
        return result
    }
}


/**
 * 获取Bitmap, 如果bitmap是倾斜的则直接放正
 * @param this@getBitmap 文件源
 * @param canSample 能够放缩，如果支持则会减少内存，避免OOM
 * @param maxPixel 最大图片像素大小
 * @param maxSize 最大图片size
 */
fun File.getBitmap(
    canSample: Boolean = true,
    maxPixel: Int,
    maxSize: Int
): Bitmap? {
    var bitmap: Bitmap? = null
    try {
        val rawBitmap: Bitmap? = if (!canSample) {
            BitmapFactory.decodeFile(
                absolutePath,
                BitmapFactory.Options().apply { this.inMutable = inMutable })
        } else {
            CustomCompress().compressGetByteArray(this, maxPixel = maxPixel, maxSize = maxSize)
                ?.let {
                    BitmapFactory.decodeByteArray(it, 0, it.size, BitmapFactory.Options())
                }
        }
        bitmap = checkOrientation(this, rawBitmap)
        if (rawBitmap != bitmap) {
            rawBitmap?.recycle()
        }
    } catch (e: OutOfMemoryError) { // 尝试捕获内存不足的异常
        bitmap?.recycle()
        bitmap = null
    }
    return bitmap
}

private fun checkOrientation(file: File, rawBitmap: Bitmap?): Bitmap? {
    var bitmap: Bitmap? = null
    return try {
        if (rawBitmap == null) {
            return null
        }
        val orientation = decodeImageDegree(file.absolutePath)
        bitmap = rotateBitmap(orientation.toFloat(), rawBitmap)
        bitmap
    } catch (e: Throwable) {
        bitmap?.recycle()
        null
    }
}

fun View.getBitmap(): Bitmap? {
    return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)?.apply {
        draw(Canvas(this))
    }
}

/**
 * 读取Assets文件夹中的图片资源
 */
fun String.getImageFromAssetsFile(context: Context): Bitmap? {
    var image: Bitmap? = null
    val am: AssetManager = context.resources.assets
    try {
        val inputStream = am.open(this)
        image = BitmapFactory.decodeStream(inputStream)
        inputStream.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return image
}
