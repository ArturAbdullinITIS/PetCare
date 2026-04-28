package ru.tbank.petcare.di

import android.content.Context
import androidx.work.WorkManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.tbank.petcare.data.repository.ActivityRepositoryImpl
import ru.tbank.petcare.data.repository.AuthRepositoryImpl
import ru.tbank.petcare.data.repository.ConnectivityRepositoryImpl
import ru.tbank.petcare.data.repository.LocaleRepositoryImpl
import ru.tbank.petcare.data.repository.PetsRepositoryImpl
import ru.tbank.petcare.data.repository.SettingsRepositoryImpl
import ru.tbank.petcare.data.repository.TranslationRepositoryImpl
import ru.tbank.petcare.data.repository.UsersRepositoryImpl
import ru.tbank.petcare.domain.repository.ActivityRepository
import ru.tbank.petcare.domain.repository.AuthRepository
import ru.tbank.petcare.domain.repository.ConnectivityRepository
import ru.tbank.petcare.domain.repository.LocaleRepository
import ru.tbank.petcare.domain.repository.PetsRepository
import ru.tbank.petcare.domain.repository.SettingsRepository
import ru.tbank.petcare.domain.repository.TranslationRepository
import ru.tbank.petcare.domain.repository.UsersRepository
import ru.tbank.petcare.utils.ErrorParser
import ru.tbank.petcare.utils.ResourceProvider
import ru.tbank.petcare.utils.ResourceProviderImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface CoreModule {

    @Binds
    @Singleton
    fun bindActivityRepository(
        impl: ActivityRepositoryImpl
    ): ActivityRepository

    @Binds
    @Singleton
    fun bindResourceProvider(
        impl: ResourceProviderImpl
    ): ResourceProvider

    @Binds
    @Singleton
    fun bindPetsRepository(
        impl: PetsRepositoryImpl
    ): PetsRepository

    @Binds
    @Singleton
    fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    fun bindUserRepository(
        impl: UsersRepositoryImpl
    ): UsersRepository

    @Binds
    @Singleton
    fun bindSettingsRepository(
        impl: SettingsRepositoryImpl
    ): SettingsRepository

    @Binds
    @Singleton
    fun bindConnectivityRepository(
        impl: ConnectivityRepositoryImpl
    ): ConnectivityRepository

    @Binds
    @Singleton
    fun bindLocaleRepository(
        impl: LocaleRepositoryImpl
    ): LocaleRepository

    @Binds
    @Singleton
    fun bindTranslationRepository(
        impl: TranslationRepositoryImpl
    ): TranslationRepository

    companion object {

        @Provides
        @Singleton
        fun provideResourceProvider(@ApplicationContext context: Context): ResourceProviderImpl {
            return ResourceProviderImpl(context)
        }

        @Provides
        @Singleton
        fun provideErrorParser(
            resourceProvider: ResourceProvider
        ): ErrorParser {
            return ErrorParser(resourceProvider)
        }

        @Provides
        @Singleton
        fun provideWorkManager(
            @ApplicationContext context: Context
        ): WorkManager {
            return WorkManager.getInstance(context)
        }
    }
}
