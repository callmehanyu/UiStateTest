package com.base.file

import android.content.Context
import android.os.Environment
import java.io.*

/**
 * Created by 魏铮铮 on 2020/3/26.
 */
private val externalDir = Environment.getExternalStorageDirectory()
private val youaDir = File(externalDir, "Youa")
private val downloadDir = File(externalDir, "一刻相册")
private const val VIDEO_DIR_NAME = "video"
private const val IMAGE_DIR_NAME = "image"
private const val TRANS_CODE_DIR_NAME = ".trans_code"
private val externalImageDir = File(youaDir, IMAGE_DIR_NAME)
private val externalVideoDir = File(youaDir, VIDEO_DIR_NAME)
private val externalTransCodeDir = File(youaDir, TRANS_CODE_DIR_NAME)
private const val TEMPLATE_DIR = "YouRes"
private const val ORIGINN_IMAGE_DIR = "OriginImageRes"
private const val MANUAL_PRODUCT_DIR = "ManualProductRes"
private const val CLASSIFICATION_DIR = "classificationRes"
private const val P2P_DOWNLOAD_LOG_DIR = "P2PLog"
private const val SHARE_DIR = "Share"
private const val SHARE_CARD_DIR = "ShareCard"
private const val MIN_SDCARD_SIZE: Int = 4 * 1024 * 1024
private const val JSON_OBJECT_NAME_FILE = "file"
private const val JSON_OBJECT_NAME_DURATION = "duration_ms"
internal const val BYTE_ARRAY_BUFFER_SIZE: Int = 1024

internal const val ROTATE_90: Int = 90
internal const val ROTATE_180: Int = 180
internal const val ROTATE_270: Int = 270

/**
 * 获取原图名称
 */
fun getOriginImageName(pcsMd5: String, format: String) = "$pcsMd5.$format"

/**
 * 获取外部图片存储的目录
 */
fun getExternalPictureDirectory(): File {
    val file = externalImageDir
    return if (file.exists()) {
        if (file.isDirectory) {
            file
        } else {
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        }
    } else {
        if (file.mkdirs()) {
            file
        } else {
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        }
    }
}

/**
 * 获取外部视频存储目录
 */
fun getExternalVideoDirectory(): File {
    val file = externalVideoDir
    return if (file.exists()) {
        if (file.isDirectory) {
            file
        } else {
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
        }
    } else {
        if (file.mkdirs()) {
            file
        } else {
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
        }
    }
}

/**
 * 获取视频缓存目录
 */
fun getCacheVideoDirectory(context: Context): File {
    val file = File(context.cacheDir, VIDEO_DIR_NAME)
    if (file.exists()) {
        if (!file.isDirectory) {
            file.delete()
            file.mkdirs()
        }
    } else {
        file.mkdirs()
    }
    return file
}

/**
 * 获取分享卡片缓存目录
 */
fun getCacheShareCardDirectory(context: Context): File {
    val file = File(context.cacheDir, SHARE_CARD_DIR)
    if (file.exists()) {
        if (!file.isDirectory) {
            file.delete()
            file.mkdirs()
        }
    } else {
        file.mkdirs()
    }
    return file
}


/**
 * 删除分享卡片缓存目录
 */
fun deleteCacheShareCardDirectory(context: Context) {
    val file = File(context.cacheDir, SHARE_CARD_DIR)
    if (file.exists()) {
        file.delete()
    }
}

/**
 * 获取分享外部缓存目录
 */
fun getExternalCacheShareDirectory(context: Context): File {
    val file = File(context.externalCacheDir, SHARE_DIR)
    if (file.exists()) {
        if (!file.isDirectory) {
            file.delete()
            file.mkdirs()
        }
    } else {
        file.mkdirs()
    }
    return file
}

/**
 * 删除分享外部缓存目录
 */
fun deleteExternalCacheShareDirectory(context: Context) {
    val file = File(context.externalCacheDir, SHARE_DIR)
    if (file.exists()) {
        file.delete()
    }
}

/**
 * 获取外部转码目录
 */
fun getExternalTransCodeDirectory(): File {
    val file = externalTransCodeDir
    return if (file.exists()) {
        if (file.isDirectory) {
            file
        } else {
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
        }
    } else {
        if (file.mkdirs()) {
            file
        } else {
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
        }
    }
}

/**
 * 获取外部转码缓存目录
 */
fun getCacheTransCodeDirectory(context: Context): File {
    val file = File(context.cacheDir, TRANS_CODE_DIR_NAME)
    if (file.exists()) {
        if (!file.isDirectory) {
            file.delete()
            file.mkdirs()
        }
    } else {
        file.mkdirs()
    }
    return file
}

/**
 * 存储空间是否充足
 */
//fun isStorageEnough(need: Long): Boolean {
//    val freeSize = getPhoneStoreInfo()?.freeSize ?: return true
//    return freeSize - need >= MIN_SDCARD_SIZE
//}

/**
 * 获取普通下载目录
 */
fun getDownloadNormalFileDir(): File {
    val dirFile = downloadDir
    return if (dirFile.exists()) {
        if (dirFile.isDirectory) {
            dirFile
        } else {
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
        }
    } else {
        dirFile.mkdirs()
        dirFile
    }
}

/**
 * 获取DCIM文件夹下的应用存储目录
 */
