package com.example.translator.presentation

import android.os.Bundle
import com.example.home.HomeFragment
import com.example.translator.R
import com.example.translator.databinding.ActivityMainBinding
import com.example.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun initBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun showFragment() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        replaceFragment(HomeFragment.newInstance(), binding.flContent, HomeFragment.TAG)
        handleBottomNavigation()
    }

    private fun handleBottomNavigation() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    replaceFragment(HomeFragment.newInstance(), binding.flContent, HomeFragment.TAG)
                    true
                }
                R.id.nav_favored -> {
                    true
                }
                R.id.nav_settings -> {
                    true
                }
                else -> true
            }
        }
    }
}
