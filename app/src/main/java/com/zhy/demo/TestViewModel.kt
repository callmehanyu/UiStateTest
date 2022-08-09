package com.zhy.demo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import com.zhy.unittest.TestUiStateCollection1

class TestViewModel(application: Application) : AndroidViewModel(application) {

    val uiState: MediatorLiveData<TestUiStateCollection1> by lazy {
        val livedata = MediatorLiveData<TestUiStateCollection1>()
        livedata.value = TestUiStateCollection1()
        livedata
    }

}
