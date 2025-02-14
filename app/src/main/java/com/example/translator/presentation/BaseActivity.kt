package com.example.translator.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<V : ViewBinding> : AppCompatActivity() {
    private var _binding: V? = null
    private val binding get() = _binding!!

    abstract fun initBinding(): V?

    abstract fun showFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = initBinding()
        setContentView(binding.root)
        showFragment()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun replaceFragment(
        fragment: Fragment,
        container: View,
        tag: String,
    ) {
        supportFragmentManager.beginTransaction().replace(container.id, fragment, tag).commit()
    }
}
