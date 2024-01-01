package com.kelompok10.pos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextClock
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class Home_Admin_Activity : AppCompatActivity() {

    private lateinit var pageTitle: TextView
    private lateinit var textClock: TextClock
    private lateinit var profileButton: ImageButton
    private lateinit var scannerButton: ImageButton
    private lateinit var historyButton: ImageButton
    private lateinit var stockButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_admin_activity)
        //binding = ActivityMainBinding.inflate(layoutInflater)
        //setContentView(binding.root)

        pageTitle = findViewById(R.id.Page_Title)
        textClock = findViewById(R.id.textClock)
        profileButton = findViewById(R.id.Profile_Button)
        scannerButton = findViewById(R.id.Scanner_Button)
        historyButton = findViewById(R.id.History_Button)
        stockButton = findViewById(R.id.Stock_Button)

        // Inside Home_Admin_Activity or Home_Cashier_Activity onCreate method
        val user = UserManager.currentUser

// Now you can use user?.userId, user?.name, user?.position, etc.

        // Set initial values or perform other setup

        // Example: Set a click listener for the profile button
        profileButton.setOnClickListener {
            // Handle profile button click
            // For testing, you can display a toast message or navigate to another activity
            // Replace the following line with your actual logic
            startActivity(Intent(this, ProfileActivity::class.java))
        }
        scannerButton.setOnClickListener {
            startActivity(Intent(this, ScannerAdminActivity::class.java))
        }

        historyButton.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        stockButton.setOnClickListener {
            startActivity(Intent(this, StockActivity::class.java))
        }
    }

}
