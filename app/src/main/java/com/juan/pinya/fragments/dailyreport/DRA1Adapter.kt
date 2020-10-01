package com.juan.pinya.fragments.dailyreport

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.juan.pinya.R
import com.juan.pinya.model.DailyReport
import kotlinx.android.synthetic.main.item_addlist.view.*

class DRA1Adapter(options: FirestoreRecyclerOptions<DailyReport>,
                  private val listener: DailyReportAdd1Fragment
) :
    FirestoreRecyclerAdapter<DailyReport, DRA1Adapter.DRA1ViewHoder>(options) {

    class DRA1ViewHoder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var company = itemView.add_company_textView

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DRA1ViewHoder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_addlist, parent, false)
        return DRA1ViewHoder(itemView)
    }

    override fun onBindViewHolder(
        holder: DRA1ViewHoder,
        position: Int,
        dailyReport: DailyReport
    ) {
        holder.company.text = dailyReport.company

        holder.itemView.setOnClickListener{
            listener.onRecyclerViewItemClick(holder.itemView, dailyReport)
        }
    }
}