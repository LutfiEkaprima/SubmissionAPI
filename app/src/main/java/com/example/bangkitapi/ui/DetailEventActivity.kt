package com.example.bangkitapi.ui

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.bangkitapi.R
import com.example.bangkitapi.data.database.entity.EventEntity
import com.example.bangkitapi.data.database.room.EventDao
import com.example.bangkitapi.data.database.room.EventDatabase
import com.example.bangkitapi.databinding.ActivityDetailEventBinding
import com.example.bangkitapi.data.response.ListEventsItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailEventBinding
    private lateinit var db: EventDatabase
    private lateinit var eventDao: EventDao
    private var isFavorited = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = EventDatabase.getDatabase(this)
        eventDao = db.eventDao()

        showLoading(true)

        val eventData: Parcelable? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("event_data", ListEventsItem::class.java)
                ?: intent.getParcelableExtra("event_data", EventEntity::class.java)
        } else {
            intent.getParcelableExtra("event_data")
        }

        when (eventData) {
            is ListEventsItem -> {
                setupUI(eventData)
                checkFavoriteStatus(eventData.name)
                setupFavoriteButton(eventData)
                showLoading(false)
            }
            is EventEntity -> {
                setupUIFromEventEntity(eventData)
                checkFavoriteStatus(eventData.eventName)
                setupFavoriteButtonFromEntity(eventData)
                showLoading(false)
            }
        }
    }

    private fun setupUI(event: ListEventsItem) {
        binding.tvEventName.text = event.name
        binding.tvEventDescription.text = HtmlCompat.fromHtml(event.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
        binding.tvEventBeginTime.text = getString(R.string.event_date, event.beginTime)
        binding.tvquota.text = getString(R.string.event_quota, event.quota - event.registrants)
        binding.tvownerName.text = event.ownerName

        val linkuri = Uri.parse(event.link)
        binding.link.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = linkuri
            }
            startActivity(intent)
        }

        Glide.with(this)
            .load(event.mediaCover)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                    showLoading(false)
                    return false
                }

                override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>?, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                    showLoading(false)
                    return false
                }
            })
            .into(binding.imgEventCover)
    }

    private fun setupUIFromEventEntity(event: EventEntity) {
        binding.tvEventName.text = event.eventName
        binding.tvEventDescription.text = HtmlCompat.fromHtml(event.eventDescription, HtmlCompat.FROM_HTML_MODE_LEGACY)
        binding.tvEventBeginTime.text = getString(R.string.event_date, event.eventBeginTime)
        binding.tvquota.text = getString(R.string.event_quota, event.eventQuota)
        binding.tvownerName.text = event.eventOwner

        val linkuri = Uri.parse(event.eventLink)
        binding.link.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = linkuri
            }
            startActivity(intent)
        }

        Glide.with(this)
            .load(event.eventCoverUrl)
            .into(binding.imgEventCover)
    }

    private fun checkFavoriteStatus(eventName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val favoriteEvent = eventDao.getFavoriteByName(eventName)
            isFavorited = favoriteEvent != null
            withContext(Dispatchers.Main) {
                updateFavoriteIcon(isFavorited)
            }
        }
    }

    private fun setupFavoriteButton(event: ListEventsItem) {
        binding.fabFavorite.setOnClickListener {
            if (isFavorited) {
                removeFavorite(event.name)
            } else {
                addFavorite(event)
            }
        }
    }

    private fun setupFavoriteButtonFromEntity(event: EventEntity) {
        binding.fabFavorite.setOnClickListener {
            if (isFavorited) {
                removeFavorite(event.eventName)
            } else {
                addFavoriteFromEntity(event)
            }
        }
    }

    private fun addFavorite(event: ListEventsItem) {
        CoroutineScope(Dispatchers.IO).launch {
            val favoriteEvent = EventEntity(
                eventName = event.name,
                eventDescription = event.description,
                eventBeginTime = event.beginTime,
                eventQuota = event.quota - event.registrants,
                eventOwner = event.ownerName,
                eventCoverUrl = event.mediaCover,
                eventLink = event.link
            )
            eventDao.insertFavorite(favoriteEvent)
            isFavorited = true
            withContext(Dispatchers.Main) {
                updateFavoriteIcon(isFavorited)
            }
        }
    }

    private fun addFavoriteFromEntity(event: EventEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            eventDao.insertFavorite(event)
            isFavorited = true
            withContext(Dispatchers.Main) {
                updateFavoriteIcon(isFavorited)
            }
        }
    }

    private fun removeFavorite(eventName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            eventDao.deleteFavorite(eventName)
            isFavorited = false
            withContext(Dispatchers.Main) {
                updateFavoriteIcon(isFavorited)
            }
        }
    }

    private fun updateFavoriteIcon(isFavorited: Boolean) {
        val favoriteIcon = if (isFavorited) {
            R.drawable.baseline_favorite_24
        } else {
            R.drawable.baseline_favorite_border_24
        }
        binding.fabFavorite.setImageDrawable(ContextCompat.getDrawable(this, favoriteIcon))
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) {
            ProgressBar.VISIBLE
        } else {
            ProgressBar.GONE
        }
    }
}
