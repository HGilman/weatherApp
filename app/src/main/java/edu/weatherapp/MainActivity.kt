package edu.weatherapp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dev.androidbroadcast.vbpd.viewBinding
import edu.weatherapp.databinding.MainActivityBinding
import edu.weatherapp.presentation.WeatherUiState
import edu.weatherapp.presentation.WeatherViewModel

class MainActivity : AppCompatActivity(R.layout.main_activity) {

    private val viewBinding: MainActivityBinding by viewBinding(MainActivityBinding::bind)

    val weatherViewModel: WeatherViewModel by viewModels { WeatherViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // todo: use Flow
        weatherViewModel.forecast.observe(this) { uiState: WeatherUiState ->
            viewBinding.location.text = getString(R.string.location, uiState.location)
            viewBinding.temperature.text = getString(R.string.temperature, uiState.temp)
            viewBinding.date.text = getString(R.string.date, uiState.time)
        }

        viewBinding.button.setOnClickListener {
            weatherViewModel.getForecast()
            if (viewBinding.button.text != "Обновить") {
                viewBinding.button.text = "Обновить"
            }
        }
    }
}
