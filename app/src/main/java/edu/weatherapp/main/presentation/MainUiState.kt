package edu.weatherapp.main.presentation

const val DEFAULT_LOCATION = "Moscow"

data class MainUiState(
    val time: String? = null,
    val temp: String? = null,
    val location: String = DEFAULT_LOCATION,
    val isLoading: Boolean = true
)
