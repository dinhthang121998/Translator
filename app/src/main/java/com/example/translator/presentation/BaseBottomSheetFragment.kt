package com.example.translator.presentation

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetFragment : BottomSheetDialogFragment() {
    abstract val isCanceledOnTouchOutside: Boolean
    abstract val isFullScreen: Boolean
    abstract val viewBinding: ViewBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return viewBinding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setCanceledOnTouchOutside(isCanceledOnTouchOutside)
        }
    }

    override fun onStart() {
        super.onStart()
        if (isFullScreen) {
            dialog?.let { dialog ->
                val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                bottomSheet?.let {
                    val behavior = BottomSheetBehavior.from(it)
                    behavior.state = BottomSheetBehavior.STATE_EXPANDED // Expand to full height
                    // STATE_EXPENDED = full screen
                    // STATE_HIDDEN = hide the bottom sheet
                    // STATE_COLLAPSED = high than STATE_HALF_EXPANDED a bit
                    behavior.skipCollapsed = true
                }
            }
        }
    }
}
