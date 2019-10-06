package com.krishna.gojekassignment.repository
import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.krishna.gojekassignment.repository.remote.*
import com.krishna.gojekassignment.ui.model.TrendDataItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class GitDataRepository(val application: Application, val uiScope: CoroutineScope, val ioScope: CoroutineScope) {
    private val isLoading = MutableLiveData<Boolean>()
    private var remoteRepository: RemoteRepository

    init {
        remoteRepository = RemoteRepository()
    }
    fun getTrendingData() : MutableLiveData<List<TrendDataItem>>?{
           isLoading.value = true
           val trendDataItemlList : MutableLiveData<List<TrendDataItem>>? = MutableLiveData<List<TrendDataItem>>()
           ioScope.launch {
               val gitRepoApi : GitRepoApi? = RetrofitFactory.retrofit().create(GitRepoApi::class.java)
               val data = remoteRepository.safeApiCall(
                   call = { gitRepoApi!!.getGitRepoData("","").await()},
                   errorMessage = "Error Fetching Trending Repo"
               )
               uiScope.launch {
                   isLoading.value = false
                   trendDataItemlList?.value = data?.mapIndexed { index, trendDataModel -> convertModelToItem(trendDataModel) }
               }
           }
        return trendDataItemlList
    }

    private fun convertModelToItem(data: TrendDataModel) : TrendDataItem{
        return TrendDataItem(data.author, data.name, data.avatar, data.url, data.description, data.stars, data.forks, data.currentPeriodStars, data.language
        , data.languageColor)
    }

    fun isLoading() : MutableLiveData<Boolean>{
        return isLoading
    }
}