package com.juan.pinya.module.dailyReport

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import com.juan.pinya.R
import com.juan.pinya.view.main.MainActivity
import com.juan.pinya.view.main.dailyReport.DailyReportFragment
import com.juan.pinya.view.main.dailyReport.carId.CarDialogFragment

abstract class BaseFragment : Fragment(){
    protected open var bottomNavigationViewVisibility = View.VISIBLE
    override fun onResume() {
        super.onResume()
        if (activity is MainActivity) {
            val mainActivity = activity as MainActivity
            mainActivity.setBottomNavigationVisibility(bottomNavigationViewVisibility)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (activity is MainActivity) {
            val mainActivity = activity as MainActivity
            mainActivity.setBottomNavigationVisibility(View.VISIBLE)
            setHasOptionsMenu(false)
        }
    }

    open fun showCarIdDialog(showAll: Boolean) {
        val bundle = Bundle()
        bundle.putBoolean("showAll", showAll)
        CarDialogFragment().also { carDialogFragment ->
            carDialogFragment.setTargetFragment(this, DailyReportFragment.CAR_ID_REQUEST)
            carDialogFragment.arguments = bundle
            carDialogFragment.show(parentFragmentManager, CarDialogFragment::class.simpleName)
        }
    }

    open fun showDeleteDialog() : Dialog{
        val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.delete_dialog, null)
        val mBuilder = AlertDialog.Builder(requireContext(), R.style.DeleteDialog)
            .setView(mDialogView)
            .setCancelable(false)
        val dialog = mBuilder.show()
       return dialog
    }

}