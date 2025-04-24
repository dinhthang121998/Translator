package com.example.pronunciationspeed

import com.example.pronunciationspeed.databinding.ActivityPronunciationSpeedBinding
import com.example.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PronunciationSpeedActivity : BaseActivity<ActivityPronunciationSpeedBinding>() {
    override fun initBinding(): ActivityPronunciationSpeedBinding = ActivityPronunciationSpeedBinding.inflate(layoutInflater)

    override fun showFragment() {
        replaceFragment(PronunciationSpeedFragment.newInstance(), binding.flContent, PronunciationSpeedFragment.TAG)
    }
}