//fun getOutsideSaveDir(): File {
//    val dir = File(getDCIMDirectory(), "一刻相册")
//    return if (dir.exists()) {
//        if (dir.isDirectory) {
//            dir
//        } else {
//            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
//        }
//    } else {
//        dir.mkdirs()
//        dir
//    }
//}

/**
 * 获取模板下载目录
 */
fun getDownloadTemplateDir(context: Context): File {
    return File(context.filesDir, TEMPLATE_DIR)
}

/**
 * 获取原图下载目录
 */
fun getDownloadOriginImageDir(context: Context): File {
    return File(context.cacheDir, ORIGINN_IMAGE_DIR)
}

/**
 * 获取P2P2下载日志目录
 */
fun getP2PDownloadLogDir(context: Context): File {
    return File(context.cacheDir, P2P_DOWNLOAD_LOG_DIR).apply {
        mkdirs()
    }
}

/**
 * 获取手动制作缓存目录
 */
fun getManualMakeProductCacheDir(context: Context): File {
    return File(context.filesDir, MANUAL_PRODUCT_DIR).apply {
        mkdirs()
    }
}

/**
 * 获取智能分类缓存目录
 */
fun getClassificationCacheDir(context: Context): File {
    return File(context.filesDir, CLASSIFICATION_DIR).apply {
        mkdirs()
    }
}


///**
// * 将文件扫描至媒体库
// */
//fun File.scan(context: Context, duration: Long? = 0, shootTimeMillis: Long = lastModified()) {
//    if (!exists()) {
//        return
//    }
//    val applicationContext = context.applicationContext
//    val time = if (shootTimeMillis == 0L) System.currentTimeMillis() else shootTimeMillis
//    if (isImage()) {
//        //手动入库
//        val orientation = when (getSimpleImageExifInfo()?.orientation) {
//            ExifInterface.ORIENTATION_ROTATE_90 -> ROTATE_90
//            ExifInterface.ORIENTATION_ROTATE_180 -> ROTATE_180
//            ExifInterface.ORIENTATION_ROTATE_270 -> ROTATE_270
//            else -> 0
//        }
//        val options = BitmapFactory.Options()
//        options.inJustDecodeBounds = true
//        BitmapFactory.decodeFile(absolutePath, options)
//        "${options.outWidth} - ${options.outWidth} ".d()
//        applicationContext.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, ContentValues {
//            MediaStore.MediaColumns.DATA - absolutePath
//            MediaStore.MediaColumns.SIZE - length()
//            MediaStore.MediaColumns.MIME_TYPE - mineType()
//            MediaStore.Images.Media.BUCKET_DISPLAY_NAME - "image"
//            MediaStore.Images.Media.WIDTH - options.outWidth
//            MediaStore.Images.Media.HEIGHT - options.outHeight
//            MediaStore.Images.Media.DATE_TAKEN - time
//            MediaStore.MediaColumns.DATE_ADDED - time
//            MediaStore.Images.Media.ORIENTATION - orientation
//        })
//    } else if (isVideo()) {
//        val durationMsLocal = if (duration == null || duration == 0L) durationMs else duration
//        //手动入库
//        applicationContext.contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, ContentValues {
//            MediaStore.MediaColumns.DATA - absolutePath
//            MediaStore.MediaColumns.SIZE - length()
//            MediaStore.MediaColumns.MIME_TYPE - mineType()
//            MediaStore.Images.Media.BUCKET_DISPLAY_NAME - "video"
//            MediaStore.Video.Media.DURATION - durationMsLocal
//            MediaStore.Images.Media.DATE_TAKEN - time
//            MediaStore.MediaColumns.DATE_ADDED - time
//        })
//    }
//    //系统自动扫描
//    MediaScannerConnection.scanFile(applicationContext, arrayOf(absolutePath), null, null)
//}
//
//val File.durationMs: Long
//    get() = try {
//        takeIf { it.exists() }?.getVideoExifJson(null)?.let {
//            JSONObject(it).getJSONObject(JSON_OBJECT_NAME_FILE).getLong(JSON_OBJECT_NAME_DURATION)
//        } ?: 0
//    } catch (e: Exception) {
//        e.printStackTraceWhenLog()
//        0L
//    }

/**
 * 将asset文件复制成制定路径文件
 */
fun copyAssetsFile(context: Context,
                   assetsPath: String,
                   destFilePath: String
): File {
    val destFile = File(destFilePath)
    if (destFile.exists()) {
//        log { "copyAssetsFile:result(${destFile.exists()},${destFile.absolutePath},${destFile.length()})" }
        return destFile
    }
    var fis: InputStream? = null
    var fos: OutputStream? = null
    try {
        val destFileParentFile = destFile.parentFile
        destFileParentFile?.mkdirs()
        fis = context.applicationContext.assets.open(assetsPath)
        fos = FileOutputStream(destFile)
        fis.copyTo(fos)
        fos.flush()
//        log { "copyAssetsFile:result(${destFile.exists()},${destFile.absolutePath},${destFile.length()})" }
    } catch (e: FileNotFoundException) {
//        log { "copyAssetsFile:FileNotFoundException" }
    } catch (e: IOException) {
//        log { "copyAssetsFile:IOException" }
    } finally {
        try {
            fis?.close()
        } catch (e: IOException) {
//            log { "copyAssetsFile:IOException" }
        }
        try {
            fos?.close()
        } catch (e: IOException) {
//            log { "copyAssetsFile:IOException" }
        }
    }
    return destFile
}