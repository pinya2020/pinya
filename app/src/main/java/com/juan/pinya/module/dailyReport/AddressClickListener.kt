package com.juan.pinya.module.dailyReport

import android.view.View
import com.juan.pinya.model.Address

interface AddressClickListener {
    fun onRecyclerViewItemClick(view: View, address: Address)

}