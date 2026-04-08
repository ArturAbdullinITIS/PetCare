package ru.tbank.petcare.data.local

import androidx.room.Entity

@Entity("pets")
data class PetDbModel(
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
