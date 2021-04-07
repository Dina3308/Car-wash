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
    fun provideRetrofit(
        client: OkHttpClient,
        converterFactory: GsonConverterFactory,
        @Named(TAG_BASE_URL) url: String
    ): Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(url)
        .addConverterFactory(converterFactory)
        .build()

    @Provides
    @Singleton
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

    companion object {
        private const val TAG_LOGGING = "tag_logging"
        private const val TAG_AUTH = "tag_auth"
        private const val TAG_BASE_URL = "tag_base_url"
        private const val TAG_LANG = "tag_lang"
    }
}
