package edu.weatherapp.presentation

import edu.weatherapp.data.CurrentWeatherResponse
import java.text.SimpleDateFormat

class UiStateMapper {

    private val dateFormat = "dd MMMM, HH:mm:ss"

    private val simpleDateFormat: SimpleDateFormat by lazy {
        SimpleDateFormat(dateFormat)
    }

    fun map(currentWeatherResponse: CurrentWeatherResponse): WeatherUiState {
        val current = currentWeatherResponse.current

        return WeatherUiState(
            temp = current.temp,
            time = simpleDateFormat.format(current.dt),
            isLoading = false
        )
    }
}
