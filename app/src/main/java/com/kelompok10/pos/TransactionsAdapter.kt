package com.kelompok10.pos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TransactionsAdapter : RecyclerView.Adapter<TransactionsAdapter.ViewHolder>() {
    private var transactionHeaders: List<TransactionHeader> = emptyList()
    private var onItemClickListener: ((TransactionHeader) -> Unit)? = null

    // ViewHolder class (create this class inside TransactionAdapter)
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Define your UI components here
        val dateTimeTextView: TextView = itemView.findViewById(R.id.DateTimeTextView)
        val headerIdTextView: TextView = itemView.findViewById(R.id.headerIdTextView)
        val userIdTextView: TextView = itemView.findViewById(R.id.userIdTextView)
        val totalAmountTextView: TextView = itemView.findViewById(R.id.totalamountTextView)
    }

    // Set data to the adapter
    fun setTransactionHeaders(transactionHeaders: List<TransactionHeader>) {
        this.transactionHeaders = transactionHeaders
        notifyDataSetChanged()
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.transaction_items, parent, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transactionHeader = transactionHeaders[position]

        // Bind data to UI components
        holder.dateTimeTextView.text = transactionHeader.dateTime.toString()
        holder.headerIdTextView.text = transactionHeader.id.toString()
        holder.userIdTextView.text = transactionHeader.userId.toString()
        holder.totalAmountTextView.text = transactionHeader.totalAmount.toString()
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return transactionHeaders.size
    }
    fun updateData(newData: List<TransactionHeader>) {
        transactionHeaders = newData
        notifyDataSetChanged()
    }
    fun setOnItemClickListener(listener: (TransactionHeader) -> Unit) {
        onItemClickListener = listener
    }
    inner class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dateTimeTextView: TextView = itemView.findViewById(R.id.DateTimeTextView)
        private val headerIdTextView: TextView = itemView.findViewById(R.id.headerIdTextView)
        private val userIdTextView: TextView = itemView.findViewById(R.id.userIdTextView)
        private val totalAmountTextView: TextView = itemView.findViewById(R.id.totalamountTextView)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val clickedItem = transactionHeaders[position]
                    onItemClickListener?.invoke(clickedItem)
                }
            }
        }

        fun bind(transactionHeader: TransactionHeader) {
            dateTimeTextView.text = transactionHeader.dateTime.toString()
            headerIdTextView.text = transactionHeader.id.toString()
            userIdTextView.text = transactionHeader.userId.toString()
            totalAmountTextView.text = transactionHeader.totalAmount.toString()
        }
    }
}
