package com.juan.pinya.module.dailyReport

import android.view.View
import com.juan.pinya.model.Company

interface CompanyClickListener {
    fun onRecyclerViewItemClick(view: View, company: Company)
}