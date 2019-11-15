package com.krishna.gojekassignment.repository

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.krishna.gojekassignment.repository.remote.*
import com.krishna.gojekassignment.ui.model.TrendDataItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.lang.Exception

class GitDataRepository(
    val application: Application,
    val uiScope: CoroutineScope,
    val ioScope: CoroutineScope
) {
    private val isLoadingComplete = MutableLiveData<Boolean>()
    private val isDataNotAvailable = MutableLiveData<Boolean>()
    private var remoteRepository: RemoteRepository

    init {
        remoteRepository = RemoteRepository()
    }

    fun getTrendingData(): MutableLiveData<List<TrendDataItem>>? {
        isLoadingComplete.value = true
        val trendDataItemlList: MutableLiveData<List<TrendDataItem>>? = MutableLiveData()
        ioScope.launch {
            val gitRepoApi: GitRepoApi? = RetrofitFactory.retrofit().create(GitRepoApi::class.java)
            val data = remoteRepository.safeApiCall(
                call = { gitRepoApi!!.getGitRepoData("", "").await() },
                errorMessage = "Error Fetching Trending Repo"
            )
            uiScope.launch {
                isLoadingComplete.value = false
                trendDataItemlList?.value =
                    data?.mapIndexed { index, trendDataModel -> convertModelToItem(trendDataModel) }
            }
        }
        return trendDataItemlList
    }

    fun getTrendingDataWithCacheEnable(): MutableLiveData<List<TrendDataItem>>? {

        val trendDataItemlList: MutableLiveData<List<TrendDataItem>>? = MutableLiveData()
        ioScope.launch {
            try {
                val gitRepoApi: GitRepoApi? =
                    RetrofitFactory.getCachedRetrofit(application.applicationContext)
                        ?.create(GitRepoApi::class.java)
                val data = remoteRepository.safeApiCall(
                    call = { gitRepoApi!!.getGitRepoData("", "").await() },
                    errorMessage = "Error Fetching Trending Repo"
                )
                uiScope.launch {
                    trendDataItemlList?.value =
                        data?.mapIndexed { index, trendDataModel ->
                            convertModelToItem(
                                trendDataModel
                            )
                        }
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
                uiScope.launch {
                    isDataNotAvailable.value = true
                }
            }

        }
        return trendDataItemlList
    }

    private fun convertModelToItem(data: TrendDataModel): TrendDataItem {
        return TrendDataItem(
            data.author,
            data.name,
            data.avatar,
            data.url,
            data.description,
            data.stars,
            data.forks,
            data.currentPeriodStars,
            data.language
            ,
            data.languageColor
        )
    }

    fun getLoadingStatus(): MutableLiveData<Boolean> {
        return isLoadingComplete
    }

    fun getIsDataNotAvailable(): MutableLiveData<Boolean> {
        return isDataNotAvailable
    }
}