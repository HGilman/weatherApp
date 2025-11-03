package edu.weatherapp.data

import retrofit2.http.GET
import retrofit2.http.Query
import java.util.Date

interface WeatherApi {

    @GET("data/3.0/onecall")
    suspend fun currentWeather(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("units") units: String = "metric"
    ): CurrentWeatherResponse
}

data class CurrentWeatherResponse(
    val current: Current
)

data class Current(
    val dt: Date, // milliseconds
    val temp: String
)

