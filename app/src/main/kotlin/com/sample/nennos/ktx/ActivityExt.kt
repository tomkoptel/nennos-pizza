@file:Suppress("UNCHECKED_CAST")

package com.sample.nennos.ktx

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

inline fun <reified VM : ViewModel> FragmentActivity.provideModel(crossinline init: () -> VM): VM {
    return ViewModelProvider(viewModelStore, object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return init.invoke() as T
        }
    }).get(VM::class.java)
}
