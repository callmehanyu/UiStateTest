package com.zhy.bitmap

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import java.io.ByteArrayOutputStream
import java.io.File
import kotlin.math.min


/**
 * 压缩目标：
 * 1、第一次采样的目的，是为了最大效率将图压缩到要求像素范围附近
 * 2、第二次缩放压缩是为了保证在要求像素范围内，保留最大像素值，提高清晰度
 * 3、第三次质量压缩和采样混合压缩是为了保证在最大像素值下，满足maxSize的要求
 *
 * 在保留最大清晰度的情况下，压缩
 * Created by yeliangliang on 2020-03-05
 */
class CustomCompress {

    /**
     * 压缩得到byte
     * @maxSize 最大大小，单位字节
     * @maxPixel 最大像素，单位pix
     */
    fun <T> compressGetByteArray(source: T, maxPixel: Int, maxSize: Int, format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG): ByteArray? {
        return sampleCompress(source, maxPixel)?.let {
            matrixCompress(it, maxPixel)?.let {
                qualityAndSampleCompress(it, maxSize, format)
            }
        }
    }

    /**
     * 缩放压缩
     */
    private fun matrixCompress(bitmap: Bitmap, maxPixel: Int): Bitmap? {

        val height = bitmap.height
        val width = bitmap.width
        if (height == 0 || width == 0) {
            return bitmap
        }
        val heightScale = maxPixel.toFloat() / height.toFloat()
        val widthScale = maxPixel.toFloat() / width.toFloat()
        val scale = min(heightScale, widthScale)
        return if (scale < 1 && scale > 0) {
            val matrix = Matrix()
            matrix.setScale(scale, scale)
            try {
                Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            } finally {
                bitmap.recycle()
            }
        } else bitmap
    }


    /**
     * 质量抽样压缩
     */
    private fun qualityAndSampleCompress(bitmap: Bitmap, maxSize: Int, format: Bitmap.CompressFormat): ByteArray? {
        //先质量压缩，10%一次，直到50%
        var result = ByteArray(0)
        ByteArrayOutputStream().use {
            bitmap.compress(format, 100, it)
            it.toByteArray().run {
                result = this
                var quality = 100
                while (result.size > maxSize && quality > 50) {
                    quality -= 10
                    ByteArrayOutputStream().use { out ->
                        bitmap.compress(format, quality, out)
                        out.toByteArray().run {
                            result = this
                        }
                    }
                }
            }
        }
        if (!bitmap.isRecycled) {
            bitmap.recycle()
        }
        //50%不满足size，采样一半
        return if (result.size > maxSize) {
            //质量压缩到50还不满足，抽样一半
            val opt = BitmapFactory.Options()
            opt.inSampleSize = 2
            opt.inJustDecodeBounds = false
            return ByteArrayOutputStream().use { out ->
                val b = try {
                    BitmapFactory.decodeByteArray(result, 0, result.size, opt)
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }
                b?.compress(format, 100, out)
                val currentByteArray = out.toByteArray()
                if (b == null || currentByteArray.isEmpty()) {
//                    throwWhenLog { "compress failed bitmap == null or currentByteArray == null" }
                    return null
                }
                //采样一半还不满足size，递归，要求最终压缩像素大于1，防止死循环
                if (currentByteArray.size > maxSize) {
                    if (b.width < 2 && b.height < 2) {
//                        throwWhenLog { "compress failed bitmap.width or bitmap.height < 2" }
                        return@use null
                    }
                    return@use qualityAndSampleCompress(b, maxSize, format)
                } else {
                    if (!b.isRecycled) {
                        b.recycle()
                    }
                    return@use currentByteArray
                }
            }
        } else {
            result
        }
    }

    /**
     * 采样压缩
     * 注意：为了保留最大像素，会除去最后一个采样倍数
     */
    private fun <T> sampleCompress(source: T, maxPixel: Int): Bitmap? {
        var bitmap: Bitmap? = null
        return try {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true

            when (source) {
                is File -> {
                    BitmapFactory.decodeFile(source.absolutePath, options)
                }
                is ByteArray -> {
                    BitmapFactory.decodeByteArray(source, 0, source.size, options)
                }
                else -> {
//                    throwWhenLog { "not support format" }
                }
            }

            val height = options.outHeight
            val width = options.outWidth
            var inSampleSize = 1
            if (height > maxPixel || width > maxPixel) {
                while (height / inSampleSize > maxPixel || width / inSampleSize > maxPixel) {
                    inSampleSize *= 2
                }
            }
            //除去最后一个采样倍数
            if (inSampleSize > 1) {
                inSampleSize /= 2
            }
            val opt = BitmapFactory.Options()
            opt.inSampleSize = inSampleSize
            opt.inJustDecodeBounds = false
            opt.inMutable = true
            when (source) {
                is File -> {
                    bitmap = BitmapFactory.decodeFile(source.absolutePath, opt)
                }
                is ByteArray -> {
                    bitmap = BitmapFactory.decodeByteArray(source, 0, source.size, opt)
                }
                else -> {
//                    throwWhenLog { "not support format" }
                }
            }
            bitmap
        } catch (e: Exception) { // 尝试捕获内存不足的异常
            e.printStackTrace()
            if (bitmap?.isRecycled == false) {
                bitmap?.recycle()
            }
            null
        }
    }
}