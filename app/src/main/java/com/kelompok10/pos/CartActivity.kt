package com.kelompok10.pos

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kelompok10.pos.databinding.CartActivityBinding
import com.kelompok10.pos.DatabaseHelper
class CartActivity : AppCompatActivity() {

    private lateinit var binding: CartActivityBinding
    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var cartAdapter: CartAdapter
    val checkoutButton: Button = binding.CheckoutButton
    private var cartItems: List<CartItem> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CartActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve cart items from the intent
        cartItems = intent.getSerializableExtra("cartItems") as List<CartItem>

        // Set up RecyclerView
        cartRecyclerView = binding.recyclerView // Use the correct ID here ('recyclerView' from your XML layout)
        cartAdapter = CartAdapter(cartItems) { clickedItem ->
            // Handle item click here
            // You can open the edit_cart_popup or perform any other action
            showEditCartPopup(clickedItem)
        }

        cartRecyclerView.layoutManager = LinearLayoutManager(this)
        cartRecyclerView.adapter = cartAdapter

        checkoutButton.setOnClickListener {
            // Create an intent to start the CheckoutActivity
            val intent = Intent(this, CheckoutActivity::class.java)

            // Pass any data you need to the CheckoutActivity using intent extras
            // For example, you can pass the list of cart items
            intent.putExtra("cartItems", ArrayList(cartItems))

            // Start the CheckoutActivity
            startActivity(intent)
        }

    }

    private fun showEditCartPopup(cartItem: CartItem) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.edit_cart_popup)

        val textId = dialog.findViewById<TextView>(R.id.textId)
        val textName = dialog.findViewById<TextView>(R.id.textName)
        val textQuantity = dialog.findViewById<TextView>(R.id.textQuantity)
        val textQuantityValue = dialog.findViewById<TextView>(R.id.textQuantityValue)
        val btnDecrease = dialog.findViewById<Button>(R.id.btnDecrease)
        val btnIncrease = dialog.findViewById<Button>(R.id.btnIncrease)
        val btnEditCartItem = dialog.findViewById<Button>(R.id.btnAddToCart)

        textId.text = "ID: ${cartItem.id}"
        textName.text = "Name: ${cartItem.name}"
        textQuantity.text = "Quantity: "

        var quantity = cartItem.quantity
        textQuantityValue.text = quantity.toString()

        btnDecrease.setOnClickListener {
            if (quantity > 1) {
                quantity--
                textQuantityValue.text = quantity.toString()
            }
        }

        btnIncrease.setOnClickListener {
            // Check if quantity is within available stock
            if (quantity < getStockQuantityFromDatabase(cartItem.id)) {
                quantity++
                textQuantityValue.text = quantity.toString()
            }
            else {
                // Optionally, you can display a message indicating that the stock is limited.
                Toast.makeText(this, "Reached maximum stock limit", Toast.LENGTH_SHORT).show()
            }
        }

        btnEditCartItem.setOnClickListener {
            // Update the quantity of the cartItem
            cartItem.quantity = quantity

            // Optionally, you can display a message or update UI to indicate the item is updated in the cart
            Toast.makeText(this, "Cart item updated: ${cartItem.name} (Quantity: $quantity)", Toast.LENGTH_SHORT).show()

            // Dismiss the popup
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun getStockQuantityFromDatabase(productId: String): Int {
        // Fetch the product from the database using the productId
        val dbHelper = DatabaseHelper(this)
        val product = dbHelper.getProductById(productId.toInt()) // Assuming productId is an integer
        return product?.stockQtty ?: 0
    }

}



