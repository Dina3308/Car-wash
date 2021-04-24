package ru.kpfu.itis.data.di

import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.kpfu.itis.data.BuildConfig
import javax.inject.Named
import javax.inject.Singleton

@Module
class NetworkModule {
    @Provides
    @Singleton
    @Named(TAG_RETROFIT_PLACES)
    fun provideRetrofit(
        @Named(TAG_CLIENT_PLACES)client: OkHttpClient,
        converterFactory: GsonConverterFactory,
        @Named(TAG_BASE_URL) url: String
    ): Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(url)
        .addConverterFactory(converterFactory)
        .build()

    @Provides
    @Singleton
    @Named(TAG_RETROFIT_WEATHER)
    fun provideRetrofitWeather(
        @Named(TAG_CLIENT_WEATHER)client: OkHttpClient,
        converterFactory: GsonConverterFactory,
        @Named(TAG_BASE_URL_WEATHER) url: String
    ): Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(url)
        .addConverterFactory(converterFactory)
        .build()

    @Provides
    @Singleton
    @Named(TAG_CLIENT_PLACES)
    fun provideClient(
        @Named(TAG_AUTH) authInterceptor: Interceptor,
        @Named(TAG_LOGGING) loggingInterceptor: Interceptor,
        @Named(TAG_LANG) langInterceptor: Interceptor
    ) = OkHttpClient().newBuilder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .addInterceptor(langInterceptor)
        .build()

    @Provides
    @Singleton
    @Named(TAG_CLIENT_WEATHER)
    fun provideClientWeather(
        @Named(TAG_AUTH_WEATHER) authInterceptor: Interceptor,
        @Named(TAG_METRIC) metricInterceptor: Interceptor,
        @Named(TAG_LOGGING) loggingInterceptor: Interceptor,
        @Named(TAG_LANG_WEATHER) langInterceptor: Interceptor
    ) = OkHttpClient().newBuilder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .addInterceptor(langInterceptor)
        .addInterceptor(metricInterceptor)
        .build()

    @Provides
    @Singleton
    @Named(TAG_AUTH)
    fun provideAuthInterceptor(): Interceptor = Interceptor { chain ->
        val newUrl = chain.request().url().newBuilder()
            .addQueryParameter("key", BuildConfig.API_KEY)
            .build()

        val newRequest = chain.request().newBuilder().url(newUrl).build()
        chain.proceed(newRequest)
    }

    @Provides
    @Singleton
    @Named(TAG_AUTH_WEATHER)
    fun provideAuthWeatherInterceptor(): Interceptor = Interceptor { chain ->
        val newUrl = chain.request().url().newBuilder()
            .addQueryParameter("appid", BuildConfig.API_KEY_WEATHER)
            .build()

        val newRequest = chain.request().newBuilder().url(newUrl).build()
        chain.proceed(newRequest)
    }

    @Provides
    @Singleton
    @Named(TAG_METRIC)
    fun provideMetricInterceptor(): Interceptor = Interceptor { chain ->
        val newUrl = chain.request().url().newBuilder()
            .addQueryParameter("units", "metric")
            .build()

        val newRequest = chain.request().newBuilder().url(newUrl).build()
        chain.proceed(newRequest)
    }

    @Provides
    @Singleton
    @Named(TAG_LANG)
    fun provideLangInterceptor(): Interceptor = Interceptor { chain ->
        val newUrl = chain.request().url().newBuilder()
            .addQueryParameter("language", "ru")
            .build()

        val newRequest = chain.request().newBuilder().url(newUrl).build()
        chain.proceed(newRequest)
    }

    @Provides
    @Singleton
    @Named(TAG_LANG_WEATHER)
    fun provideLangWeatherInterceptor(): Interceptor = Interceptor { chain ->
        val newUrl = chain.request().url().newBuilder()
            .addQueryParameter("lang", "ru")
            .build()

        val newRequest = chain.request().newBuilder().url(newUrl).build()
        chain.proceed(newRequest)
    }

    @Provides
    @Singleton
    @Named(TAG_LOGGING)
    fun provideLoggingInterceptor(): Interceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    @Singleton
    fun provideConvertFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    @Singleton
    @Named(TAG_BASE_URL)
    fun provideBaseURL(): String = BuildConfig.API_ENDPOINT

    @Provides
    @Singleton
    @Named(TAG_BASE_URL_WEATHER)
    fun provideBaseURLWeather(): String = BuildConfig.API_ENDPOINT_WEATHER

    companion object {
        private const val TAG_LOGGING = "tag_logging"
        private const val TAG_AUTH = "tag_auth"
        private const val TAG_AUTH_WEATHER = "tag_auth_weather"
        private const val TAG_BASE_URL = "tag_base_url"
        private const val TAG_BASE_URL_WEATHER = "tag_base_url_weather"
        private const val TAG_LANG = "tag_lang"
        private const val TAG_LANG_WEATHER = "tag_lang_weather"
        private const val TAG_METRIC = "tag_metric"
        private const val TAG_CLIENT_PLACES = "tag_client_places"
        private const val TAG_CLIENT_WEATHER = "tag_client_weather"
        private const val TAG_RETROFIT_WEATHER = "tag_retrofit_weather"
        private const val TAG_RETROFIT_PLACES = "tag_retrofit_places"
    }
}
