package com.example.translator.presentation.home

import com.example.translator.databinding.ActivityHomeBinding
import com.example.translator.presentation.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>() {
    override fun initBinding(): ActivityHomeBinding = ActivityHomeBinding.inflate(layoutInflater)

    override fun showFragment() {
        replaceFragment(HomeFragment.newInstance(), initBinding().frameLayout, TAG)
    }

    companion object {
        private val TAG = "HomeFragment"
    }
}
