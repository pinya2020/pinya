package com.juan.pinya.module.Expenditure

import android.view.View
import com.juan.pinya.model.Expenditure

interface ExpenditureClickListener {
    fun onRecyclerViewItemClick(view: View, expenditure: Expenditure)

}