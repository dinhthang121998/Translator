package com.example.translatecamerax

import android.os.Bundle
import com.example.translatecamerax.databinding.ActivityTranslateCameraXactivityBinding
import com.example.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TranslateCameraXActivity : BaseActivity<ActivityTranslateCameraXactivityBinding>() {
    override fun initBinding(): ActivityTranslateCameraXactivityBinding = ActivityTranslateCameraXactivityBinding.inflate(layoutInflater)

    override fun showFragment(savedInstanceState: Bundle?) {
        replaceFragment(TranslateCameraXFragment.newInstance(), initBinding().frameLayout, TAG)
    }

    companion object {
        const val TAG = "TranslateCameraXFragment"
    }
}
