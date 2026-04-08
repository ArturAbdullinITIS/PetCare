package ru.tbank.petcare.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [PetDbModel::class],
    version = 1,
    exportSchema = false
)
abstract class Database : RoomDatabase() {
    abstract fun petsDao(): PetsDao
}
