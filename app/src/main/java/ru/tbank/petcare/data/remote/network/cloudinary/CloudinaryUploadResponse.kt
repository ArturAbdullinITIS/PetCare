package ru.tbank.petcare.data.remote.network.cloudinary

import com.google.gson.annotations.SerializedName

data class CloudinaryUploadResponse(
    @SerializedName("secure_url")
    val secureUrl: String
)
