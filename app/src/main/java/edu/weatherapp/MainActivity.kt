package edu.weatherapp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dev.androidbroadcast.vbpd.viewBinding
import edu.weatherapp.databinding.MainActivityBinding
import edu.weatherapp.presentation.WeatherUiState
import edu.weatherapp.presentation.WeatherViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(R.layout.main_activity) {

    private val viewBinding: MainActivityBinding by viewBinding(MainActivityBinding::bind)

    val weatherViewModel: WeatherViewModel by viewModels { WeatherViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        collectData()
        viewBinding.button.setOnClickListener {
            weatherViewModel.getForecast()
            if (viewBinding.button.text != "Обновить") {
                viewBinding.button.text = "Обновить"
            }
        }
    }

    private fun collectData() {
        // Start a coroutine in the lifecycle scope
        lifecycleScope.launch {
            // repeatOnLifecycle launches the block in a new coroutine every time the
            // lifecycle is in the STARTED state (or above) and cancels it when it's STOPPED.
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    weatherViewModel.forecast.collect { uiState: WeatherUiState ->
                        viewBinding.location.text = getString(R.string.location, uiState.location)
                        viewBinding.temperature.text = getString(R.string.temperature, uiState.temp)
                        viewBinding.date.text = getString(R.string.date, uiState.time)
                        viewBinding.loadingIndicator.isVisible = uiState.isLoading
                        viewBinding.button.isEnabled = !uiState.isLoading
                    }
                }
            }
        }
    }
}
