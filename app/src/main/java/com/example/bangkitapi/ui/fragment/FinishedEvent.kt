package com.example.bangkitapi.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bangkitapi.ui.DetailEventActivity
import com.example.bangkitapi.data.retrofit.ApiConfig
import com.example.bangkitapi.data.response.EventResponse
import com.example.bangkitapi.data.response.ListEventsItem
import com.example.bangkitapi.databinding.FragmentFinishedEventBinding
import com.example.bangkitapi.ui.EventAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedEventBinding? = null
    private val binding get() = _binding!!
    private lateinit var eventAdapter: EventAdapter
    private val restaurantId = "0"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        findEvents()
    }

    private fun setupRecyclerView() {
        binding.rvEvents.layoutManager = LinearLayoutManager(requireContext())
        binding.rvEvents.addItemDecoration(
            DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        )
    }

    private fun findEvents() {
        showLoading(true)
        val client = ApiConfig.getApiService().getEvent(restaurantId)
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(
                call: Call<EventResponse>,
                response: Response<EventResponse>
            ) {
                if (isAdded) {
                    showLoading(false)
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            setEventData(responseBody.listEvents)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                if (isAdded) {
                    showLoading(false)
                }
            }
        })
    }

    private fun setEventData(events: List<ListEventsItem>) {
        eventAdapter = EventAdapter(events) { event ->
            val intent = Intent(requireContext(), DetailEventActivity::class.java)
            intent.putExtra("event_data", event)
            startActivity(intent)
        }
        binding.rvEvents.adapter = eventAdapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isAdded) {
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}