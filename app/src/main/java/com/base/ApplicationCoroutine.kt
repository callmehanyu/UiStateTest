package com.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/**
 * 全局范围的一个Scope
 * 主要提供给业务层用，UI层使用ViewModel的scope
 */
@Deprecated("不推荐使用, 协程应该都是通过ViewModel发出; 使用TaskSchedulerImpl.runOnNonUiThread或者Activity,Fragment的viewModelScope扩展 ")
val applicationScope by lazy { CoroutineScope(SupervisorJob() + Dispatchers.Main) }