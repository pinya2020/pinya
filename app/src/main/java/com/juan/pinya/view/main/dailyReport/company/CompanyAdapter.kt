package com.juan.pinya.view.main.dailyReport.company

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.juan.pinya.R
import com.juan.pinya.model.Company
import kotlinx.android.synthetic.main.item_addlist.view.*

class CompanyAdapter(options: FirestoreRecyclerOptions<Company>,
                     private val listener: CompanyFragment
) :
    FirestoreRecyclerAdapter<Company, CompanyAdapter.DRAdd1ViewHoder>(options) {

    private var selectedPosition = -1

    class DRAdd1ViewHoder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val companyTextView = itemView.add_company_textView

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DRAdd1ViewHoder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_addlist, parent, false)
        return DRAdd1ViewHoder(itemView)
    }

    override fun onBindViewHolder(
        holder: DRAdd1ViewHoder,
        position: Int,
        company: Company
    ) {
        if (selectedPosition == position) {
            holder.companyTextView.setBackgroundResource(R.drawable.textview_select_item_background)
        } else {
            holder.companyTextView.setBackgroundResource(R.drawable.textview_item_background)
        }
        holder.companyTextView.text = company.company

        holder.itemView.setOnClickListener{
            holder.companyTextView.setBackgroundResource(R.drawable.textview_select_item_background)
            listener.onRecyclerViewItemClick(holder.itemView, company)
            notifyItemChanged(selectedPosition)
            selectedPosition = position
        }
    }
}