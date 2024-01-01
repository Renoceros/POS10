package com.kelompok10.pos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ReceiptItemAdapter(private val itemList: List<CartItem>) :
    RecyclerView.Adapter<ReceiptItemAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.textNameReceiptItem)
        val quantityTextView: TextView = itemView.findViewById(R.id.textQuantityReceiptItem)
        val priceTextView: TextView = itemView.findViewById(R.id.textPriceReceiptItem)
        val subTotalTextView: TextView = itemView.findViewById(R.id.textSubtotReceiptItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.receipt_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = itemList[position]

        // Bind your data to the ViewHolder's views
        holder.nameTextView.text = currentItem.name
        holder.quantityTextView.text = currentItem.quantity.toString()
        holder.priceTextView.text = currentItem.price.toString()
        holder.subTotalTextView.text = currentItem.subTot.toString()
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}
