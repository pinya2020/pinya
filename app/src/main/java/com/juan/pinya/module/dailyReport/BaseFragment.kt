package com.juan.pinya.module.dailyReport

import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
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

    open fun showCarIdDialog() {
        CarDialogFragment().also { carDialogFragment ->
            carDialogFragment.setTargetFragment(this, DailyReportFragment.CAR_ID_REQUEST)
            carDialogFragment.show(parentFragmentManager, CarDialogFragment::class.simpleName)
        }
    }

}