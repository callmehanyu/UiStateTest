package com.zhy.demo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import com.zhy.unittest.TestUiState

class TestViewModel(application: Application) : AndroidViewModel(application) {

    val uiState: MediatorLiveData<TestUiState> by lazy {
        val livedata = MediatorLiveData<TestUiState>()
        livedata.value = TestUiState()
        livedata
    }

}
