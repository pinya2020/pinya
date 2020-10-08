package com.juan.pinya.module.dailyReport

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.juan.pinya.R
import com.juan.pinya.model.DailyReport
import kotlinx.android.synthetic.main.item_addlist.view.*

class DRAdd3Adapter(options: FirestoreRecyclerOptions<DailyReport>,
//                    private val listener: DRAdd4Fragment
) :
    FirestoreRecyclerAdapter<DailyReport, DRAdd3Adapter.DRA3ViewHoder>(options) {

    class DRA3ViewHoder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var company = itemView.add_company_textView

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DRA3ViewHoder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_addlist, parent, false)
        return DRA3ViewHoder(itemView)
    }

    override fun onBindViewHolder(
        holder: DRA3ViewHoder,
        position: Int,
        dailyReport: DailyReport
    ) {
        holder.company.text = dailyReport.site

        holder.itemView.setOnClickListener{
            listener.onRecyclerViewItemClick(holder.itemView, dailyReport)
        }
    }
}