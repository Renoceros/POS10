package com.kelompok10.pos

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.lang.Exception

// Define Retrofit interface for API calls
class MainActivity : AppCompatActivity() {

    private lateinit var UserIDInput: EditText
    private lateinit var PasswordInput: EditText
    private lateinit var LoginButton: Button

    // Use the DatabaseHelper for authentication
    private val dbHelper by lazy { DatabaseHelper(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        UserIDInput = findViewById(R.id.UserID_Input)
        PasswordInput = findViewById(R.id.Password_Input)
        LoginButton = findViewById(R.id.Login_Button)

        LoginButton.setOnClickListener {
            val id = UserIDInput.text.toString()
            val idval = id.toInt()
            val password = PasswordInput.text.toString()

            lifecycleScope.launch {
                try {
                    // Authenticate user using DatabaseHelper
                    val user = dbHelper.authenticateUser(idval, password)

                    // Check the user's position and navigate accordingly
                    when (user?.position) {
                        "Admin" -> {
                            // Save the user information to UserManager
                            UserManager.currentUser = user

                            val intent = Intent(this@MainActivity, Home_Admin_Activity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        }
                        "Cashier" -> {
                            // Save the user information to UserManager
                            UserManager.currentUser = user

                            val intent = Intent(this@MainActivity, Home_Cashier_Activity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        }
                        else -> {
                            showGenericErrorPopup("Wrong ID or Password")
                        }
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    showGenericErrorPopup("${e.message}")
                    Log.d(CONST.OoH, "Login Error: ${e.message}")
                }
            }
        }
    }



    private fun showGenericErrorPopup(errorCode: String) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.error_popup)
        val textId = dialog.findViewById<TextView>(R.id.textId)
        val btnDismiss = dialog.findViewById<Button>(R.id.btnDissmiss)

        textId.text = "$errorCode"

        btnDismiss.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}