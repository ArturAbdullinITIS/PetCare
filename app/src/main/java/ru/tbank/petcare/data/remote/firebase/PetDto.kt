package ru.tbank.petcare.data.remote.firebase

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

data class PetDto(
    @DocumentId
    var id: String = "",

    @get:PropertyName("name")
    @set:PropertyName("name")
    var name: String = "",

    @get:PropertyName("gender")
    @set:PropertyName("gender")
    var gender: String = "",

    @get:PropertyName("breed")
    @set:PropertyName("breed")
    var breed: String = "",

    @get:PropertyName("date_of_birth")
    @set:PropertyName("date_of_birth")
    var dateOfBirth: Timestamp? = null,

    @get:PropertyName("game_score")
    @set:PropertyName("game_score")
    var gameScore: Int = 0,

    @get:PropertyName("icon_status")
    @set:PropertyName("icon_status")
    var iconStatus: String = "",

    @get:PropertyName("is_public")
    @set:PropertyName("is_public")
    var isPublic: Boolean = false,

    @get:PropertyName("note")
    @set:PropertyName("note")
    var note: String = "",

    @get:PropertyName("owner_id")
    @set:PropertyName("owner_id")
    var ownerId: String = "",

    @get:PropertyName("photo_url")
    @set:PropertyName("photo_url")
    var photoUrl: String = "",

    @get:PropertyName("weight")
    @set:PropertyName("weight")
    var weight: Double = 0.0,

    @get:PropertyName("last_activity")
    @set:PropertyName("last_activity")
    var lastActivity: LastActivityDto? = null
)
