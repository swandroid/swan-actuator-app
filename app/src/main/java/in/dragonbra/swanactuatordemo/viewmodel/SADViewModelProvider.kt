package `in`.dragonbra.swanactuatordemo.viewmodel

import `in`.dragonbra.swanactuatordemo.ui.main.MainViewModel
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

class SADViewModelProvider(
        private val mainViewModel: MainViewModel
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>) = when (modelClass) {
        MainViewModel::class.java -> mainViewModel as T
        else -> throw IllegalArgumentException("Unknown view model type: ${modelClass.simpleName}")
    }
}