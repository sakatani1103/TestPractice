package com.example.testpractice.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.testpractice.data.local.Place
import com.example.testpractice.databinding.ListItemBinding

class ListAdapter(val clickListener: PlaceListListener) :
    RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    class ViewHolder(var binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<Place>() {
        override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
            return oldItem.placeId == newItem.placeId
        }

        override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    var places : List<Place>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = places[position]
        holder.binding.place = place
        holder.binding.listener = clickListener
    }

    override fun getItemCount(): Int {
        return places.size
    }
}

class PlaceListListener(val clickListener: (id: String) -> Unit) {
    fun onClick(place: Place) = clickListener(place.placeId)
}