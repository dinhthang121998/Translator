package com.example.translateimage

import com.example.translateimage.databinding.ActivityTranslateImageBinding
import com.example.ui.base.BaseActivity
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
