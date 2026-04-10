package ru.tbank.petcare.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("pets")
data class PetDbModel(
    @PrimaryKey
    val id: String,
    val name: String,
    val gender: String,
    val breed: String,
    val dateOfBirthMillis: Long?,
    val gameScore: Int,
    val iconStatus: String,
    val isPublic: Boolean,
    val note: String,
    val ownerId: String,
    val photoUrl: String,
    val weight: Double
)
