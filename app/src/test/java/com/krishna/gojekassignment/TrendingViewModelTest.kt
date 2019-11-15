package com.krishna.gojekassignment
import androidx.lifecycle.MutableLiveData
import com.krishna.gojekassignment.repository.GitDataRepository
import com.krishna.gojekassignment.ui.model.TrendDataItem
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations

class TrendingViewModelTest {

    var gitDataRepository = mock<GitDataRepository>()


    @Before
    fun setUp(){
    }

    @Test
    fun getTrendingDataTest(){
        val liveData = MutableLiveData<List<TrendDataItem>>()
        whenever(gitDataRepository.getTrendingData()).thenReturn(liveData)
        Assert.assertNotNull(gitDataRepository.getTrendingData())
    }

    @Test
    fun refreshTrendingDataTest(){
        val liveData = MutableLiveData<List<TrendDataItem>>()
        whenever(gitDataRepository.getTrendingDataWithCacheEnable()).thenReturn(liveData)
        Assert.assertNotNull(gitDataRepository.getTrendingDataWithCacheEnable())
    }

    @Test
    fun getLoadingStatusTest(){
        val loadingComplete = MutableLiveData<Boolean>()
        whenever(gitDataRepository.getLoadingStatus()).thenReturn(loadingComplete)
        Assert.assertNotNull(gitDataRepository.getLoadingStatus())
    }

    @Test
    fun getIsDataNotAvailableTest(){
        val isDataNotAvailable = MutableLiveData<Boolean>()
        whenever(gitDataRepository.getIsDataNotAvailable()).thenReturn(isDataNotAvailable)
        Assert.assertNotNull(gitDataRepository.getIsDataNotAvailable())
    }
}