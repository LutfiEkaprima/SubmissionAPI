package com.example.bangkitapi.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.bangkitapi.R
import com.example.bangkitapi.databinding.ActivityMainBinding
import com.example.bangkitapi.ui.fragment.FavoriteFragment
import com.example.bangkitapi.ui.fragment.FinishedFragment
import com.example.bangkitapi.ui.fragment.SettingsFragment
import com.example.bangkitapi.ui.fragment.UpcomingFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNavigation()
        loadFragment(UpcomingFragment())
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            val fragment: Fragment = when (item.itemId) {
                R.id.nav_upcoming -> UpcomingFragment()
                R.id.nav_finished -> FinishedFragment()
                R.id.nav_favorite -> FavoriteFragment()
                R.id.nav_setting -> SettingsFragment()
                else -> UpcomingFragment()
            }
            loadFragment(fragment)
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        if (currentFragment != null && currentFragment::class == fragment::class) {
            return
        }

        fragmentTransaction.replace(R.id.fragmentContainer, fragment).commit()
    }
}
