package com.example.bangkitapi.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bangkitapi.data.database.entity.EventEntity
import com.example.bangkitapi.databinding.ItemActiveEventBinding
import com.example.bangkitapi.ui.DetailEventActivity

class FavoriteEventsAdapter(
    private val context: Context,
    private var favoriteEvents: List<EventEntity>
) : RecyclerView.Adapter<FavoriteEventsAdapter.FavoriteEventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteEventViewHolder {
        val binding = ItemActiveEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteEventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteEventViewHolder, position: Int) {
        holder.bind(favoriteEvents[position])
    }

    override fun getItemCount(): Int = favoriteEvents.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newFavoriteEvents: List<EventEntity>) {
        favoriteEvents = newFavoriteEvents
        notifyDataSetChanged()
    }

    inner class FavoriteEventViewHolder(private val binding: ItemActiveEventBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(event: EventEntity) {
            binding.tvItemName.text = event.eventName

            Glide.with(binding.imgItemPhoto.context)
                .load(event.eventCoverUrl)
                .into(binding.imgItemPhoto)

            binding.cardView.setOnClickListener {
                val intent = Intent(context, DetailEventActivity::class.java).apply {
                    putExtra("event_data", event)
                }
                context.startActivity(intent)
            }
        }
    }
}
