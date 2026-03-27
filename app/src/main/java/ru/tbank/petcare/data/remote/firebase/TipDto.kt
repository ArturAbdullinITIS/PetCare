package ru.tbank.petcare.data.remote.firebase

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

data class TipDto(
    @DocumentId
    var id: String = "",
    @PropertyName("text")
    var text: String = ""
)