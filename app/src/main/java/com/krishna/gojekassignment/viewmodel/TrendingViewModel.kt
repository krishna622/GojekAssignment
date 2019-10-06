package com.krishna.gojekassignment.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.krishna.gojekassignment.repository.GitDataRepository
import com.krishna.gojekassignment.ui.model.TrendDataItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class TrendingViewModel(private var application: Application) : ViewModel() {
    private var gitDataRepository: GitDataRepository
    private var trendingDataList: LiveData<List<TrendDataItem>>? = null
    private val viewModelJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val ioScope =  CoroutineScope(Dispatchers.IO + viewModelJob)

    init {
       gitDataRepository = GitDataRepository(application, uiScope, ioScope)
    }
    fun getTrendingData() : LiveData<List<TrendDataItem>>? {
       if(trendingDataList == null)
          trendingDataList = gitDataRepository.getTrendingData()
       return trendingDataList
    }

    fun isLoading() : MutableLiveData<Boolean>{
        return gitDataRepository.isLoading()
    }
}