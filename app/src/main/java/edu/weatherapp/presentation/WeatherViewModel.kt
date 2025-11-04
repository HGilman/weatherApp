package edu.weatherapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import edu.weatherapp.App
import edu.weatherapp.data.CoordinatesApi
import edu.weatherapp.data.WeatherApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WeatherViewModel(
    private val weatherApi: WeatherApi,
    private val coordinatesApi: CoordinatesApi,
    private val uiStateMapper: UiStateMapper
) : ViewModel() {

    @Suppress("UNCHECKED_CAST")
    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val app: App = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY]) as App
                val weatherApi = app.weatherApi
                val coordinatesApi = app.coordinatesApi
                val uiStateMapper = app.uiStateMapper
                return WeatherViewModel(weatherApi, coordinatesApi, uiStateMapper) as T
            }
        }
    }

    private val _forecast: MutableStateFlow<WeatherUiState> = MutableStateFlow(WeatherUiState())
    val forecast: StateFlow<WeatherUiState> = _forecast

    fun getForecast() {
        // не делаем запрос если идет загрузка
        if (_forecast.value.isLoading) return

        viewModelScope.launch {
            _forecast.update { it.copy(isLoading = true) }

            val location = _forecast.value.location
            val (lat, lon) = withContext(Dispatchers.Default) {
                coordinatesApi.coordinates(location)
                    .map { it.lat to it.lon }.first()
            }
            val newUiState = withContext(Dispatchers.Default) {
                // искусственная задержка, чтобы показать лоадер
                delay(500)
                val weatherResponse = weatherApi.currentWeather(lat, lon)
                uiStateMapper.map(weatherResponse)
            }
            _forecast.update { newUiState }
        }
    }
}
