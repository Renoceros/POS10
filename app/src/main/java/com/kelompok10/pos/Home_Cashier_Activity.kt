package com.kelompok10.pos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextClock
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class Home_Cashier_Activity : AppCompatActivity() {

    private lateinit var pageTitle: TextView
    private lateinit var textClock: TextClock
    private lateinit var profileButton: ImageButton
    private lateinit var scannerButton: ImageButton
    private lateinit var historyButton: ImageButton
    private lateinit var stockButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_cashier_activity)
        //binding = ActivityMainBinding.inflate(layoutInflater)
        //setContentView(binding.root)

        pageTitle = findViewById(R.id.Page_Title)
        textClock = findViewById(R.id.textClock)
        profileButton = findViewById(R.id.Profile_Button)
        scannerButton = findViewById(R.id.Scanner_Button)
        historyButton = findViewById(R.id.History_Button)
        stockButton = findViewById(R.id.Stock_Button)


        // Set initial values or perform other setup

        // Example: Set a click listener for the profile button
        profileButton.setOnClickListener {
            // Handle profile button click
            // For testing, you can display a toast message or navigate to another activity
            // Replace the following line with your actual logic
            Log.d(CONST.OoH,"Home_Cashier_activity : opening ProfileActivity")
            startActivity(Intent(this, ProfileActivity::class.java))
        }
        scannerButton.setOnClickListener {
            Log.d(CONST.OoH,"Home_Cashier_activity : opening ScannerActivity")
            startActivity(Intent(this, ScannerActivity::class.java))
        }

        historyButton.setOnClickListener {
            Log.d(CONST.OoH,"Home_Cashier_activity : opening HistoryActivity")
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        stockButton.setOnClickListener {
            Log.d(CONST.OoH,"Home_Cashier_activity : opening StockActivity")
            startActivity(Intent(this, StockActivity::class.java))
        }
    }

}
