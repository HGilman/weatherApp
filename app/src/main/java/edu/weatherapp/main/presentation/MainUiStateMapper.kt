package edu.weatherapp.main.presentation

import edu.weatherapp.data.CurrentWeatherResponse
import java.text.SimpleDateFormat
import kotlin.math.roundToInt

class MainUiStateMapper {

    private val dateFormat = "dd MMMM, HH:mm:ss"

    private val simpleDateFormat: SimpleDateFormat by lazy {
        SimpleDateFormat(dateFormat)
    }
    fun map(
        currentUiState: MainUiState,
        currentWeatherResponse: CurrentWeatherResponse
    ): MainUiState {

        val current = currentWeatherResponse.current
        return currentUiState.copy(
            temp = current.temp.roundToInt().toString(),
            time = simpleDateFormat.format(current.dt),
            isLoading = false
        )
    }
}
