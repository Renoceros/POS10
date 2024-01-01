package com.kelompok10.pos
import com.kelompok10.pos.User
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.TextClock
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {

    private lateinit var pageTitle: TextView
    private lateinit var textClock: TextClock
    private lateinit var userIDView: TextView
    private lateinit var nameView: TextView
    private lateinit var positionView: TextView
    private lateinit var logOutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_activity)

        // Initialize views
        pageTitle = findViewById(R.id.Page_Title)
        textClock = findViewById(R.id.textClock)
        userIDView = findViewById(R.id.UserIDView)
        nameView = findViewById(R.id.NameView)
        positionView = findViewById(R.id.PssView)
        logOutButton = findViewById(R.id.LogOutBttn)

        // Retrieve user information from Intent extras
        val intent = intent
        val userID = intent.getStringExtra("userID")
        val name = intent.getStringExtra("name")
        val position = intent.getStringExtra("position")
        // Inside ProfileActivity onCreate method
        val user = UserManager.currentUser

        // Display user details in the TextViews
        userIDView.text = "User ID: ${user?.userId}"
        nameView.text = "Name: ${user?.name}"
        positionView.text = "Position: ${user?.position}"

        logOutButton.setOnClickListener {
            // Handle logout logic here if needed
            val intent = Intent(this@ProfileActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent) // Close the ProfileActivity
        }
    }
}
