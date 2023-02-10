package com.base.bitmap

import android.content.Context
import android.graphics.*
import android.graphics.Bitmap.CompressFormat
import android.net.Uri
import android.os.Environment
import android.view.View
import androidx.exifinterface.media.ExifInterface
import com.base.encode.Base64Utils
import java.io.*
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import kotlin.math.roundToInt


/**
 * 这个类提供一些操作Bitmap的方法
 */
object BitmapUtils {
    /**
     * 图像的旋转方向是0
     */
    const val ROTATE0: Int = 0
    /**
     * 图像的旋转方向是90
     */
    const val ROTATE90: Int = 90
    /**
     * 图像的旋转方向是180
     */
    const val ROTATE180: Int = 180
    /**
     * 图像的旋转方向是270
     */
    const val ROTATE270: Int = 270
    /**
     * 图像的旋转方向是360
     */
    const val ROTATE360: Int = 360
    /**
     * 图片太大内存溢出后压缩的比例
     */
    const val PIC_COMPRESS_SIZE: Int = 4
    /**
     * 图像压缩边界
     */
    const val IMAGEBOUND: Int = 128

    private const val DEFAULT_JPEG_QUALITY: Int = 90

    /**
     * 得到显示宽度
     *
     * @param context Context
     * @return 宽度
     */
    private fun getDisplayWidth(context: Context): Int {
        return context.resources.displayMetrics.widthPixels
    }

    /**
     * 得到显示高度
     *
     * @param context Context
     * @return 高度
     */
    private fun getDisplayHeight(context: Context): Int {
        return context.resources.displayMetrics.heightPixels
    }

    /**
     * 解析图片的旋转方向
     *
     * @param jpeg 图片数据
     * @return 旋转角度
     */
    fun decodeImageDegree(jpeg: ByteArray): Int {
        return ImageExif.getOrientation(jpeg)
    }

    /**
     * 解析图片的旋转方向
     *
     * @param path 图片的路径
     * @return 旋转角度
     */
    private fun decodeImageDegree(path: String): Int {
        return try {
            val exifInterface = ExifInterface(path)
            val orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL)
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
    fun rotateBitmap(orientation: Float, bitmap: Bitmap): Bitmap {
        val m = Matrix()
        return if (orientation == 0f) {
            bitmap
        } else {
            m.setRotate(orientation)
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, m, true)
        }
    }


