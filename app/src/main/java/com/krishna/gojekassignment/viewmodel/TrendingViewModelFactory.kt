package com.krishna.gojekassignment.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

class TrendingViewModelFactory(val application: Application) : ViewModelProvider.Factory{


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TrendingViewModel(application) as T
    }
}