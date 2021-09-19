package com.richarddewan.paging3_todo.util

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.richarddewan.paging3_todo.R

object AppHelper {

    fun showErrorSnackBar(view: View, context: Context, msg: String) {
        Snackbar.make(view,msg, Snackbar.LENGTH_INDEFINITE).apply {
            setBackgroundTint(ContextCompat.getColor(context, R.color.purple_700))
            setTextColor(ContextCompat.getColor(context, R.color.white))
            setActionTextColor(ContextCompat.getColor(context, R.color.white))
            setAction(R.string.close){
                dismiss()
            }
            anchorView = view
        }.show()
    }
}