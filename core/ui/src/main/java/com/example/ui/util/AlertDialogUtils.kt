package com.example.ui.util

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import com.example.common.R

object AlertDialogUtils {
    private var alertDialog: AlertDialog? = null

    fun showAlertDialog(
        context: Context,
        title: String,
        message: String,
        positiveText: String = context.getString(R.string.ok),
        negativeText: String = context.getString(R.string.cancel),
        onPositiveClick: ((DialogInterface) -> Unit)? = null,
        onNegativeClick: ((DialogInterface) -> Unit)? = null,
    ) {
        hideAlertDialog()
        alertDialog =
            AlertDialog.Builder(context).setTitle(title).setMessage(message)
                .setPositiveButton(positiveText) { dialog, _ ->
                    onPositiveClick?.invoke(dialog)
                }.setNegativeButton(negativeText) { dialog, _ ->
                    onNegativeClick?.invoke(dialog)
                }.create()

        alertDialog?.show()
    }

    fun hideAlertDialog() {
        alertDialog?.hide()
    }
}
