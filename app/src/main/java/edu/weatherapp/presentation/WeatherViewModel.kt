package edu.weatherapp.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import edu.weatherapp.App
import edu.weatherapp.data.CoordinatesApi
import edu.weatherapp.data.WeatherApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WeatherViewModel(
    private val weatherApi: WeatherApi,
    private val coordinatesApi: CoordinatesApi,
    private val uiStateMapper: UiStateMapper
) : ViewModel() {

    var currentForecastJob: Job? = null

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

    //todo: use Flow
    private val _forecast: MutableLiveData<WeatherUiState> = MutableLiveData(WeatherUiState())
    val forecast: LiveData<WeatherUiState> = _forecast

    fun getForecast() {
        currentForecastJob?.cancel()
        currentForecastJob = viewModelScope.launch {
            _forecast.value = _forecast.value.copy(isLoading = true)
            
            val location = _forecast.value.location
            val (lat, lon) = withContext(Dispatchers.Default) {
                coordinatesApi.coordinates(location)
                    .map { it.lat to it.lon }.first()
            }
            val uiState = withContext(Dispatchers.Default) {
                delay(500)
                val weatherResponse = weatherApi.currentWeather(lat, lon)
                uiStateMapper.map(weatherResponse)
            }
            _forecast.value = uiState
        }
    }
}
