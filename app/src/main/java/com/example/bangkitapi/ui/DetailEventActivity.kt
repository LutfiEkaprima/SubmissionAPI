package com.example.bangkitapi.ui

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.bangkitapi.R
import com.example.bangkitapi.data.response.ListEventsItem

class DetailEventActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    val tgl = "Tanggal Acara : "
    val kuota = "Kuota : "
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_event)

        progressBar = findViewById(R.id.progressBar)

        showLoading(true)

        val event = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("event_data", ListEventsItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<ListEventsItem>("event_data")
        }

        event?.let {
            findViewById<TextView>(R.id.tvEventName).text = it.name
            findViewById<TextView>(R.id.tvEventDescription).text =
                HtmlCompat.fromHtml(it.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
            findViewById<TextView>(R.id.tvEventBeginTime).text = tgl + it.beginTime
            val quotasisa = it.quota - it.registrants
            findViewById<TextView>(R.id.tvquota).text = kuota + quotasisa
            findViewById<TextView>(R.id.tvownerName).text = it.ownerName
            val buttonLink = findViewById<Button>(R.id.link)
            val linkuri = Uri.parse(it.link)

            buttonLink.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = linkuri
                }
                startActivity(intent)
            }

            val imageView = findViewById<ImageView>(R.id.imgEventCover)

            Glide.with(this)
                .load(it.mediaCover)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        showLoading(false)
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        showLoading(false)
                        return false
                    }
                })
                .into(imageView)
        } ?: run {
            showLoading(false)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) {
            ProgressBar.VISIBLE
        } else {
            ProgressBar.GONE
        }
    }
}
