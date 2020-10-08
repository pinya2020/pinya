package com.juan.pinya.module.dailyReport

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.juan.pinya.R
import com.juan.pinya.model.DailyReport
import com.juan.pinya.view.main.dailyReport.DRAdd1Fragment
import kotlinx.android.synthetic.main.item_addlist.view.*

class DRAdd1Adapter(options: FirestoreRecyclerOptions<DailyReport>,
                    private val listener: DRAdd1Fragment
) :
    FirestoreRecyclerAdapter<DailyReport, DRAdd1Adapter.DRA1ViewHoder>(options) {

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