package com.example.bangkitapi.data.retrofit

import com.example.bangkitapi.data.response.EventResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("events")
    fun getEvent(
        @Query("active") active: String
    ): Call<EventResponse>

}