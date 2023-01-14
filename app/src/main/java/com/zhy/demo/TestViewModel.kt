package com.zhy.demo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import com.zhy.unittest.TestUiStateCollection

class TestViewModel(application: Application) : AndroidViewModel(application) {

    val uiState: MediatorLiveData<TestUiStateCollection> by lazy {
        val livedata = MediatorLiveData<TestUiStateCollection>()
        livedata.value = TestUiStateCollection()
        livedata
    }

}
