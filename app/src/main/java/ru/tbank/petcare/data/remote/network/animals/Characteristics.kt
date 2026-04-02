package ru.tbank.petcare.data.remote.network.animals
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Characteristics(
    @SerializedName("common_name")
    val commonName: String = "",
    @SerializedName("diet")
    val diet: String = "",
    @SerializedName("group")
    val group: String = "",
    @SerializedName("lifespan")
    val lifespan: String = "",
    @SerializedName("skin_type")
    val skinType: String = "",
    @SerializedName("slogan")
    val slogan: String = "",
    @SerializedName("weight")
    val weight: String = ""
)
