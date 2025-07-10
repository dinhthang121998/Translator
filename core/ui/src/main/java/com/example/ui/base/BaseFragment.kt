package com.example.ui.base

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.example.model.SearchLanguageItem
import com.example.ui.R
import com.example.ui.bottomSheet.searchSelectedLanguage.SearchSelectedLanguageSheet
import com.example.ui.util.AlertDialogUtils

abstract class BaseFragment<V : ViewBinding, M : ViewModel> : Fragment() {
    private var _binding: V? = null
    val binding get() = _binding!!

    abstract fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToParent: Boolean,
    ): V?

    abstract val viewModel: M

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = initBinding(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun showSearchBottomSheet(
        clickItem: ((SearchLanguageItem) -> Unit)? = null,
    ) {
        val bottomSheetLanguage =
            SearchSelectedLanguageSheet.newInstance().apply {
                clickCloseButton = {
                    this.dismiss()
                }
                clickItemButton = { searchLanguageItem: SearchLanguageItem ->
                    val languageItem = searchLanguageItem as SearchLanguageItem.LanguageItem
                    clickItem?.invoke(languageItem)
                    this.dismiss()
                }
            }
        bottomSheetLanguage.show(childFragmentManager, SearchSelectedLanguageSheet.TAG)
    }

    fun showAlertDialog(
        context: Context,
        title: String,
        message: String,
        positiveText: String = context.getString(R.string.ok),
        negativeText: String = context.getString(R.string.cancel),
        onPositiveClick: ((DialogInterface) -> Unit)? = null,
        onNegativeClick: ((DialogInterface) -> Unit)? = null,
    ) {
        AlertDialogUtils.showAlertDialog(
            context,
            title,
            message,
            positiveText,
            negativeText,
            onPositiveClick,
            onNegativeClick
        )
    }
}
