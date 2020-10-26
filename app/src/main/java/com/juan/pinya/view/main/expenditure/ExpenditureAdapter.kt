package com.juan.pinya.view.main.expenditure

import android.view.LayoutInflater
import android.view.ViewGroup
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.juan.pinya.R
import com.juan.pinya.model.Expenditure

class ExpenditureAdapter(
    options: FirestoreRecyclerOptions<Expenditure>,
    private val listener: (Expenditure) -> Unit
) :
    FirestoreRecyclerAdapter<Expenditure, ExpenditureViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenditureViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_expenditure, parent, false)
        return ExpenditureViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: ExpenditureViewHolder,
        position: Int,
        expenditure: Expenditure
    ) {
        holder.onBind(expenditure, listener)
    }
}
