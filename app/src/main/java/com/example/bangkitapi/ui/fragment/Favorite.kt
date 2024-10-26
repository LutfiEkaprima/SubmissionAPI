package com.example.bangkitapi.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bangkitapi.data.database.room.EventDatabase
import com.example.bangkitapi.databinding.FragmentFavoriteBinding
import com.example.bangkitapi.ui.adapter.FavoriteEventsAdapter
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: EventDatabase
    private lateinit var favoriteEventsAdapter: FavoriteEventsAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        db = EventDatabase.getDatabase(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "Favorite Event"
        setupRecyclerView()
        loadFavoriteEvents()
    }

    private fun setupRecyclerView() {
        binding.rvEvents.layoutManager = LinearLayoutManager(context)
        favoriteEventsAdapter = FavoriteEventsAdapter(requireContext(), emptyList())
        binding.rvEvents.adapter = favoriteEventsAdapter
    }

    private fun loadFavoriteEvents() {
        binding.progressBar.visibility = View.VISIBLE
        viewLifecycleOwner.lifecycleScope.launch {
            db.eventDao().getAllFavorites().collect { favoriteEvents ->
                favoriteEventsAdapter.updateData(favoriteEvents)
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
