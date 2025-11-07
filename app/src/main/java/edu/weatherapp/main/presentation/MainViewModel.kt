package edu.weatherapp.main.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import edu.weatherapp.App
import edu.weatherapp.data.CoordinatesApi
import edu.weatherapp.data.WeatherApi
import edu.weatherapp.data.WeatherDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val weatherApi: WeatherApi,
    private val coordinatesApi: CoordinatesApi,
    private val uiStateMapper: MainUiStateMapper,
    private val dataStore: WeatherDataStore
) : ViewModel() {

    @Suppress("UNCHECKED_CAST")
    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val app: App = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY]) as App
                val weatherApi = app.weatherApi
                val coordinatesApi = app.coordinatesApi
                val uiStateMapper = app.uiStateMapper
                val dataStore = app.dataStore
                return MainViewModel(weatherApi, coordinatesApi, uiStateMapper, dataStore) as T
            }
        }
    }

    init {
        requestLastLocationForecast()
    }

    private val _forecast: MutableStateFlow<MainUiState> = MutableStateFlow(MainUiState())
    val forecast: StateFlow<MainUiState> = _forecast

    fun saveNewCityAndRequestForecast(newCity: String) {
        updateCityAndRequestForecast(newCity)
        viewModelScope.launch(Dispatchers.Default) {
            dataStore.saveLastLocation(newCity)
        }
    }

    fun getForecast() {
        viewModelScope.launch {
            _forecast.update { it.copy(isLoading = true) }
            val location = _forecast.value.location
            val newUiState = withContext(Dispatchers.Default) {
                val (lat, lon) = coordinatesApi.coordinates(location)
                    .map { it.lat to it.lon }
                    .first()
                val weatherResponse = weatherApi.currentWeather(lat, lon)
                uiStateMapper.map(_forecast.value, weatherResponse)
            }
            _forecast.update { newUiState }
        }
    }

    private fun requestLastLocationForecast() {
        viewModelScope.launch(Dispatchers.Default) {
            val lastLocation = dataStore.lastLocationsFlow.first().first()
            updateCityAndRequestForecast(lastLocation)
        }
    }

    private fun updateCityAndRequestForecast(newCity: String) {
        _forecast.update { it.copy(location = newCity) }
        getForecast()
    }
}
