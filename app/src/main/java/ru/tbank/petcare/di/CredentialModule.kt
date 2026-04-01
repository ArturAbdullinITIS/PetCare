package ru.tbank.petcare.di

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import ru.tbank.petcare.R


@Module
@InstallIn(SingletonComponent::class)
interface CredentialModule {

    companion object {

        @Provides
        @Singleton
        fun provideCredentialManager(@ApplicationContext context: Context): CredentialManager {
            return CredentialManager.create(context)
        }

        @Provides
        @Singleton
        fun provideGetGoogleIdOption(@ApplicationContext context: Context): GetGoogleIdOption {
            return GetGoogleIdOption.Builder()
                .setServerClientId(context.getString(R.string.default_web_client_id))
                .setFilterByAuthorizedAccounts(false)
                .build()
        }

        @Provides
        @Singleton
        fun provideGetCredentialRequest(getGoogleIdOption: GetGoogleIdOption): GetCredentialRequest {
            return GetCredentialRequest.Builder()
                .addCredentialOption(getGoogleIdOption)
                .build()
        }
    }
}