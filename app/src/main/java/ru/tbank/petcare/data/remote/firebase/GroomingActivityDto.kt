package ru.tbank.petcare.data.remote.firebase

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

data class GroomingActivityDto(
    @DocumentId
    var id: String = "",

    @get:PropertyName("activity_id")
    @set:PropertyName("activity_id")
    var activityId: String = "",

    @get:PropertyName("cost")
    @set:PropertyName("cost")
    var cost: Double = 0.0,

    @get:PropertyName("duration_minutes")
    @set:PropertyName("duration_minutes")
    var durationMinutes: Int = 0,

    @get:PropertyName("procedure_type")
    @set:PropertyName("procedure_type")
    var procedureType: String = ""
)
