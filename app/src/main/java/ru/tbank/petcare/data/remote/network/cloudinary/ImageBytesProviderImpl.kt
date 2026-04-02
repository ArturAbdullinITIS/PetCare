package ru.tbank.petcare.data.remote.network.cloudinary

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ImageBytesProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ImageBytesProvider {

    companion object {
        private const val DEFAULT_MIME_TYPE = "image/*"
    }

    override suspend fun readBytes(uri: Uri): ByteArray =
        withContext(Dispatchers.IO) {
            context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                ?: error("Can't open input stream for $uri")
        }

    override fun getMimeType(uri: Uri): String =
        context.contentResolver.getType(uri) ?: DEFAULT_MIME_TYPE
}
