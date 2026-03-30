package ru.tbank.petcare.domain.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Pet(
    val id: String = "",
    val name: String = "",
    val breed: String = "",
    val gender: Gender = Gender.UNKNOWN,
    val isPublic: Boolean = false,
    val note: String = "",
    val gameScore: Int = 0,
    val ownerId: String = "",
    val weight: Double = 0.0,
    val dateOfBirth: Long = 0L,
    val iconStatus: IconStatus = IconStatus.NONE,
    val photoUrl: String = ""
)

enum class IconStatus(name: String) {
    NONE(""),
    HEART("heart"),
    SPARKLES("sparkles"),
    STAR("star");


    companion object {
        fun getIconStatusFromValue(value: String): IconStatus {
            return IconStatus.entries.find { it.name == value } ?: NONE
        }
    }
}

enum class Gender(name: String) {
    MALE("male"),
    FEMALE("female"),
    UNKNOWN("");

    companion object {
        fun getGenderFromValue(value: String): Gender {
            return Gender.entries.find { it.name == value } ?: UNKNOWN
        }
    }
}