    /**
     * 等比压缩图片
     *
     * @param bitmap 原图
     * @param scale  压缩因子
     * @return 压缩后的图片
     */
    private fun scale(bitmap: Bitmap, scale: Float): Bitmap {
        val matrix = Matrix()
        matrix.postScale(scale, scale)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
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

    fun createLivenessBitmap(context: Context, argbByte: IntArray, roundRect: Rect): Bitmap? {
        var transformed: Bitmap? = null
        try {
            transformed = Bitmap.createBitmap(argbByte, roundRect.width(), roundRect.height(), Bitmap.Config.ARGB_8888)
        } catch (e: OutOfMemoryError) {
            e.printStackTrace()
            transformed?.recycle()
        }

        return transformed
    }

    /**
     * 得到要显示的图片数据
     *
     * @param data        拍照保存的图片数据byte[]类型
     * @param orientation 图片方向
     * @param format      压缩格式
     * @return Bitmap 返回显示的图片bitmap
     */
    fun createBitmap(data: ByteArray, maxPixel: Int,
                     maxSize: Int, orientation: Float = ROTATE0.toFloat(),
                     format: CompressFormat = CompressFormat.JPEG): Bitmap? {
        var bitmap: Bitmap? = null
        var transformed: Bitmap? = null
        try {
            bitmap = CustomCompress().compressGetBitmap(data, maxPixel = maxPixel, maxSize = maxSize, format = format)
            bitmap?.let {
                transformed = rotateBitmap(orientation, it)
            }
        } catch (e: OutOfMemoryError) {
//            e.throwWhenLog()
            if (bitmap?.isRecycled == false) {
                bitmap?.recycle()
            }
            if (transformed?.isRecycled == false) {
                transformed?.recycle()
                transformed = null
            }
        }
        if (transformed != bitmap && bitmap != null) {
            bitmap.recycle()
        }
        return transformed

    }

    /**
     * 获取无损压缩图片合适的压缩比例
     *
     * @param options        图片的一些设置项
     * @param minSideLength  最小边长
     * @param maxNumOfPixels 最大的像素数目
     * @return 返回合适的压缩值
     */
    fun computeSampleSize(options: BitmapFactory.Options, minSideLength: Int, maxNumOfPixels: Int): Int {
        val initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels)
        var roundedSize: Int
        if (initialSize <= 8) { // SUPPRESS CHECKSTYLE
            roundedSize = 1
            while (roundedSize < initialSize) {
                roundedSize = roundedSize shl 1
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8 // SUPPRESS CHECKSTYLE
        }
        return roundedSize
    }

    /**
     * 获取无损压缩图片的压缩比
     *
     * @param options        图片的一些设置项
     * @param minSideLength  最小边长
     * @param maxNumOfPixels 最大的像素数目
     * @return 返回合适的压缩值
     */
    fun computeInitialSampleSize(options: BitmapFactory.Options,
                                 minSideLength: Int,
                                 maxNumOfPixels: Int): Int {
        val w = options.outWidth.toDouble()
        val h = options.outHeight.toDouble()
        val lowerBound = if (maxNumOfPixels == -1) 1 else Math.ceil(Math.sqrt(w * h / maxNumOfPixels)).toInt()
        val upperBound = if (minSideLength == -1)
            IMAGEBOUND
        else
            Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength)).toInt()
        if (upperBound < lowerBound) {
            return lowerBound
        }
        return if (maxNumOfPixels == -1 && minSideLength == -1) {
            1
        } else if (minSideLength == -1) {
            lowerBound
        } else {
            upperBound
        }
    }

    /**
     * @param bitmap  图片
     * @param quality 生成的JPG的质量
     * @param maxSize 最大边像素数
     * @return base64编码的数据
     */
    fun bitmapToJpegBase64(bitmap: Bitmap, quality: Int, maxSize: Float): String? {
        try {
            val scale = maxSize / Math.max(bitmap.width, bitmap.height)
            val newBitmap = if (scale < 1) {
                scale(bitmap, scale)
            } else {
                bitmap
            }
            val out = ByteArrayOutputStream()
            newBitmap.compress(CompressFormat.JPEG, quality, out)
            val data = out.toByteArray()
            out.close()

            return Base64Utils.encodeToString(data, Base64Utils.NO_WRAP)
        } catch (e: Exception) {
            return null
        }
    }

    /**
     * 图片转换为base64
     *
     * @param bitmap  图片
     * @param quality 压缩质量
     * @return base64编码的数据
     */
    fun bitmapToJpegBase64(bitmap: Bitmap, quality: Int): String? {
        var out: ByteArrayOutputStream? = null
        try {
            out = ByteArrayOutputStream()
            bitmap.compress(CompressFormat.JPEG, quality, out)
            val data = out.toByteArray()
            return Base64Utils.encodeToString(data, Base64Utils.NO_WRAP)
        } catch (e: Exception) {
            return null
        } finally {
            if (out != null) {
                try {
                    out.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
    }


    /**
     * 等比压缩图片
     *
     * @param resBitmap 原图
     * @param desWidth  压缩后图片的宽度
     * @param desHeight 压缩后图片的高度
     * @return 压缩后的图片
     */
    fun calculateInSampleSize(resBitmap: Bitmap, desWidth: Int, desHeight: Int): Bitmap? {
        if (resBitmap.isRecycled) {
            return null
        }
        val resWidth = resBitmap.width
        val resHeight = resBitmap.height
        if (resHeight > desHeight || resWidth > desWidth) {
            // 计算出实际宽高和目标宽高的比率
            val heightRatio = desHeight.toFloat() / resHeight.toFloat()
            val widthRatio = desWidth.toFloat() / resWidth.toFloat()
            val scale = if (heightRatio < widthRatio) heightRatio else widthRatio
            return scale(resBitmap, scale)
        }
        return resBitmap
    }

    /**
     * 等比放大图片
     *
     * @param resBitmap 原图
     * @param desWidth  压缩后图片的宽度
     * @param desHeight 压缩后图片的高度
     * @return 当大后的图片
     */
    fun getBitmapWithEnlarge(resBitmap: Bitmap, desWidth: Int, desHeight: Int): Bitmap {
        val resWidth = resBitmap.width
        val resHeight = resBitmap.height
        if (resHeight < desHeight || resWidth < desWidth) {
            // 计算出实际宽高和目标宽高的比率
            val heightRatio = desHeight.toFloat() / resHeight.toFloat()
            val widthRatio = desWidth.toFloat() / resWidth.toFloat()
            val scale = if (heightRatio < widthRatio) widthRatio else heightRatio
            return scale(resBitmap, scale)
        }
        return resBitmap
    }

    private fun ensureDirectoryExist(dir: File?): Boolean {
        if (dir == null) {
            return false
        }
        if (!dir.exists()) {
            try {
                dir.mkdirs()
            } catch (e: SecurityException) {
                return false
            }

        }
        return true
    }

    /**
     * 压缩文件存储到本地
     */
    fun compressToFile(bitmap: Bitmap, path: String,
                       format: CompressFormat = CompressFormat.JPEG,
                       quality: Int = DEFAULT_JPEG_QUALITY): Boolean {
        val f = File(path)
        var fos: FileOutputStream? = null
        var result = false
        try {
            if (f.exists()){
                f.deleteRecursively()
            }
            fos = FileOutputStream(f)
            bitmap.compress(format, quality, fos)
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

    fun getByteArray(bmp: Bitmap, quality: Int, needRecycle: Boolean): ByteArray {
        return try {
            val output = ByteArrayOutputStream()
            bmp.compress(Bitmap.CompressFormat.JPEG, quality, output)
            if (needRecycle) {
                bmp.recycle()
            }
            val result = output.toByteArray()
            output.close()
            result
        } catch (e: Exception) {
            ByteArray(0)
        }
    }

    fun getByteArray(rawBitmap: Bitmap, maxSize: Long): ByteArray? {
        return try {
            var currentByteArray = ByteArray(0)
            ByteArrayOutputStream().use {
                rawBitmap.compress(CompressFormat.JPEG, 100, it)
                currentByteArray = it.toByteArray()
            }
            if (currentByteArray.isEmpty()) return null
            val sourceSize = currentByteArray.size
            if (currentByteArray.size < maxSize) {
                return currentByteArray
            }
            var sample = 1
            var bitmap: Bitmap? = null
            try {
                while (currentByteArray.size > maxSize) {
                    sample *= 2
                    val options = BitmapFactory.Options()
                    options.inSampleSize = sample
                    options.inJustDecodeBounds = false
                    ByteArrayOutputStream().use { out ->
                        bitmap = BitmapFactory.decodeByteArray(currentByteArray, 0, currentByteArray.size, options)
                        bitmap?.compress(CompressFormat.JPEG, 100, out)
                        currentByteArray = out.toByteArray()
                    }
                    bitmap?.recycle()
                }
            } catch (e: Throwable) { // 尝试捕获内存不足的异常
                if (bitmap?.isRecycled != true) {
                    bitmap?.recycle()
                }
                bitmap = null
            }
            if (currentByteArray.isEmpty()) return null
            return if (currentByteArray.size < maxSize) {
                currentByteArray
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    fun getByteArray(byteArray: ByteArray, maxSize: Long): ByteArray? {
        var bitmap: Bitmap? = null
        return try {
            getByteArray(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size, BitmapFactory.Options()).also {
                bitmap = it
            }, maxSize)
        } catch (e: Exception) {
            bitmap?.recycle()
            null
        }
    }

    /**
     * 获取Bitmap, 如果bitmap是倾斜的则直接放正
     * @param file 文件源
     * @param canSample 能够放缩，如果支持则会减少内存，避免OOM
     * @param maxPixel 最大图片像素大小
     * @param maxSize 最大图片size
     */
    fun getBitmap(context: Context, file: File,
                  canSample: Boolean = true,
                  maxPixel: Int, maxSize: Int): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            val rawBitmap: Bitmap? = if (!canSample) {
                BitmapFactory.decodeFile(file.absolutePath, BitmapFactory.Options().apply { this.inMutable = inMutable })
            } else {
                CustomCompress().compressGetByteArray(file, maxPixel = maxPixel, maxSize = maxSize)?.let {
                    BitmapFactory.decodeByteArray(it, 0, it.size, BitmapFactory.Options())
                }
            }
            bitmap = checkOrientation(file, rawBitmap)
            if (rawBitmap != bitmap) {
                rawBitmap?.recycle()
            }
        } catch (e: OutOfMemoryError) { // 尝试捕获内存不足的异常
//            e.throwWhenLog()
            bitmap?.recycle()
            bitmap = null
        }
        return bitmap
    }

    fun getBitmapWithSampleUnSafe(file: File, sample: Int, inMutable: Boolean = false): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            val options = BitmapFactory.Options()
            val filePath = file.absolutePath
            BitmapFactory.decodeFile(filePath, options)
            options.inSampleSize = sample
            options.inJustDecodeBounds = false
            options.inMutable = inMutable
            val rawBitmap: Bitmap = BitmapFactory.decodeFile(filePath, options)?:return null
            val orientation = decodeImageDegree(filePath)
            bitmap = rotateBitmap(orientation.toFloat(), rawBitmap)
            if (bitmap != rawBitmap) {
                rawBitmap.recycle()
            }
        } catch (e: Throwable) {
//            e.throwWhenLog()
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
//            e.throwWhenLog()
            bitmap?.recycle()
            null
        }
    }

    fun calculateInSampleSize(context: Context, file: File,
                              maxPixel: Int = Math.max(getDisplayHeight(context), getDisplayWidth(context))): Int {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(file.absolutePath, options)
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if (height > maxPixel || width > maxPixel) {
            while (height / inSampleSize > maxPixel || width / inSampleSize > maxPixel) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    fun getBitmapFromView(view: View): Bitmap? {
        return Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)?.apply {
            view.draw(Canvas(this))
        }
    }

    fun cropLocalImage(localPath: String, rect: Rect): Bitmap? {
        var rawBitmap: Bitmap? = null
        var bitmap: Bitmap? = null
        try {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = false
            options.inMutable = false
            rawBitmap = BitmapFactory.decodeFile(localPath, options)
            if (rawBitmap == null) {
                return null
            }
            bitmap = Bitmap.createBitmap(rawBitmap, rect.left, rect.top, rect.width(), rect.height())
            if (rawBitmap != bitmap) {
                rawBitmap.recycle()
            }
        } catch (e: Throwable) {
//            e.throwWhenLog()
            e.printStackTrace()
            rawBitmap?.recycle()
            bitmap?.recycle()
            bitmap = null
        }
        return bitmap
    }

    fun cropImage(rawBitmap: Bitmap, rect: Rect): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            bitmap = Bitmap.createBitmap(rawBitmap, rect.left, rect.top, rect.width(), rect.height())
        } catch (e: Throwable) {
            e.printStackTrace()
        } finally {
            if (rawBitmap != bitmap) {
                if (!rawBitmap.isRecycled) {
                    rawBitmap.recycle()
                }
            }
        }
        return bitmap
    }

    fun scaleResourcesToBitmap(context: Context, resId: Int, width: Int, height: Int = width): Bitmap? {
        return BitmapFactory.decodeResource(context.resources, resId).use {
            Bitmap.createScaledBitmap(it, width, height, true)
        }
    }

    fun getDrawableImageBytes(context: Context, resId: Int): ByteArray {
        val thumb = BitmapFactory.decodeResource(context.resources, resId)
        return getByteArray(thumb, 75, true)
    }

    fun getBitmapWithUri(context: Context, imageUri: Uri, maxSize: Int): Bitmap? {
        var inputStream: InputStream? = null
        return try {
            val contentResolver = context.contentResolver
            inputStream = contentResolver.openInputStream(imageUri)?:return null
            val boundsOptions = BitmapFactory.Options()
            boundsOptions.inJustDecodeBounds = true
            BitmapFactory.decodeStream(inputStream, null, boundsOptions)
            inputStream.close()
            var scale = 1
            if (boundsOptions.outHeight > maxSize || boundsOptions.outWidth > maxSize) {
                scale = (boundsOptions.outHeight.coerceAtLeast(boundsOptions.outWidth).toDouble() / maxSize).roundToInt()
            }
            val scaleOptions = BitmapFactory.Options()
            scaleOptions.inSampleSize = scale
            inputStream = contentResolver.openInputStream(imageUri)
            BitmapFactory.decodeStream(inputStream, null, scaleOptions)
        } catch (e: FileNotFoundException) {
            null
        } catch (e: IOException) {
            null
        }finally {
            inputStream?.use {  }
        }
    }

    /**
     * Converts a immutable bitmap to a mutable bitmap. This operation doesn't allocates
     * more memory that there is already allocated.
     *
     * @param imgIn - Source image. It will be released, and should not be used more
     * @return a copy of imgIn, but muttable.
     */
    fun convertToMutable(imgIn: Bitmap): Bitmap {
        var imgIn = imgIn
        try {
            //this is the file going to use temporally to save the bytes.
            // This file will not be a image, it will store the raw image data.
            val file = File("${Environment.getExternalStorageDirectory()}${File.separator}temp.tmp")

            //Open an RandomAccessFile
            //Make sure you have added uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
            //into AndroidManifest.xml file
            val randomAccessFile = RandomAccessFile(file, "rw")

            // get the width and height of the source bitmap.
            val width = imgIn.width
            val height = imgIn.height
            val type = imgIn.config

            //Copy the byte to the file
            //Assume source bitmap loaded using options.inPreferredConfig = Config.ARGB_8888;
            val channel: FileChannel = randomAccessFile.channel
            val map: MappedByteBuffer =
                channel.map(FileChannel.MapMode.READ_WRITE, 0, (imgIn.rowBytes * height).toLong())
            imgIn.copyPixelsToBuffer(map)
            //recycle the source bitmap, this will be no longer used.
            imgIn.recycle()
            System.gc() // try to force the bytes from the imgIn to be released

            //Create a new bitmap to load the bitmap again. Probably the memory will be available.
            imgIn = Bitmap.createBitmap(width, height, type)
            map.position(0)
            //load it back from temporary
            imgIn.copyPixelsFromBuffer(map)
            //close the temporary file and channel , then delete that also
            channel.close()
            randomAccessFile.close()

            // delete the temp file
            file.delete()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return imgIn
    }

}

inline fun <R> Bitmap.use(block: (Bitmap) -> R): R {
    try {
        return block(this)
    } catch (e: Throwable) {
        throw e
    } finally {
        this.recycle()
    }
}