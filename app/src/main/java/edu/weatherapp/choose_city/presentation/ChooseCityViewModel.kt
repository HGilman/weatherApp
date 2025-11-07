package edu.weatherapp.choose_city.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import edu.weatherapp.App
import edu.weatherapp.choose_city.recycler.CityItem
import edu.weatherapp.data.CoordinatesApi
import edu.weatherapp.data.WeatherDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChooseCityViewModel(
    private val coordinatesApi: CoordinatesApi,
    private val store: WeatherDataStore
) : ViewModel() {

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as App
                val coordinatesApi = app.coordinatesApi
                val storage = app.dataStore
                ChooseCityViewModel(coordinatesApi, storage)
            }
        }
    }

    init {
        populateLastLocations()
    }

    private fun populateLastLocations() {
        viewModelScope.launch(Dispatchers.Default) {
            val lastLocations = store.lastLocationsFlow.first()
            _cities.update { lastLocations.map { CityItem(it, isSelectedPreviously = true) } }
        }
    }

    private val _cities: MutableStateFlow<List<CityItem>> = MutableStateFlow(emptyList())
    val cities: StateFlow<List<CityItem>> = _cities

    fun requestCities(city: String) {
        if (city.isEmpty()) return
        viewModelScope.launch {
            val cities = withContext(Dispatchers.Default) {
                coordinatesApi.coordinates(city, limit = 10).map { it.name }
            }
            _cities.update { cities.map { CityItem(it) } }
        }
    }
}
