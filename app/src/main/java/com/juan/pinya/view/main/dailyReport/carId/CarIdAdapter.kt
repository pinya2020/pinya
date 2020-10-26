package com.juan.pinya.view.main.dailyReport.carId

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.juan.pinya.R
import com.juan.pinya.model.Car
import kotlinx.android.synthetic.main.item_addlist.view.*

class CarIdAdapter(options: FirestoreRecyclerOptions<Car>,
                   private val listener: CarDialogFragment
) : FirestoreRecyclerAdapter<Car, CarIdAdapter.CarIdViewHolder>(options) {

    private var selectedPosition = -1

    class CarIdViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val carIdTextView = itemView.add_company_textView

        fun onBind(car: Car) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarIdViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_view_car_id, parent, false)
        return CarIdViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: CarIdViewHolder,
        position: Int,
        car: Car
    ) {
        holder.onBind(car)
        if (selectedPosition == position) {
            holder.carIdTextView.setBackgroundResource(R.drawable.textview_select_round_border)
        } else {
            holder.carIdTextView.setBackgroundResource(R.drawable.textview_round_border)
        }
        holder.carIdTextView.text = car.carId

        holder.itemView.setOnClickListener{
            holder.carIdTextView.setBackgroundResource(R.drawable.textview_select_round_border)
            listener.onRecyclerViewItemClick(holder.itemView, car)
            notifyItemChanged(selectedPosition)
            selectedPosition = position
        }
    }
}