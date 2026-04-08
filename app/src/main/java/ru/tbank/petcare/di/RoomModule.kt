package ru.tbank.petcare.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import ru.tbank.petcare.data.local.Database
import ru.tbank.petcare.data.local.PetsDao

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Provides
    @Singleton
    fun provideRoomDatabase(
        @ApplicationContext context: Context
    ): Database {
        return Room.databaseBuilder(
            context = context,
            klass = Database::class.java,
            name = "pets.db",
        ).build()
    }

    @Provides
    @Singleton
    fun providePetsDao(
        db: Database
    ): PetsDao {
        return db.petsDao()
    }
}
