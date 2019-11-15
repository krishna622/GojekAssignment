package com.krishna.gojekassignment

import android.app.Application
import com.krishna.gojekassignment.viewmodel.TrendingViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApplication)
            modules(viewModelModule)
        }
    }

    val viewModelModule = module {
        viewModel {
            TrendingViewModel(get())
        }
    }
}