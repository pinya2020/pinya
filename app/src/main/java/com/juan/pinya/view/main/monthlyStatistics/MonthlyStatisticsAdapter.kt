package com.juan.pinya.view.main.monthlyStatistics

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.juan.pinya.R
import com.juan.pinya.model.Statistics
import com.juan.pinya.view.main.dailyReport.carId.CarDialogFragment
import kotlin.contracts.contract

class MonthlyStatisticsAdapter(private var statisticsList: List<Statistics>,
                               private val listener: MonthlyStatisticsActivity

): RecyclerView.Adapter<MonthlyStatisticsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthlyStatisticsViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_monthly_statistics, parent, false)
        return MonthlyStatisticsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MonthlyStatisticsViewHolder, position: Int) {
        holder.companyTextView.text = statisticsList[position].company
        if (statisticsList[position].expenditure){
            holder.meterTextView.setTextColor(Color.parseColor("#FF3C30"))
        }else{
            holder.meterTextView.setTextColor(Color.parseColor("#318014"))
        }
        holder.meterTextView.text = statisticsList[position].meter.toString()
        holder.itemView.setOnClickListener {
            listener.onRecyclerViewItemClick(holder.itemView, statisticsList[position])
        }
    }

    override fun getItemCount(): Int {
        return statisticsList.size
    }
    fun updateList(newStatisticsList: List<Statistics>){
        statisticsList = newStatisticsList

    }
}
