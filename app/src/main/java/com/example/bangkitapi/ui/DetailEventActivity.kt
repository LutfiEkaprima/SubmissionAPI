package com.example.bangkitapi.ui

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.bangkitapi.R
import com.example.bangkitapi.databinding.ActivityDetailEventBinding
import com.example.bangkitapi.data.response.ListEventsItem

class DetailEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailEventBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoading(true)

        val event = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("event_data", ListEventsItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("event_data")
        }

        event?.let {
            binding.tvEventName.text = it.name
            binding.tvEventDescription.text = HtmlCompat.fromHtml(it.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
            binding.tvEventBeginTime.text = getString(R.string.event_date, it.beginTime)

            val quotasisa = it.quota - it.registrants
            binding.tvquota.text = getString(R.string.event_quota, quotasisa)
            binding.tvownerName.text = it.ownerName

            val linkuri = Uri.parse(it.link)
            binding.link.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = linkuri
                }
                startActivity(intent)
            }

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
                .into(binding.imgEventCover)
        } ?: run {
            showLoading(false)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) {
            ProgressBar.VISIBLE
        } else {
            ProgressBar.GONE
        }
    }
}
