package com.zhy.demo

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.zhy.R
import com.zhy.collection.writeMock
import com.zhy.unittest.TestUiStateCollection1
import com.zhy.util.getViewModel

class TestActivity : AppCompatActivity() {

    private val viewModel: TestViewModel by lazy { getViewModel() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        findViewById<TextView>(R.id.tv_1).setOnClickListener {
//            val tv1Cnt = viewModel.uiState.value?.tv1Cnt ?: 0
//            viewModel.uiState.value = viewModel.uiState.value?.copy(tv1Cnt = tv1Cnt + 1)
        }

        viewModel.uiState.observe(this) {
            writeMock(this, it)
            handle(it)
        }

    }

    fun handle(uiState: TestUiStateCollection1) {
        findViewById<TextView>(R.id.tv_1).text = uiState.myEnum.name
        findViewById<TextView>(R.id.tv_2).text = uiState.hisEnum.name
    }

}