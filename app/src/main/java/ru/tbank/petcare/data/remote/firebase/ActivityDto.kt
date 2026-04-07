package ru.tbank.petcare.data.remote.firebase

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

data class ActivityDto(
    @DocumentId
    var id: String = "",

    @get:PropertyName("is_reminder")
    @set:PropertyName("is_reminder")
    var isReminder: Boolean = false,

    @get:PropertyName("notes")
    @set:PropertyName("notes")
    var notes: String = "",

    @get:PropertyName("type")
    @set:PropertyName("type")
    var type: String = "",

    @get:PropertyName("date")
    @set:PropertyName("date")
    var date: Timestamp? = null,

    @get:PropertyName("details")
    @set:PropertyName("details")
    var details: Map<String, Any>? = null

)
