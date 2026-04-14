package ru.tbank.petcare.data.remote.firebase

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class LastActivityDto(
    @get:PropertyName("id")
    @set:PropertyName("id")
    var id: String = "",

    @get:PropertyName("type")
    @set:PropertyName("type")
    var type: String = "",

    @get:PropertyName("date")
    @set:PropertyName("date")
    var date: Timestamp? = null
)
