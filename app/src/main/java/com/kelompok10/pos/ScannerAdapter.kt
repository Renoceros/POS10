package com.kelompok10.pos


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class ScannerAdapter(
    private val cartItems: MutableList<CartItem>,
    private val onItemClick: (CartItem) -> Unit,
    private val onEditClick: (CartItem) -> Unit
) : RecyclerView.Adapter<ScannerAdapter.CartViewHolder>() {
    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textId: TextView = itemView.findViewById(R.id.textIdCartItem)
        private val textName: TextView = itemView.findViewById(R.id.textNameCartItem)
        private val textQuantity: TextView = itemView.findViewById(R.id.textQuantityCartItem)
        private val textPrice: TextView = itemView.findViewById(R.id.textPriceCartItem)
        private val textSubtotal: TextView = itemView.findViewById(R.id.textSubtotCartItem)

        init {
            Log.d(CONST.OoH,"ScannerAdapter : A")
            itemView.setOnClickListener {
                Log.d(CONST.OoH,"ScannerAdapter : B")
                val position = adapterPosition
                Log.d(CONST.OoH,"ScannerAdapter : C")
                if (position != RecyclerView.NO_POSITION) {
                    Log.d(CONST.OoH,"ScannerAdapter : C1")
                    val clickedItem = cartItems[position]
                    Log.d(CONST.OoH,"ScannerAdapter : C2")
                    onItemClick.invoke(clickedItem)
                    Log.d(CONST.OoH,"ScannerAdapter : D")
                }
                Log.d(CONST.OoH,"ScannerAdapter : E")
            }
            Log.d(CONST.OoH,"ScannerAdapter : F")
            itemView?.findViewById<Button>(R.id.editCartItemBttn)?.setOnClickListener {
                Log.d(CONST.OoH,"ScannerAdapter : G")
                val position = adapterPosition
                Log.d(CONST.OoH,"ScannerAdapter : H")
                if (position != RecyclerView.NO_POSITION) {
                    Log.d(CONST.OoH,"ScannerAdapter : H1")
                    val clickedItem = cartItems[position]
                    Log.d(CONST.OoH,"ScannerAdapter : H2")
                    onEditClick.invoke(clickedItem)
                    Log.d(CONST.OoH,"ScannerAdapter : I")
                }
                Log.d(CONST.OoH,"ScannerAdapter : J")
            }
            Log.d(CONST.OoH,"ScannerAdapter : K")
        }

        fun bind(cartItem: CartItem) {
            textId.text = cartItem.id.toString()
            textName.text = cartItem.name
            textQuantity.text = cartItem.quantity.toString()
            textPrice.text = cartItem.price.toString()
            textSubtotal.text = cartItem.subTot.toString()

            // Debugging log
            Log.d(CONST.OoH, "Item bound - ID: ${cartItem.id}, Name: ${cartItem.name}")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        // Debugging log
        Log.d(CONST.OoH,"ScannerAdapter : onCreateViewHolder")

        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)
        Log.d(CONST.OoH,"ScannerAdapter : Initializing View")
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        // Debugging log
        Log.d(CONST.OoH,"ScannerAdapter : onBindViewHolder - position: $position")

        val cartItem = cartItems[position]
        holder.bind(cartItem)
    }

    override fun getItemCount(): Int {
        // Debugging log
        Log.d(CONST.OoH,"ScannerAdapter : getItemCount: ${cartItems.size}")

        return cartItems.size
    }


}
