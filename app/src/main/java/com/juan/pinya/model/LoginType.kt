package com.juan.pinya.model

import android.app.Activity
import android.app.Dialog
import com.juan.pinya.R
import com.juan.pinya.extention.showAlertDialog
import com.juan.pinya.view.main.MainActivity

sealed class LoginType(var isLoginSuccess: Boolean, var work: Boolean) {
    class Normal(isLoginSuccess: Boolean, work:Boolean): LoginType(isLoginSuccess, work) {
        override fun action(activity: Activity, loadingDialog: Dialog?) {
            if (work) {
                if (isLoginSuccess) {
                    gotoMainActivity(activity, loadingDialog)
                } else {
                    loadingDialog?.dismiss()
                    activity.showAlertDialog(
                        activity.getString(R.string.text_wrong_password),
                        activity.getString(R.string.text_enter)
                    ) { dialog, _ ->
                        dialog.cancel()
                    }
                }
            }else{
                loadingDialog?.dismiss()
                activity.showAlertDialog(
                    activity.getString(R.string.text_wrong_work),
                    activity.getString(R.string.text_enter)
                ) { dialog, _ ->
                    dialog.cancel()
                }
            }
        }
    }

    class AutoLogin(isLoginSuccess: Boolean, work:Boolean): LoginType(isLoginSuccess, work) {
        override fun action(activity: Activity, loadingDialog: Dialog?) {
            if (work) {
                if (isLoginSuccess) {
                    gotoMainActivity(activity, loadingDialog)
                } else {
                    loadingDialog?.dismiss()
                }
            }else{
                loadingDialog?.dismiss()
                activity.showAlertDialog(
                    activity.getString(R.string.text_wrong_work),
                    activity.getString(R.string.text_enter)
                ) { dialog, _ ->
                    dialog.cancel()
                }
            }
        }
    }

    protected fun gotoMainActivity(activity: Activity, loadingDialog: Dialog?) {
        loadingDialog?.dismiss()
        val intent = MainActivity.getIntent(activity)
        activity.startActivity(intent)
        activity.finish()
    }

    abstract fun action(activity: Activity, loadingDialog: Dialog?)
}