package ru.tbank.petcare.data.remote.network

import retrofit2.http.GET
import retrofit2.http.Query

interface AnimalsApiService {
    @GET("animals")
    suspend fun getAnimalsByBreed(
        @Query("name") name: String
    ): AnimalsResponseDto
}
