package com.example.translator.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.translator.R
import com.example.translator.databinding.ActivityMainBinding
import com.example.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    private val viewmodel: MainViewmodel by viewModels()

    override fun initBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun showFragment(savedInstanceState: Bundle?) {
        handleBottomNavigation()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewmodel.observeTheme()
        lifecycleScope.launch {
            launch {
                val isDarkMode = viewmodel.themeFlow.first { it != null }
                isDarkMode?.let {
                    val mode =
                        if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
                    AppCompatDelegate.setDefaultNightMode(mode)
                }
            }
        }
    }

    private fun handleBottomNavigation() {
        // Get the NavController from the NavHostFragment
        val navHostFragment =
            supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Connect the BottomNavigationView to the NavController
        binding.bottomNavigationView.setupWithNavController(navController)
    }
}
