package com.juan.pinya.view.main.dailyReport.site

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.juan.pinya.R
import com.juan.pinya.model.Site
import kotlinx.android.synthetic.main.item_addlist.view.*

class SiteAdapter(options: FirestoreRecyclerOptions<Site>,
                  private val listener: SiteFragment
) :
    FirestoreRecyclerAdapter<Site, SiteAdapter.DRAdd2ViewHoder>(options) {

    private var selectedPosition = -1

    class DRAdd2ViewHoder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var siteTextView = itemView.add_item_textView

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DRAdd2ViewHoder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_addlist, parent, false)
        return DRAdd2ViewHoder(itemView)
    }

    override fun onBindViewHolder(
        holder: DRAdd2ViewHoder,
        position: Int,
        site: Site
    ) {
        if (selectedPosition == position) {
            holder.siteTextView.setBackgroundResource(R.drawable.textview_select_item_background)
        } else {
            holder.siteTextView.setBackgroundResource(R.drawable.textview_item_background)
        }
        holder.siteTextView.text = site.site

        holder.itemView.setOnClickListener{
            holder.siteTextView.setBackgroundResource(R.drawable.textview_select_item_background)
            listener.onRecyclerViewItemClick(holder.itemView, site)
            notifyItemChanged(selectedPosition)
            selectedPosition = position
        }
    }
}