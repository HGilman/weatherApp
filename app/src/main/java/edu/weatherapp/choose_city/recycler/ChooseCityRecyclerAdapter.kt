package edu.weatherapp.choose_city.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import dev.androidbroadcast.vbpd.viewBinding
import edu.weatherapp.R
import edu.weatherapp.databinding.ChooseCityItemBinding

class ChooseCityRecyclerAdapter(
    private val listener: (CityItem) -> Unit
) : RecyclerView.Adapter<CityRecyclerViewHolder>() {

    private var items: List<CityItem> = emptyList()

    fun updateData(newItems: List<CityItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityRecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.choose_city_item, parent, false)
        return CityRecyclerViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: CityRecyclerViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun onBindViewHolder(holder: CityRecyclerViewHolder, position: Int, payloads: List<Any?>) {
        super.onBindViewHolder(holder, position, payloads)
    }

    override fun getItemCount() = items.size
}

class CityRecyclerViewHolder(
    itemView: View,
    private val listener: (CityItem) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val binding: ChooseCityItemBinding by viewBinding(ChooseCityItemBinding::bind)

    fun bind(item: CityItem) {
        binding.city.text = item.city
        binding.icon.isVisible = item.isSelectedPreviously
        binding.root.setOnClickListener {
            listener.invoke(item)
        }
    }
}

data class CityItem(val city: String, val isSelectedPreviously: Boolean = false)
