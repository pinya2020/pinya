package com.juan.pinya.view.main.expenditure

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.juan.pinya.R
import com.juan.pinya.model.Expenditure
import kotlinx.android.synthetic.main.item_expenditure.view.*
import java.text.SimpleDateFormat
import java.util.*

class ExpenditureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {
    var expenditure: Expenditure ?= null
    private val format = SimpleDateFormat("MM/dd", Locale.getDefault())
    private val context: Context
        get() = itemView.context
    val typeTextView = itemView.type_textView
    val carIdTextView = itemView.carId_textView
    val dateTextView = itemView.date_TextView
    val priceTextView = itemView.price_textView
    val psTextView = itemView.ps_textView
    val psTableRow = itemView.ps_tableRow

  fun onBind(expenditure: Expenditure , listener: (Expenditure) -> Unit){
      this.expenditure = expenditure

      typeTextView.text = expenditure.type
      carIdTextView.text = expenditure.carId
      dateTextView.text = format.format(expenditure.date.toDate())
      priceTextView.text = String.format(Locale.getDefault(), context.getString(R.string.format_price), expenditure.price)

      if( expenditure.ps.isNotEmpty()){
          psTableRow.visibility = View.VISIBLE
          psTextView.text = expenditure.ps
      }else{
          psTableRow.visibility = View.GONE
      }

      itemView.setOnLongClickListener {
          //invoke?
          listener.invoke(expenditure)
          true
      }
  }

}