package com.juan.pinya.module.dailyReport

import android.view.View
import com.juan.pinya.model.Site

interface SiteClickListener {
    fun onRecyclerViewItemClick(view: View, site: Site)

}