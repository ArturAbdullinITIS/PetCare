package ru.tbank.petcare.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.tbank.petcare.data.repository.PetsRepositoryImpl
import ru.tbank.petcare.domain.repository.PetsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface FirebaseModule {
    @Binds
    @Singleton
    fun bindPetsRepository(
        impl: PetsRepositoryImpl
    ): PetsRepository

    companion object {

        @Provides
        @Singleton
        fun provideFirebaseAuth(): FirebaseAuth {
            return FirebaseAuth.getInstance()
        }

        @Provides
        @Singleton
        fun provideFirebaseFirestore(): FirebaseFirestore {
            return FirebaseFirestore.getInstance()
        }
    }
}
