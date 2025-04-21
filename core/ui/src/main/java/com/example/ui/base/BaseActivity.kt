package com.example.ui.base

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<V : ViewBinding> : AppCompatActivity() {
    private var _binding: V? = null
    val binding get() = _binding!!

    private var currentTag = ""

    abstract fun initBinding(): V?

    abstract fun showFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = initBinding()
        setContentView(binding.root)
        handleEdgeToEdge(binding.root)
        showFragment()
    }

    // for system bar and cutout
    private fun handleEdgeToEdge(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val bars =
                insets.getInsets(
                    WindowInsetsCompat.Type.systemBars()
                        or WindowInsetsCompat.Type.displayCutout(),
                )
            v.updatePadding(
                left = bars.left,
                top = bars.top,
                right = bars.right,
                bottom = bars.bottom,
            )
            WindowInsetsCompat.CONSUMED
        }
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
        if (tag == currentTag) return
        currentTag = tag
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(container.id, fragment, tag)
        transaction.commit()
    }
}
