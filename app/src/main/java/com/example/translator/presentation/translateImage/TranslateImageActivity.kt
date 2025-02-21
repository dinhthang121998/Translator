package com.example.translator.presentation.translateImage

import com.example.translator.databinding.ActivityTranslateImageBinding
import com.example.translator.presentation.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TranslateImageActivity : BaseActivity<ActivityTranslateImageBinding>() {
    override fun initBinding(): ActivityTranslateImageBinding = ActivityTranslateImageBinding.inflate(layoutInflater)

    override fun showFragment() {
        replaceFragment(TranslateImageFragment.newInstance(), initBinding().frameLayout, TAG)
    }

    companion object {
        const val TAG = "TranslateImageFragment"
    }
}