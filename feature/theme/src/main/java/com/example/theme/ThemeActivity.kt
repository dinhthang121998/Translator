package com.example.theme

import android.os.Bundle
import com.example.theme.databinding.ActivityThemeBinding
import com.example.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ThemeActivity : BaseActivity<ActivityThemeBinding>() {
    override fun initBinding(): ActivityThemeBinding {
        return ActivityThemeBinding.inflate(layoutInflater)
    }

    override fun showFragment(savedInstanceState: Bundle?) {
        replaceFragment(ThemeFragment.newInstance(), binding.frameLayout, ThemeFragment.TAG)
    }
}
