package ru.tbank.petcare.data.remote.firebase

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

data class UserDto(
    @DocumentId
    var id: String = "",

    @get:PropertyName("email")
    @set:PropertyName("email")
    var email: String = "",

    @get:PropertyName("first_name")
    @set:PropertyName("first_name")
    var firstName: String = "",

    @get:PropertyName("last_name")
    @set:PropertyName("last_name")
    var lastName: String = "",

    @get:PropertyName("photo_url")
    @set:PropertyName("photo_url")
    var photoUrl: String = ""
)
