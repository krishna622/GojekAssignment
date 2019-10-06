package com.krishna.gojekassignment.repository.remote
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface GitRepoApi {//QueryMap params : Map<String, String>
    @GET("repositories")
    fun getGitRepoData(@Query("language")  language : String, @Query("since") since : String) : Deferred<Response<List<TrendDataModel>>>
}