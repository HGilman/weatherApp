package edu.weatherapp.data

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

class WeatherDataStore(
    private val context: Application
) {

    private val DEFAULT_LOCATION = "Almaty"

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("weatherPreferences")

    private val LAST_LOCATIONS = stringPreferencesKey("last_locations")

    val lastLocationsFlow = context.dataStore.data.map { preferences ->
        val location = preferences[LAST_LOCATIONS] ?: DEFAULT_LOCATION
        val lastLocations = location.split(",")
        lastLocations
    }

    suspend fun saveLastLocation(location: String) {
        context.dataStore.edit { preferences ->
            val previousLocations = preferences[LAST_LOCATIONS]
            val locationsList: List<String> = (previousLocations?.split(",") ?: listOf(DEFAULT_LOCATION))

            val newLocations = (listOf(location) + locationsList).uniqueItems().take(5)
            val newLocationsString = newLocations.joinToString(separator = ",")
            preferences[LAST_LOCATIONS] = newLocationsString
        }
    }

    private fun <T> List<T>.uniqueItems(): List<T> {
        return toSet().toList()
    }

}
