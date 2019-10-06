package com.krishna.gojekassignment.repository.remote
import android.app.Application
import android.content.Context
import android.util.Log
import com.krishna.gojekassignment.BuildConfig
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.xml.datatype.DatatypeConstants.DAYS
import okhttp3.CacheControl
import java.io.File
import java.util.concurrent.TimeUnit


object RetrofitFactory{
    private lateinit var mCachedOkHttpClient: OkHttpClient
    val HEADER_CACHE_CONTROL = "Cache-Control"
    val HEADER_PRAGMA = "Pragma"
    private var mCache: Cache? = null
    private var mCachedRetrofit: Retrofit? = null
    private val authInterceptor = Interceptor {chain->
        val newUrl = chain.request().url()
                .newBuilder()
                .addQueryParameter("apikey","e75ea8d5")
                .build()

        val newRequest = chain.request()
                .newBuilder()
                .url(newUrl)
                .build()

        chain.proceed(newRequest)
    }

    private val loggingInterceptor =  HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }


    private val provideForcedOfflineCacheInterceptor = Interceptor { chain->
        var request = chain.request()

        val cacheControl = CacheControl.Builder()
            .maxStale(5, TimeUnit.MINUTES)
            .build()

        request = request.newBuilder()
            .removeHeader(HEADER_PRAGMA)
            .removeHeader(HEADER_CACHE_CONTROL)
            .cacheControl(cacheControl)
            .build()
        chain.proceed(request);
    }
    //Not logging the authkey if not debug
    private val client =
        if(BuildConfig.DEBUG){
             OkHttpClient().newBuilder()
                    .addInterceptor(authInterceptor)
                    .addInterceptor(loggingInterceptor)
                    .build()
        }else{
            OkHttpClient().newBuilder()
                    .addInterceptor(loggingInterceptor)
                    .addInterceptor(authInterceptor)
                    .build()
        }





    fun retrofit() : Retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(ConstUrl.baseUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

    fun getCachedRetrofit(application: Context): Retrofit? {
        if (mCachedRetrofit == null) {
            val httpClient = OkHttpClient.Builder()
                // Add all interceptors you want (headers, URL, logging)
                .addInterceptor(provideForcedOfflineCacheInterceptor)
                .cache(provideCache(application))

            mCachedOkHttpClient = httpClient.build()

            mCachedRetrofit = Retrofit.Builder()
                .baseUrl(ConstUrl.baseUrl)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .client(mCachedOkHttpClient)
                .build()
        }

        return mCachedRetrofit
    }
    private fun provideCache(applicationContext: Context): Cache? {
        if (mCache == null) {
            try {
                mCache = Cache(
                    File(applicationContext.getCacheDir(), "http-cache"),
                    10 * 1024 * 1024
                ) // 10 MB
            } catch (e: Exception) {
                Log.e("cache", "Could not create Cache!")
            }

        }

        return mCache
    }

//    val gitRepoApi : GitRepoApi = retrofit().create(GitRepoApi::class.java)

}