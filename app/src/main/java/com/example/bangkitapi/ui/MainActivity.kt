package com.example.bangkitapi.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.bangkitapi.R
import com.example.bangkitapi.databinding.ActivityMainBinding
import com.example.bangkitapi.ui.fragment.FavoriteFragment
import com.example.bangkitapi.ui.fragment.FinishedFragment
import com.example.bangkitapi.ui.fragment.SettingsFragment
import com.example.bangkitapi.ui.fragment.UpcomingFragment
import androidx.appcompat.app.AppCompatDelegate
import com.example.bangkitapi.ui.preferences.SettingPreferences
import com.example.bangkitapi.ui.preferences.dataStore
import com.example.bangkitapi.ui.viewmodel.MainViewModel
import com.example.bangkitapi.ui.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingPreferences.getInstance(applicationContext.dataStore)
        val mainViewModel = ViewModelProvider(this, ViewModelFactory(pref))[MainViewModel::class.java]

        mainViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        setupBottomNavigation()

        if (savedInstanceState == null) {
            loadFragment(UpcomingFragment())
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            val fragment = when (item.itemId) {
                R.id.nav_upcoming -> UpcomingFragment()
                R.id.nav_finished -> FinishedFragment()
                R.id.nav_favorite -> FavoriteFragment()
                R.id.nav_setting -> SettingsFragment()
                else -> UpcomingFragment()
            }
            loadFragment(fragment)
            saveLastSelectedFragment(item.itemId)
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

    private fun saveLastSelectedFragment(itemId: Int) {
        val sharedPref = getSharedPreferences("FragmentPrefs", MODE_PRIVATE)
        with(sharedPref.edit()) {
            putInt("LAST_SELECTED_FRAGMENT", itemId)
            apply()
        }
    }
    
}
