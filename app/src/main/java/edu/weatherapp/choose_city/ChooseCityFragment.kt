package edu.weatherapp.choose_city

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import dev.androidbroadcast.vbpd.viewBinding
import edu.weatherapp.R
import edu.weatherapp.choose_city.presentation.ChooseCityViewModel
import edu.weatherapp.choose_city.recycler.ChooseCityRecyclerAdapter
import edu.weatherapp.choose_city.recycler.CityItem
import edu.weatherapp.databinding.ChooseCityFragmentBinding
import kotlinx.coroutines.launch

class ChooseCityFragment : Fragment() {

    companion object {
        const val REQUEST_KEY = "ChooseCityFragmentRequestKey"
        const val SELECTED_ITEM = "ChooseCityFragmentSelectedItem"
    }

    private val binding: ChooseCityFragmentBinding by viewBinding(ChooseCityFragmentBinding::bind)

    private lateinit var adapter: ChooseCityRecyclerAdapter

    private val viewModel: ChooseCityViewModel by viewModels { ChooseCityViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        collectData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.choose_city_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
    }

    private val onItemClickListener: (CityItem) -> Unit = {
        setFragmentResult(REQUEST_KEY, bundleOf(SELECTED_ITEM to it.city))
        binding.search.postDelayed({
            activity?.supportFragmentManager?.popBackStack()
        }, 50)
    }

    fun setUpViews() {
        binding.searchRecycler.layoutManager = LinearLayoutManager(context)
        binding.searchRecycler.adapter = ChooseCityRecyclerAdapter(onItemClickListener).also {
            adapter = it
        }
        binding.search.doOnTextChanged {  city: CharSequence?, _, _, _ ->
            if (city != null && city.isNotBlank()) {
                viewModel.requestCities(city.toString())
            }
        }
        binding.search.requestFocus()
        binding.close.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    fun collectData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.cities.collect {
                        adapter.updateData(it)
                    }
                }
            }
        }
    }
}
