package com.example.bangkitapi.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bangkitapi.data.response.ListEventsItem
import com.example.bangkitapi.databinding.ItemActiveEventBinding

class ActiveEventAdapter(private val events: List<ListEventsItem>) :
    RecyclerView.Adapter<ActiveEventAdapter.EventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemActiveEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(events[position])
    }

    override fun getItemCount(): Int = events.size

    inner class EventViewHolder(private val binding: ItemActiveEventBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem) {
            binding.tvItemName.text = event.name
            Glide.with(binding.imgItemPhoto.context)
                .load(event.mediaCover)
                .into(binding.imgItemPhoto)
        }
    }
}