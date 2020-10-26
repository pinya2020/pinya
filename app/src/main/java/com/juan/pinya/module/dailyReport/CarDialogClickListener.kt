package com.juan.pinya.module.dailyReport

import android.view.View
import com.juan.pinya.model.Car

interface CarDialogClickListener {
    fun onRecyclerViewItemClick(view: View, car: Car)

}