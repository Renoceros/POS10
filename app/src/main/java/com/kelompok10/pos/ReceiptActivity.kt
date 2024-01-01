package com.kelompok10.pos
import android.content.Intent
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextClock
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import kotlin.text.StringBuilder
import java.time.LocalDateTime
import java.util.Date
import java.util.Locale

class ReceiptActivity : AppCompatActivity() {
    private lateinit var pageTitle: TextView
    private lateinit var textClock: TextClock
    private lateinit var profileButton: ImageButton
    private lateinit var printButton: ImageButton
    private lateinit var returnHomeButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: CustomerViewModel // Replace with your actual ViewModel
    private lateinit var cartItems: List<CartItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.receipt_activity)

        // Initialize views
        pageTitle = findViewById(R.id.Page_Title)
        textClock = findViewById(R.id.textClock)
        profileButton = findViewById(R.id.Profile_Button)
        printButton = findViewById(R.id.PrintBttn)
        returnHomeButton = findViewById(R.id.ReturnHomeButton)
        recyclerView = findViewById(R.id.recyclerView)


        // Set up RecyclerView
        var adapter = ReceiptItemAdapter(cartItems)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Retrieve data from Intent extras
        val totalNet = intent.getDoubleExtra("totalNet", 0.0)
        val tax = intent.getDoubleExtra("tax", 0.0)
        val tip = intent.getDoubleExtra("tip", 0.0)
        val totalGross = intent.getDoubleExtra("totalGross", 0.0)
        val cash = intent.getDoubleExtra("cash", 0.0)
        val change = intent.getDoubleExtra("change", 0.0)
        var cartItems = intent.getSerializableExtra("cartItems") as List<CartItem>
        val dateFormat = SimpleDateFormat("dd.MM.yyyy-HH:mm", Locale.getDefault())
        val currentDateTime = Date()
        val receiptDT = dateFormat.format(currentDateTime).toString()
        findViewById<TextView>(R.id.totalNetTextView).text = totalNet.toString() // Updated text
        findViewById<TextView>(R.id.taxTextView).text = tax.toString() // Updated text
        findViewById<TextView>(R.id.tipTextView).text = tip.toString() // Updated text
        findViewById<TextView>(R.id.totalGrossTextView).text = totalGross.toString() // Updated text
        findViewById<TextView>(R.id.cashTextView).text = cash.toString() // Updated text
        findViewById<TextView>(R.id.changeTextView).text = change.toString() // Updated text
        findViewById<TextView>(R.id.DateTimeTextView).text = receiptDT
        // Now you can use these values to createTransactionHeader and modify stock
        val db = DatabaseHelper(this)
        // Add customer and update ViewModel
        if (db.addCustomer()) {
            viewModel.currentCustomerId = db.getCurrentCustomerId()
        }


        // Retrieve user data (replace with your actual logic)


        // Create TransactionHeader
        val user = UserManager.currentUser

        val transactionHeader: TransactionHeader? = user?.let {
            val header = TransactionHeader(
                id = 0,
                customerId = viewModel.currentCustomerId?.toIntOrNull() ?: 0,
                userId = it.userId,
                totalAmount = totalGross,
                amountPaid = cash,
                tip = tip,
                changes = change,
                dateTime = LocalDateTime.now()
            )
            findViewById<TextView>(R.id.UserIdNameTextView).text = "${it.userId}/${it.name}" // Updated text
            header
        }

        // Add TransactionHeader to the database
        val transactionHeaderId = transactionHeader?.let { db.addTransactionHeader(it) }

        // Create TransactionDetails and update stock
        // Inside the loop where you process each cart item
        for (cartItem in cartItems) {
            val product = db.getProductById(cartItem.id.toInt())
            if (product != null) {
                val transactionDetail = transactionHeaderId?.let {
                    TransactionDetail(
                        tDetailId = 0,
                        tHeaderId = it,
                        productId = product.prodId,
                        saleQuantity = cartItem.quantity,
                        salePrice = product.price,
                        subTotal = cartItem.subTot
                    )
                }

                // Add TransactionDetail to the database
                if (transactionDetail != null) {
                    db.addTransactionDetail(transactionDetail)
                }

                // Update stock
                val updatedStockQty = product.stockQtty - cartItem.quantity
                db.saveEditProduct(product.prodId, product.name, product.price, updatedStockQty)
            }
        }


        // Update TextViews with receipt details
        findViewById<TextView>(R.id.HeaderIdTextView).text = transactionHeaderId.toString()
        // Add more TextView updates as needed
        // Set OnClickListener for the Print button
        printButton.setOnClickListener {
            generatePrintableDocument()
            launchPrintDialog()
        }

        // Set OnClickListener for the Return Home button
        returnHomeButton.setOnClickListener {
            // Clear CartItems and PurchaseItems Lists
            cartItems = emptyList()
            // Clear other lists or data structures as needed
            // Create an intent to navigate to Home_Cashier_Activity
            val intent = Intent(this, Home_Cashier_Activity::class.java)
            // Add any necessary extras to the intent
            // intent.putExtra("key", value)
            // Set flags to clear the back stack
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            // Start the Home_Cashier_Activity and finish the current ReceiptActivity
            startActivity(intent)
            finish()
        }

    }

    // Add the necessary functions for generating a printable document and launching the print dialog
    private fun generatePrintableDocument() {
        // Calculate the height of the receipt layout
        val receiptContainer = findViewById<LinearLayout>(R.id.receiptContainer)
        receiptContainer.measure(
            View.MeasureSpec.makeMeasureSpec(receiptContainer.width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        val measuredHeight = receiptContainer.measuredHeight

        // Create a PdfDocument with dynamic size
        val pageWidth = 58 * 72 / 25.4 // Width of the roll converted to points
        val pageHeight = measuredHeight.toFloat()
        val pageInfo = PdfDocument.PageInfo.Builder(pageWidth.toInt(), pageHeight.toInt(), 1).create()
        val pdfDocument = PdfDocument()

        // Start the page
        val page = pdfDocument.startPage(pageInfo)

        // Draw the content of the receipt layout onto the PDF page
        val canvas = page.canvas
        receiptContainer.draw(canvas)

        // Finish the page
        pdfDocument.finishPage(page)

        // Save or share the PDF as needed
        // For example, save it to external storage
        val file = File(getExternalFilesDir(null), "receipt.pdf")
        try {
            pdfDocument.writeTo(FileOutputStream(file))
            // Optionally, launch an Intent to view or share the generated PDF
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(Uri.fromFile(file), "application/pdf")
            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            startActivity(intent)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            pdfDocument.close()
        }
    }


    private fun launchPrintDialog() {
        // Get the file path of the generated PDF
        val file = File(getExternalFilesDir(null), "receipt.pdf")

        // Create a Uri for the file
        val uri = FileProvider.getUriForFile(this, "${packageName}.fileprovider", file)

        // Create an intent to share the PDF
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "application/pdf"
        intent.putExtra(Intent.EXTRA_STREAM, uri)

        // Grant read permission to the receiving app
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        // Start the intent chooser
        startActivity(Intent.createChooser(intent, "Share PDF using"))
    }

}


