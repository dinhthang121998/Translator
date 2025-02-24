package com.example.home

import com.example.home.databinding.ActivityHomeBinding
import com.example.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>() {
    override fun initBinding(): ActivityHomeBinding = ActivityHomeBinding.inflate(layoutInflater)

    override fun showFragment() {
        replaceFragment(HomeFragment.newInstance(), initBinding().frameLayout, TAG)
    }

    companion object {
        private const val TAG = "HomeFragment"
    }
}
