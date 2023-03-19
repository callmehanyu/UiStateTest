package com.zhy.demo

import android.Manifest
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.base.screenshot.listener.ScreenshotListener
import com.base.screenshot.service.ScreenShortRecordService
import com.base.screenshot.utils.MediaPlayerHelper
import com.zhy.R
import com.zhy.bitmap.compressToFile
import com.zhy.file.getUiTestDirectory
import com.zhy.unittest.TestUiState
import com.zhy.util.getViewModel

class TestActivity : AppCompatActivity() {

    //录屏
    private val MIRROR_CODE = 2
    //截屏
    private val CAPTURE_CODE = 1

    private val mediaManager: MediaProjectionManager by lazy {
        getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
    }

    //截屏、录屏服务
    private var mScreenShortService: ScreenShortRecordService? = null
    private val viewModel: TestViewModel by lazy { getViewModel() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        com.base.perm.checkPermission(this)
        checkPermission()
        findViewById<TextView>(R.id.tv_1).setOnClickListener {
//            screenShotByShell(" /sdcard/sreenshot1.png")
//            val mScreenBitmap1: Bitmap = SurfaceControl.screenshot(Rect(), 0, 0, Surface.ROTATION_0)

//            val mScreenBitmap: Bitmap? = try {
//                val demo = Class.forName("android.view.SurfaceControl")
//                val method: Method = demo.getDeclaredMethod(
//                    "screenshot",
//                    Rect::class.javaPrimitiveType,
//                    Int::class.javaPrimitiveType,
//                    Int::class.javaPrimitiveType,
//                    Int::class.javaPrimitiveType,
//                )
//                method.invoke(null, 0, 0, Surface.ROTATION_0) as Bitmap
//            } catch (e: Exception) {
//                e.printStackTrace()
//                null
//            }
            capture()
//            screenshot()
//            val tv1Cnt = viewModel.uiState.value?.tv1Cnt ?: 0
//            viewModel.uiState.value = viewModel.uiState.value?.copy(tv1Cnt = tv1Cnt + 1)
        }

        viewModel.uiState.observe(this) {
            updateView(it)
        }

    }

//    @RunUiStateTest
    fun updateView(uiState: TestUiState) {
        findViewById<TextView>(R.id.tv_1).text = uiState.myEnum.name
        findViewById<TextView>(R.id.tv_2).text = uiState.hisEnum.name
        findViewById<ImageView>(R.id.iv_1).setImageBitmap(uiState.bmp)
    }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, iBinder: IBinder?) {
            if (iBinder is ScreenShortRecordService.ScreenShortBinder) {
                //截屏
                mScreenShortService = iBinder.getService()

            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            //no-op
        }
    }

    override fun onStart() {
        super.onStart()
        // 绑定服务
        Intent(this, ScreenShortRecordService::class.java)
            .also { intent ->
                bindService(intent, connection, Context.BIND_AUTO_CREATE)
            }
    }

    override fun onStop() {
        super.onStop()
        //解绑服务
        unbindService(connection)
    }

    override fun onDestroy() {
        super.onDestroy()
        MediaPlayerHelper.release()
    }

    //截屏点击事件
    fun capture() {
        mScreenShortService?.let {
            //开始截屏
            if (savedIntent == null) {
                mediaManager.createScreenCaptureIntent().apply {
                    startActivityForResult(this, CAPTURE_CODE)
                }
            } else {
                mScreenShortService?.startShort(savedIntent!!, object : ScreenshotListener {
                    override suspend fun onScreenSuc(bitmap: Bitmap) {
                        bitmap.compressToFile(
                            getUiTestDirectory().absolutePath + "/com_zhy_demo_TestActivity_screenShot_index.jpg"
                        )
                    }
                })
            }

        }
    }

    var savedIntent: Intent? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                //截屏
                CAPTURE_CODE -> {
                    savedIntent = data
                    data?.let {
                        mScreenShortService?.startShort(it, object : ScreenshotListener {
                            override suspend fun onScreenSuc(bitmap: Bitmap) {
                                bitmap.compressToFile(
                                    getUiTestDirectory().absolutePath + "/com_zhy_demo_TestActivity_screenShot_index.jpg"
                                )
                            }
                        })
                    }
                }
                //录屏
                MIRROR_CODE -> {
                    //开始录制
//                    data?.let {
//                        mScreenShortService?.startRecorder(path, fileName, it)
//                    }
                }
            }
        }
    }


    //权限检查和申请
    fun checkPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val checkSelfPermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            )
            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.RECORD_AUDIO),
                    200
                );
                return false
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && requestCode == 200) {
            var isGranted = true
            for (result in grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    isGranted = false
                }
            }
            if (isGranted) {
                //开始录制
//                startRecordScreen()
            } else {
                Toast.makeText(this, "请先授权", Toast.LENGTH_SHORT).show()
            }
        }
    }

}