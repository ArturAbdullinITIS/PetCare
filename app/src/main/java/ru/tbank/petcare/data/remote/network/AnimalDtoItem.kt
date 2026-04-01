package ru.tbank.petcare.data.remote.network
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
data class AnimalDtoItem(
    @SerialName("characteristics")
    val characteristics: Characteristics = Characteristics(),
    @SerialName("locations")
    val locations: List<String> = listOf(),
)
