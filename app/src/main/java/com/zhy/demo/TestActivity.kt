package com.zhy.demo

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.zhy.R
import com.zhy.unittest.TestUiStateCollection1
import com.zhy.viewmodelExt.getViewModel
import com.zhy.collection.writeMock

class TestActivity : AppCompatActivity() {

    private val viewModel: TestViewModel by lazy { getViewModel() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        checkPermission()

        findViewById<TextView>(R.id.tv_1).setOnClickListener {
            val tv1Cnt = viewModel.uiState.value?.tv1Cnt ?: 0
            viewModel.uiState.value = viewModel.uiState.value?.copy(tv1Cnt = tv1Cnt + 1)
        }

        viewModel.uiState.observe(this) {
            // todo 生成 uistate
//            writeMock(this, it, )
            handle(it)
        }

    }

    fun handle(uiState: TestUiStateCollection1) {
        findViewById<TextView>(R.id.tv_1).text = uiState.tv1Cnt.toString()
        findViewById<TextView>(R.id.tv_2).text = uiState.btn1String
    }

    private fun checkPermission() {
        try {
            val PERMISSIONS_STORAGE = arrayOf<String>(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            val permission = ActivityCompat.checkSelfPermission(
                this,
                "android.permission.WRITE_EXTERNAL_STORAGE"
            )
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 100)
            } else {
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }



}