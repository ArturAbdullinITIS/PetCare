package ru.tbank.petcare.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.tbank.petcare.BuildConfig
import ru.tbank.petcare.data.remote.network.animals.AnimalsApiService
import ru.tbank.petcare.data.remote.network.cloudinary.CloudinaryApiService
import ru.tbank.petcare.data.remote.network.cloudinary.ImageBytesProvider
import ru.tbank.petcare.data.remote.network.cloudinary.ImageBytesProviderImpl
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NetworkModule {

    @Binds
    @Singleton
    fun bindImageBytesProvider(
        impl: ImageBytesProviderImpl
    ): ImageBytesProvider

    companion object {
        private const val TIMEOUT_SECONDS = 30L
        private const val ANIMALS_HEADER_KEY = "X-Api-Key"
        private const val ANIMALS_QUAL = "animals"
        private const val CLOUDINARY_QUAL = "cloudinary"

        @Provides
        @Singleton
        fun provideOkHttpClient(): OkHttpClient {
            return OkHttpClient.Builder()
                .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .addInterceptor { chain ->
                    val request = chain.request()
                    val url = request.url
                    val baseHost = BuildConfig.BASE_URL_ANIMALS.toHttpUrl().host
                    val isAnimalsHost = url.host == baseHost

                    val newRequest = if (isAnimalsHost) {
                        request.newBuilder()
                            .header(ANIMALS_HEADER_KEY, BuildConfig.API_KEY_ANIMALS)
                            .build()
                    } else {
                        request
                    }
                    chain.proceed(newRequest)
                }
                .addInterceptor(
                    HttpLoggingInterceptor().apply {
                        level = if (BuildConfig.DEBUG) {
                            HttpLoggingInterceptor.Level.BODY
                        } else {
                            HttpLoggingInterceptor.Level.NONE
                        }
                    }
                )
                .build()
        }

        @Provides
        @Singleton
        @Named(ANIMALS_QUAL)
        fun provideAnimalsRetrofit(
            okHttpClient: OkHttpClient
        ): Retrofit {
            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BuildConfig.BASE_URL_ANIMALS)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        @Provides
        @Singleton
        @Named(CLOUDINARY_QUAL)
        fun provideCloudinaryRetrofit(
            okHttpClient: OkHttpClient
        ): Retrofit {
            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BuildConfig.BASE_URL_CLOUDINARY)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        @Provides
        @Singleton
        fun provideAnimalsApiService(
            @Named(ANIMALS_QUAL) retrofit: Retrofit
        ): AnimalsApiService {
            return retrofit.create(AnimalsApiService::class.java)
        }

        @Provides
        @Singleton
        fun provideCloudinaryApiService(
            @Named(CLOUDINARY_QUAL) retrofit: Retrofit
        ): CloudinaryApiService {
            return retrofit.create(CloudinaryApiService::class.java)
        }
    }
}
