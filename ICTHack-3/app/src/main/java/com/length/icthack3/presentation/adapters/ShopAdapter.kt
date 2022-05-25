package com.length.icthack3.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.length.icthack3.R
import com.length.icthack3.domain.model.Animal
import com.length.icthack3.domain.model.User

class ShopAdapter() :
    RecyclerView.Adapter<ShopAdapter.ScheduleViewHolder>() {

    var animals = listOf<Animal>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {



        fun bind(animals: Animal) {
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.shop_recycler_item, parent, false)
        return ScheduleViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        holder.bind(animals[position])
    }

    override fun getItemCount() = animals.size

}