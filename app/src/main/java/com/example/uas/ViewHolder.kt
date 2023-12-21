package com.example.uas

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.uas.data.SpendingItem
import java.util.Locale

class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
    val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
    val amountTextView: TextView = itemView.findViewById(R.id.amountTextView)
    val dateTextView: TextView = itemView.findViewById(R.id.descTextView)

    fun bind(item: SpendingItem) {
        nameTextView.text = item.name
        amountTextView.text = formatPrice(item.amount)
        dateTextView.text = item.date
    }

    fun formatPrice(price: Double): String {
        val formatter = java.text.NumberFormat.getInstance(Locale("id", "ID"))
        return formatter.format(price)
    }

}
