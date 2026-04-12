package ru.tbank.petcare.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PetsDao {

    @Query("SELECT * FROM pets WHERE ownerId = :ownerId")
    fun observePetsByOwner(ownerId: String): Flow<List<PetDbModel>>

    @Query("SELECT * FROM pets WHERE id = :petId LIMIT 1")
    fun observePetById(petId: String): Flow<PetDbModel?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(pets: List<PetDbModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(pet: PetDbModel)

    @Query("DELETE FROM pets WHERE ownerId = :ownerId")
    suspend fun deleteByOwner(ownerId: String)

    @Query("DELETE FROM pets WHERE id = :petId")
    suspend fun deleteById(petId: String)

    @Query("DELETE FROM pets")
    suspend fun clearAll()

    @Query("SELECT * FROM pets WHERE id = :petId LIMIT 1")
    suspend fun getPetByIdOnce(petId: String): PetDbModel?
}
