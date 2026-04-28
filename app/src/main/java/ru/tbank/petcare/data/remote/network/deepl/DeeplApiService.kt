package ru.tbank.petcare.data.remote.network.deepl

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface DeeplApiService {

    @FormUrlEncoded
    @POST("v2/translate")
    suspend fun translate(
        @Field("text") text: String,
        @Field("source_lang") sourceLang: String,
        @Field("target_lang") targetLang: String
    ): DeeplTranslationResponse
}
