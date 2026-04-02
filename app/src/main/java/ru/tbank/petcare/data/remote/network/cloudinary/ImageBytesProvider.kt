package ru.tbank.petcare.data.remote.network.cloudinary

import android.net.Uri

interface ImageBytesProvider {
    suspend fun readBytes(uri: Uri): ByteArray
    fun getMimeType(uri: Uri): String
}
