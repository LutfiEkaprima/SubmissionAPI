package com.example.bangkitapi.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Entity(tableName = "favorite_events")
@Parcelize
data class EventEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val eventName: String,
    val eventDescription: String,
    val eventBeginTime: String,
    val eventQuota: Int,
    val eventOwner: String,
    val eventCoverUrl: String,
    val eventLink: String,

) : Parcelable