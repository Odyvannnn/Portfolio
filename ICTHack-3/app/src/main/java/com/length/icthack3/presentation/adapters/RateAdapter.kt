package com.length.icthack3.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.length.icthack3.R
import com.length.icthack3.domain.model.User
import org.w3c.dom.Text

class RateAdapter() :
    RecyclerView.Adapter<RateAdapter.ScheduleViewHolder>() {

    var users = listOf<User>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val userName: TextView = itemView.findViewById(R.id.userName)
        val userPoints: TextView = itemView.findViewById(R.id.userPoints)

        fun bind(user: User) {
            userName.text = user.token
            userPoints.text = user.balance.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.rate_recycler_item, parent, false)
        return ScheduleViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount() = users.size

}