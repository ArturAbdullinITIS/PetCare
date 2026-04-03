package ru.tbank.petcare.data.remote.network.animals
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
@Serializable
data class AnimalDtoItem(
    @SerializedName("name")
    val name: String = "",
    @SerializedName("characteristics")
    val characteristics: Characteristics = Characteristics(),
    @SerializedName("locations")
    val locations: List<String> = listOf(),
)
