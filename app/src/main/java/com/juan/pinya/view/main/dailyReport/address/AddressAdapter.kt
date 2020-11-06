package com.juan.pinya.view.main.dailyReport.address

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.juan.pinya.R
import com.juan.pinya.model.Address
import kotlinx.android.synthetic.main.item_addlist.view.add_item_textView
import kotlinx.android.synthetic.main.item_addresslist.view.*
import java.util.*

class AddressAdapter(
    options: FirestoreRecyclerOptions<Address>,
    private val listener: AddressFragment
) :
    FirestoreRecyclerAdapter<Address, AddressAdapter.DRAdd3ViewHoder>(options) {

    private var selectedPosition = -1

    class DRAdd3ViewHoder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val addressTextView = itemView.add_item_textView
        val priceTextView = itemView.add_cost_textView

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DRAdd3ViewHoder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_addresslist, parent, false)
        return DRAdd3ViewHoder(itemView)
    }

    override fun onBindViewHolder(
        holder: DRAdd3ViewHoder,
        position: Int,
        address: Address
    ) {
        if (selectedPosition == position) {
            holder.addressTextView.setBackgroundResource(R.drawable.textview_select_item_background)
            holder.priceTextView.setBackgroundResource(R.drawable.textview_select_item_background)
        } else {
            holder.addressTextView.setBackgroundResource(R.drawable.textview_item_background)
            holder.priceTextView.setBackgroundResource(R.drawable.textview_item_background)
        }
        holder.addressTextView.text = address.address
        holder.priceTextView.text = String.format(Locale.getDefault(),
            holder.priceTextView.context.getString(R.string.format_price),
            address.cost)
        holder.itemView.setOnClickListener {
            holder.addressTextView.setBackgroundResource(R.drawable.textview_select_item_background)
            holder.priceTextView.setBackgroundResource(R.drawable.textview_select_item_background)
            listener.onRecyclerViewItemClick(holder.itemView, address)
            notifyItemChanged(selectedPosition)
            selectedPosition = position
        }
    }
}