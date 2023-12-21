package com.example.uas.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.uas.R
import com.example.uas.ViewHolder
import com.example.uas.data.SpendingItem

class RVAdapter(private var itemList: List<SpendingItem>) : RecyclerView.Adapter<ViewHolder>() {


    fun updateData(newList: List<SpendingItem>) {
        itemList = newList
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_spending, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    // Add this method to update the data in the adapter

}