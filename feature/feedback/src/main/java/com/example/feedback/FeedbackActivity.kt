package com.example.feedback

import android.os.Bundle
import com.example.feedback.databinding.ActivityFeedbackBinding
import com.example.ui.base.BaseActivity

class FeedbackActivity : BaseActivity<ActivityFeedbackBinding>() {
    override fun initBinding(): ActivityFeedbackBinding = ActivityFeedbackBinding.inflate(layoutInflater)

    override fun showFragment(savedInstanceState: Bundle?) {
        replaceFragment(FeedbackFragment.newInstance(), binding.flContent, FeedbackFragment.TAG)
    }
}
