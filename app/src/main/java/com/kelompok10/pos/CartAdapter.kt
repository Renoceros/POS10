package com.kelompok10.pos


import android.view.View
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView


class CartAdapter(
    private val cartItems: MutableList<CartItem>,
    private val onItemClick: (CartItem) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    fun addItem(cartItem: CartItem) {
        cartItems.add(cartItem)
        notifyItemInserted(cartItems.size - 1)  // Notify the adapter that an item has been inserted
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = cartItems[position]
        holder.bind(cartItem)
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textId: TextView = itemView.findViewById(R.id.textIdCartItem)
        private val textName: TextView = itemView.findViewById(R.id.textNameCartItem)
        private val textQuantity: TextView = itemView.findViewById(R.id.textQuantityCartItem)
        private val textPrice: TextView = itemView.findViewById(R.id.textPriceCartItem)
        private val textSubtotal: TextView = itemView.findViewById(R.id.textSubtotCartItem)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val clickedItem = cartItems[position]
                    onItemClick.invoke(clickedItem)
                }
            }
        }

        fun bind(cartItem: CartItem) {
            textId.text = cartItem.id
            textName.text = cartItem.name
            textQuantity.text = cartItem.quantity.toString()
            textPrice.text = cartItem.price.toString()
            textSubtotal.text = cartItem.subTot.toString()
        }
    }
}


