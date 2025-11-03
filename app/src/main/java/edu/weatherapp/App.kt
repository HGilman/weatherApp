package edu.weatherapp

import android.app.Application
import edu.weatherapp.data.CoordinatesApi
import edu.weatherapp.data.WeatherApi
import edu.weatherapp.network.Network
import edu.weatherapp.presentation.UiStateMapper

class App : Application() {

    val weatherApi: WeatherApi by lazy { Network.weatherApi }

    val coordinatesApi: CoordinatesApi by lazy { Network.coordinatesApi }

    val uiStateMapper: UiStateMapper by lazy { UiStateMapper() }
}
