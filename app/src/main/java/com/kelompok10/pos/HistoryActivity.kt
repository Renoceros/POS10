package com.kelompok10.pos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextClock
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class HistoryActivity : AppCompatActivity() {

    private lateinit var pageTitle: TextView
    private lateinit var textClock: TextClock
    private lateinit var profileButton: ImageButton
    private lateinit var TransBttn : ImageButton
    private lateinit var PurchBttn : ImageButton
    private lateinit var LogBttn : ImageButton
    private lateinit var TransText : TextView
    private lateinit var PurchText : TextView
    private lateinit var LogText : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.history_activity)
        pageTitle = findViewById(R.id.Page_Title)
        textClock = findViewById(R.id.textClock)
        profileButton = findViewById(R.id.Profile_Button)
        TransBttn = findViewById(R.id.Transactions_Button)
        PurchBttn = findViewById(R.id.Purchases_Button)
        LogBttn = findViewById(R.id.Logs_Button)
        TransText = findViewById(R.id.Transactions_Text)
        PurchText = findViewById(R.id.Purchases_Text)
        LogText = findViewById(R.id.Logs_Text)

        TransBttn.setOnClickListener {
            val intent = Intent(this, TransactionsActivity::class.java)
            startActivity(intent)
        }
        PurchBttn.setOnClickListener {
            val intent = Intent(this, PurchasesActivity::class.java)
            startActivity(intent)
        }
        LogBttn.setOnClickListener {
            val intent = Intent(this, LogsActivity::class.java)
            startActivity(intent)
        }
    }
}
