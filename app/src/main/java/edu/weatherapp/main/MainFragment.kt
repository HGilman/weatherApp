package edu.weatherapp.main

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dev.androidbroadcast.vbpd.viewBinding
import edu.weatherapp.R
import edu.weatherapp.choose_city.ChooseCityFragment
import edu.weatherapp.databinding.MainFragmentBinding
import edu.weatherapp.main.presentation.MainUiState
import edu.weatherapp.main.presentation.MainViewModel
import kotlinx.coroutines.launch

class MainFragment : Fragment(R.layout.main_fragment) {

    private val viewBinding: MainFragmentBinding by viewBinding(MainFragmentBinding::bind)

    val mainViewModel: MainViewModel by viewModels { MainViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        collectData()
        setFragmentResultListener(ChooseCityFragment.REQUEST_KEY) { requestKey, bundle ->
            bundle.getString(ChooseCityFragment.SELECTED_ITEM)?.let {
                mainViewModel.saveNewCityAndRequestForecast(it)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
    }

    private fun collectData() {
        // Start a coroutine in the lifecycle scope
        lifecycleScope.launch {
            // repeatOnLifecycle launches the block in a new coroutine every time the
            // lifecycle is in the STARTED state (or above) and cancels it when it's STOPPED.
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    mainViewModel.forecast.collect { uiState: MainUiState ->
                        viewBinding.location.text = getString(R.string.location, uiState.location)
                        viewBinding.temperature.text = getString(R.string.temperature, uiState.temp)
                        viewBinding.date.text = getString(R.string.date, uiState.time)
                        viewBinding.loadingIndicator.isVisible = uiState.isLoading
                        viewBinding.button.isEnabled = !uiState.isLoading

                        viewBinding.temperature.isVisible = !uiState.isLoading
                        viewBinding.date.isVisible = !uiState.isLoading
                    }
                }
            }
        }
    }

    private fun setUpViews() {
        with(viewBinding) {
            button.setOnClickListener {
                mainViewModel.getForecast()
                if (button.text != "Обновить") {
                    button.text = "Обновить"
                }
            }
            location.setOnClickListener {
                parentFragmentManager.commit {
                    setCustomAnimations(
                        R.anim.slide_in_right,
                        R.anim.slide_out_left,
                        R.anim.slide_in_left,
                        R.anim.slide_out_right
                    )
                    replace(R.id.fragmentContainer, ChooseCityFragment(), null)
                    addToBackStack(null)
                }
            }
        }
    }
}
