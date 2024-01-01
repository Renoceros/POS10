package com.kelompok10.pos

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextClock
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TransactionsActivity : AppCompatActivity() {
    private lateinit var pageTitle: TextView
    private lateinit var textClock: TextClock
    private lateinit var profileButton: ImageButton
    private lateinit var editHeaderIdText: EditText
    private lateinit var searchButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var transactionsAdapter: TransactionsAdapter // Replace with your actual Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.transactions_activity)

        // Initialize RecyclerView and Adapter
        recyclerView = findViewById(R.id.recyclerView)
        transactionsAdapter = TransactionsAdapter()

        // Set the adapter to the RecyclerView
        recyclerView.adapter = transactionsAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Call getAllTransactionHeaders and update the adapter data
        val db = DatabaseHelper(this)
        val transactionHeaders = db.getAllTransactionHeaders()

        // Update the adapter data with the list of transaction headers
        transactionsAdapter.setTransactionHeaders(transactionHeaders)


        // Initialize views
        pageTitle = findViewById(R.id.Page_Title)
        textClock = findViewById(R.id.textClock)
        profileButton = findViewById(R.id.Profile_Button)
        editHeaderIdText = findViewById(R.id.editHeaderIdText)
        searchButton = findViewById(R.id.SearchButton)
        recyclerView = findViewById(R.id.recyclerView)

        // Set up RecyclerView
        transactionsAdapter = TransactionsAdapter() // Replace with your actual Adapter
        recyclerView.adapter = transactionsAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Set OnClickListener for the Search button
        searchButton.setOnClickListener {
            val headerId = editHeaderIdText.text.toString().toLongOrNull()

            if (headerId != null) {
                val transactionHeader = db.getTransactionHeaderById(headerId)

                if (transactionHeader != null) {
                    val dataList = listOf(transactionHeader) // Replace with your data structure
                    transactionsAdapter.updateData(dataList)
                } else {
                    showToast("Header Id Not Found")
                }
            } else {
                Log.d(CONST.OoH,"Wrong Data Type")
            }
        }

        transactionsAdapter.setOnItemClickListener { clickedTransactionHeader ->
            // Handle item click here
            // You can check the user type and perform the appropriate action
            if (UserManager.currentUser?.position == "Cashier") {
                // Open the PDF or perform the action for Cashier
                // You may need to pass additional data to open the PDF if needed
                openPDF(clickedTransactionHeader)
            } else if (UserManager.currentUser?.position == "Admin") {
                // Show the options popup for Admin
                showAdminOptionsPopup(clickedTransactionHeader)
            }
        }
    }
    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
    private fun openPDF(transactionHeader: TransactionHeader) {
        // TODO: Implement PDF opening logic using the generated PDF
        // You can use an Intent to open a PDF viewer, for example
        showToast("Opening PDF for Cashier")
    }
    private fun showAdminOptionsPopup(transactionHeader: TransactionHeader) {

    }
}
