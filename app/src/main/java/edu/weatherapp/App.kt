package edu.weatherapp

import android.app.Application
import edu.weatherapp.data.CoordinatesApi
import edu.weatherapp.data.WeatherApi
import edu.weatherapp.data.WeatherDataStore
import edu.weatherapp.network.Network
import edu.weatherapp.main.presentation.MainUiStateMapper

class App : Application() {

    val weatherApi: WeatherApi by lazy { Network.weatherApi }

    val coordinatesApi: CoordinatesApi by lazy { Network.coordinatesApi }

    val uiStateMapper: MainUiStateMapper by lazy { MainUiStateMapper() }

    val dataStore: WeatherDataStore by lazy { WeatherDataStore(this) }
}
