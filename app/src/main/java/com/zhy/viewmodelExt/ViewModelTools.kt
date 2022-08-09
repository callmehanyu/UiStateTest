package com.zhy.viewmodelExt

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * activity获取viewModel的方法
 */
inline fun <reified T : ViewModel> FragmentActivity.getViewModel(): T {
    return ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application))[T::class.java]
}

/**
 * fragment获取viewModel的方法
 */
inline fun <reified T : ViewModel> Fragment.getViewModel(): T {
    return ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(requireActivity().application))[T::class.java]
}