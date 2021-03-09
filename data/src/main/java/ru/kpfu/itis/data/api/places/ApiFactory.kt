package ru.kpfu.itis.data.api.places

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import retrofit2.converter.gson.GsonConverterFactory
import ru.kpfu.itis.data.BuildConfig

import ru.kpfu.itis.data.api.LoggingInterceptor


object PlacesApiFactory {

    private const val QUERY_API_KEY = "key"
    private const val QUERY_LANG = "language"

    private val apiKeyInterceptor = Interceptor { chain ->
        val original = chain.request()
        original.url().newBuilder()
                .addQueryParameter(QUERY_API_KEY, BuildConfig.API_KEY)
                .build()
                .let {
                    chain.proceed(
                            original.newBuilder().url(it).build()
                    )
                }
    }
    private val languageInterceptor = Interceptor { chain ->
        val original = chain.request()
        original.url().newBuilder()
            .addQueryParameter(QUERY_LANG, "ru")
            .build()
            .let {
                chain.proceed(
                    original.newBuilder().url(it).build()
                )
            }
    }

    private val client by lazy {
        OkHttpClient.Builder()
                .addInterceptor(apiKeyInterceptor)
                .addInterceptor(LoggingInterceptor())
                .addInterceptor(languageInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
                .client(client)
                .baseUrl(BuildConfig.API_ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    val placesService: PlacesService by lazy {
        retrofit.create(PlacesService::class.java)
    }
}