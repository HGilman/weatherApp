package edu.weatherapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import edu.weatherapp.presentation.WeatherUiState
import edu.weatherapp.presentation.WeatherViewModel

class MainActivity : AppCompatActivity(R.layout.main_activity) {

    val weatherViewModel: WeatherViewModel by viewModels { WeatherViewModel.Factory }

    // todo: useViewBinding
    lateinit var location: Button
    lateinit var temperature: TextView
    lateinit var date: TextView
    lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // todo: use viewBinding
        location = findViewById(R.id.location)
        temperature = findViewById(R.id.temperature)
        date = findViewById(R.id.date)
        button = findViewById(R.id.button)

        // todo: use Flow
        weatherViewModel.forecast.observe(this) { uiState: WeatherUiState ->
            location.text = getString(R.string.location, uiState.location)
            temperature.text = getString(R.string.temperature, uiState.temp)
            date.text = getString(R.string.date, uiState.time)
        }

        button.setOnClickListener {
            weatherViewModel.getForecast()
            if (button.text != "Обновить") {
                button.text = "Обновить"
            }
        }
    }
}
