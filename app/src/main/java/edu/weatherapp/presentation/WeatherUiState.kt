package edu.weatherapp.presentation

const val DEFAULT_LOCATION = "Moscow"

data class WeatherUiState(
    val time: String? = null,
    val temp: String? = null,
    val location: String = DEFAULT_LOCATION,
    val isLoading: Boolean = false
)
