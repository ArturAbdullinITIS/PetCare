package ru.tbank.petcare.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import ru.tbank.petcare.utils.ResourceProvider
import ru.tbank.petcare.utils.ResourceProviderImpl

@Module
@InstallIn(SingletonComponent::class)
interface CoreModule {

    @Binds
    @Singleton
    fun bindResourceProvider(
        impl: ResourceProviderImpl
    ): ResourceProvider

    companion object {
        @Provides
        @Singleton
        fun provideResourceProvider(@ApplicationContext context: Context): ResourceProviderImpl {
            return ResourceProviderImpl(context)
        }
    }
}
