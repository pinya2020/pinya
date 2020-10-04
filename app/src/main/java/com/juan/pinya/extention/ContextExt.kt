package com.juan.pinya.extention

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.juan.pinya.R

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.showAlertDialog(
    message: String,
    positiveText: String,
    positiveAction: (DialogInterface, Int) -> Unit,
    negativeText: String,
    negativeAction: (DialogInterface, Int) -> Unit
) {
    AlertDialog.Builder(this)
        .setMessage(message)
        .setPositiveButton(positiveText, positiveAction)
        .setNegativeButton(negativeText, negativeAction)
        .show()
}

fun Context.showAlertDialog(
    message: String,
    positiveText: String,
    positiveAction: (DialogInterface, Int) -> Unit
) {
    AlertDialog.Builder(this)
        .setMessage(message)
        .setPositiveButton(positiveText, positiveAction)
        .setCancelable(false)
        .show()
}

fun Context.showLoadingDialog():AlertDialog {
    val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null)
    val mBuilder = AlertDialog.Builder(this)
        .setView(mDialogView)
        .setCancelable(false)
    val mAlertDialog = mBuilder.show()
    return mAlertDialog

}
