package ru.tbank.petcare.data.remote.network.deepl

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class DeeplTranslationResponse(
    @SerializedName("translations")
    val translations: List<DeeplTranslation>
)
