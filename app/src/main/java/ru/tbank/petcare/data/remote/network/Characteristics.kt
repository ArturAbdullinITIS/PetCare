package ru.tbank.petcare.data.remote.network
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Characteristics(
    @SerialName("common_name")
    val commonName: String = "",
    @SerialName("diet")
    val diet: String = "",
    @SerialName("group")
    val group: String = "",
    @SerialName("lifespan")
    val lifespan: String = "",
    @SerialName("skin_type")
    val skinType: String = "",
    @SerialName("slogan")
    val slogan: String = "",
    @SerialName("weight")
    val weight: String = ""
)
