package com.example.ui.util

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import com.example.ui.R

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
        val builder =
            AlertDialog.Builder(context)
                .setTitle(title).setMessage(message)
                .setPositiveButton(positiveText) { dialog, _ ->
                    dialog.dismiss()
                    onPositiveClick?.invoke(dialog)
                }

        onNegativeClick?.let {
            builder.setNegativeButton(negativeText) { dialog, _ ->
                dialog.dismiss()
                onNegativeClick.invoke(dialog)
            }
        }
        alertDialog = builder.create()
        alertDialog?.show()
    }

    fun hideAlertDialog() {
        alertDialog?.hide()
    }
}
