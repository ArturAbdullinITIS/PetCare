package ru.tbank.petcare.data.remote.network.deepl
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class DeeplTranslation(
    @SerializedName("detected_source_language")
    val detectedSourceLanguage: String,
    @SerializedName("text")
    val text: String
)
