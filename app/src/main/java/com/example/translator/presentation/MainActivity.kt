package com.example.translator.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.example.favored.FavoredFragment
import com.example.home.HomeFragment
import com.example.settings.SettingsFragment
import com.example.translator.R
import com.example.translator.databinding.ActivityMainBinding
import com.example.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    private val viewmodel: MainViewmodel by viewModels()

    override fun initBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun showFragment(savedInstanceState: Bundle?) {
        // Keep the setting screen when changing dark/ light theme
        if (savedInstanceState == null) {
            replaceFragment(HomeFragment.newInstance(), binding.flContent, HomeFragment.TAG)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleBottomNavigation()
        viewmodel.observeTheme()
        lifecycleScope.launch {
            launch {
                viewmodel.themeSharedFlow.collect { isDarkMode ->
                    val mode =
                        if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
                    AppCompatDelegate.setDefaultNightMode(mode)
                }
            }
        }
    }

    private fun handleBottomNavigation() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    replaceFragment(
                        HomeFragment.newInstance(),
                        binding.flContent,
                        HomeFragment.TAG,
                    )
                    true
                }

                R.id.nav_favored -> {
                    replaceFragment(
                        FavoredFragment.newInstance(),
                        binding.flContent,
                        FavoredFragment.TAG,
                    )
                    true
                }

                R.id.nav_settings -> {
                    replaceFragment(
                        SettingsFragment.newInstance(),
                        binding.flContent,
                        SettingsFragment.TAG,
                    )
                    true
                }

                else -> true
            }
        }
    }
}
