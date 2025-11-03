package edu.weatherapp.data

import retrofit2.http.GET
import retrofit2.http.Query

interface CoordinatesApi {

    @GET("geo/1.0/direct")
    suspend fun coordinates(
        @Query("q") city: String,
        @Query("limit") limit: Int = 1
    ): List<Coordinates>
}

data class Coordinates(
    val name: String,
    val lat: String,
    val lon: String,
    val country: String
)
