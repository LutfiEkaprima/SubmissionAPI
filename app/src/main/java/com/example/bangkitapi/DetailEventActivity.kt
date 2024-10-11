package com.example.bangkitapi

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.bangkitapi.data.response.ListEventsItem

class DetailEventActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_event)

        val event = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("event_data", ListEventsItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<ListEventsItem>("event_data")
        }

        if (event != null) {
            Log.d("DetailEventActivity", "Event received: ${event.name}")
        } else {
            Log.e("DetailEventActivity", "No event data received")
        }

        event?.let {
            findViewById<TextView>(R.id.tvEventName).text = it.name
            findViewById<TextView>(R.id.tvEventDescription).text = it.description
            findViewById<TextView>(R.id.tvEventBeginTime).text = it.beginTime
            findViewById<TextView>(R.id.tvEventEndTime).text = it.endTime

            val imageView = findViewById<ImageView>(R.id.imgEventCover)
            Glide.with(this).load(it.mediaCover).into(imageView)
        }
    }
}