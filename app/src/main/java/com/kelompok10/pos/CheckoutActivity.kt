package com.kelompok10.pos

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.kelompok10.pos.ReceiptActivity
import com.kelompok10.pos.databinding.CheckoutActivityBinding

class CheckoutActivity : AppCompatActivity() {

    private lateinit var binding: CheckoutActivityBinding
    private lateinit var finalizeButton: Button
    private lateinit var editTipText: EditText
    private lateinit var editCashText: EditText
    private lateinit var totalNetTextView: TextView
    private lateinit var taxTextView: TextView
    private lateinit var tipTextView: TextView
    private lateinit var totalTextView: TextView
    private lateinit var cashTextView: TextView
    private lateinit var changeTextView: TextView

    private lateinit var cartItems: List<CartItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CheckoutActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve cart items from the intent
        cartItems = intent.getSerializableExtra("cartItems") as List<CartItem>

        initializeViews()

        // Set initial values
        updateTotals()

        // Set listeners
        finalizeButton.setOnClickListener {
            finalizeOrder()
        }
        editTipText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                updateTotals()
            }
        }
        editCashText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                updateTotals()
            }
        }
    }

    private fun initializeViews() {
        finalizeButton = binding.FinalizeButton
        editTipText = binding.editTipText
        editCashText = binding.editCashText
        totalNetTextView = binding.TotalNetTextView
        taxTextView = binding.TaxTextView
        tipTextView = binding.TipTextView
        totalTextView = binding.TotalTextView
        cashTextView = binding.CashTextView
        changeTextView = binding.ChangeTextView
    }

    private fun updateTotals() {
        val totalNet = calculateTotalNet()
        val tax = totalNet * 0.1 // 10% tax
        val tip = if (editTipText.text.toString().isNotEmpty()) editTipText.text.toString().toDouble() else 0.0
        val totalGross = totalNet + tax + tip
        val cash = if (editCashText.text.toString().isNotEmpty()) editCashText.text.toString().toDouble() else 0.0
        val change = totalGross - cash

        // Update TextViews
        totalNetTextView.text = "Total Net    : Rp $totalNet"
        taxTextView.text = "          Tax    : Rp $tax"
        tipTextView.text = "          Tip     : Rp $tip"
        totalTextView.text = "Total Gross : Rp $totalGross"
        cashTextView.text = "Cash            : Rp $cash"
        changeTextView.text = "Change       : Rp $change"
    }

    private fun calculateTotalNet(): Double {
        return cartItems.sumByDouble { it.subTot }
    }

    private fun finalizeOrder() {
        // Calculate totals
        val totalNet = calculateTotalNet()
        val tax = totalNet * 0.1 // 10% tax
        val tip = if (editTipText.text.toString().isNotEmpty()) editTipText.text.toString().toDouble() else 0.0
        val totalGross = totalNet + tax + tip
        val cash = if (editCashText.text.toString().isNotEmpty()) editCashText.text.toString().toDouble() else 0.0
        val change = totalGross - cash

        // Create intent
        val intent = Intent(this, ReceiptActivity::class.java)

        // Pass necessary information as extras
        intent.putExtra("totalNet", totalNet)
        intent.putExtra("tax", tax)
        intent.putExtra("tip", tip)
        intent.putExtra("totalGross", totalGross)
        intent.putExtra("cash", cash)
        intent.putExtra("change", change)
        intent.putExtra("cartItems", ArrayList(cartItems))

        // Start ReceiptActivity
        startActivity(intent)
    }

}

