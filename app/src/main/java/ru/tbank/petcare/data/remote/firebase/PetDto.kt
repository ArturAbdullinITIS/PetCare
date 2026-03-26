package ru.tbank.petcare.data.remote.firebase

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp
import ru.tbank.petcare.domain.model.Gender
import java.util.Date

data class PetDto(
    @DocumentId
    var id: String = "",

    @PropertyName("name")
    val name: String = "",

    @PropertyName("gender")
    val gender: String = "",

    @PropertyName("breed")
    val breed: String = "",

    @ServerTimestamp
    @PropertyName("date_of_birth")
    val dateOfBirth: Long = 0L,

    @PropertyName("game_score")
    val gameScore: Int = 0,

    @PropertyName("icon_status")
    val iconStatus: String = "",

    @PropertyName("is_public")
    val isPublic: Boolean = false,

    @PropertyName("notes")
    val notes: String = "",

    @PropertyName("owner_id")
    val ownerId: String = "",

    @PropertyName("photo_url")
    val photoUrl: String = "",

    @PropertyName("weight")
    val weight: Double = 0.0

)