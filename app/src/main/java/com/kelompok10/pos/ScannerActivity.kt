package com.kelompok10.pos

import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextClock
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.kelompok10.pos.CONST.REQUEST_CODE_PERMISSIONS
import com.kelompok10.pos.CONST.REQUIRED_PERMISSIONS
import com.kelompok10.pos.databinding.ScannerActivityBinding
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ScannerActivity : AppCompatActivity() {
    private lateinit var pageTitle: TextView
    private lateinit var textClock: TextView
    private lateinit var profileButton: ImageButton
    private lateinit var captureButton: Button
    private lateinit var checkoutButton: Button
    private lateinit var binding: ScannerActivityBinding
    private lateinit var barcodeScanner: BarcodeScanner
    private lateinit var cameraExecutor: ExecutorService
    private val cartItems = mutableListOf<CartItem>()
    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var cartAdapter: CartAdapter

    private var imageCapture: ImageCapture? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scanner_activity)

        pageTitle = findViewById(R.id.Page_Title)
        textClock = findViewById(R.id.textClock)
        profileButton = findViewById(R.id.Profile_Button)
        captureButton = findViewById(R.id.ScanButton)
        checkoutButton = findViewById(R.id.CheckoutButton)
        binding = ScannerActivityBinding.inflate(layoutInflater)

        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_CODE_128)
            .build()
        barcodeScanner = BarcodeScanning.getClient(options)

        cameraExecutor = Executors.newSingleThreadExecutor()

        // Check camera permissions
        if (allPermissionGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        profileButton.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        captureButton.setOnClickListener {
            takePicture()
        }

        checkoutButton.setOnClickListener {
            val intent = Intent(this, CheckoutActivity::class.java)
            intent.putExtra("cartItems", ArrayList(cartItems))
            startActivity(intent)
        }

        // Set up RecyclerView
        cartRecyclerView = findViewById(R.id.recyclerView)
        cartAdapter = CartAdapter(cartItems) { clickedItem ->
            showEditCartPopup(clickedItem)
        }

        cartRecyclerView.layoutManager = LinearLayoutManager(this)
        cartRecyclerView.adapter = cartAdapter
    }

    private fun allPermissionGranted() =
        REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
        }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            imageCapture = ImageCapture.Builder()
                .setTargetRotation(windowManager.defaultDisplay.rotation)
                .build()

            try {
                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )

                preview.setSurfaceProvider(binding.Camera.surfaceProvider)

                val imageAnalysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()

                imageAnalysis.setAnalyzer(cameraExecutor) { image ->
                    // Add your custom image analysis logic if needed
                }

                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis)
            } catch (e: Exception) {
                Log.e(CONST.TAG, "Use case binding failed", e)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePicture() {
        val photoFile = File(externalMediaDirs.first(), "${System.currentTimeMillis()}.jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture?.takePicture(
            outputOptions, cameraExecutor, object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = output.savedUri ?: Uri.fromFile(photoFile)
                    analyzeImage(savedUri)
                    deleteImageFile(photoFile)
                }

                override fun onError(exc: ImageCaptureException) {
                    Log.e(CONST.TAG, "Error capturing image: ${exc.message}", exc)
                }
            }
        )
    }

    private fun deleteImageFile(photoFile: File) {
        if (photoFile.exists()) {
            val deleted = photoFile.delete()
            if (!deleted) {
                Log.e(CONST.TAG, "Failed to delete image file.")
            }
        }
    }

    private fun analyzeImage(imageUri: Uri) {
        val inputImage = InputImage.fromFilePath(this, imageUri)

        barcodeScanner.process(inputImage)
            .addOnSuccessListener { barcodes ->
                for (barcode in barcodes) {
                    handleBarcode(barcode)
                }
            }
            .addOnFailureListener { e ->
                Log.e(CONST.TAG, "Barcode scanning failed: ${e.message}", e)
            }
    }

    private fun handleBarcode(barcode: Barcode) {
        val rawValue = barcode.rawValue
        val format = barcode.format
        Log.d(CONST.TAG, "Barcode raw value: $rawValue, Format: $format")

        if (rawValue != null) {
            val rawValueInt = rawValue.toInt()
            val dbHelper = DatabaseHelper(this) // Replace 'context' with your actual context
            val product = dbHelper.getProductById(rawValueInt)
            Log.d(CONST.TAG, "Product: $product")
            if (product != null) {
                showPopup(product)
                vibrate(100)
            } else {
                showToast("Product not found in the database.")
            }
        }
    }


    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this@ScannerActivity, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
    private fun showPopup(product: Product) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.add_cart_popup)

        val textId = dialog.findViewById<TextView>(R.id.textId)
        val textName = dialog.findViewById<TextView>(R.id.textName)
        val textQuantity = dialog.findViewById<TextView>(R.id.textQuantity)
        val textQuantityValue = dialog.findViewById<TextView>(R.id.textQuantityValue)
        val btnDecrease = dialog.findViewById<Button>(R.id.btnDecrease)
        val btnIncrease = dialog.findViewById<Button>(R.id.btnIncrease)
        val btnAddToCart = dialog.findViewById<Button>(R.id.btnAddToCart)

        textId.text = "ID: ${product.prodId}"
        textName.text = "Name: ${product.name}"
        textQuantity.text = "Quantity: "

        var quantity = 1

        btnDecrease.setOnClickListener {
            if (quantity > 0) {
                quantity--
                textQuantityValue.text = quantity.toString()
            }
        }

        btnIncrease.setOnClickListener {
            if (quantity < product.stockQtty) { // Check if quantity is within available stock
                quantity++
                textQuantityValue.text = quantity.toString()
            } else {
                // Optionally, you can display a message indicating that the stock is limited.
                Toast.makeText(this, "Reached maximum stock limit", Toast.LENGTH_SHORT).show()
            }
        }

        btnAddToCart.setOnClickListener {
            // Calculate subTotal based on the product price and quantity
            val subTotal = product.price * quantity

            // Add the selected item to the cart
            val cartItem = CartItem(product.prodId.toString(), product.name, quantity, product.price, subTotal)

            // Use the addItem method to add the new item to the adapter
            cartAdapter.addItem(cartItem)

            // Optionally, you can display a message or update UI to indicate the item is added to the cart
            Toast.makeText(this, "Added to Cart: ${product.name} (Quantity: $quantity)", Toast.LENGTH_SHORT).show()

            // Dismiss the popup
            dialog.dismiss()
        }

        dialog.show()
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

    private fun vibrate(duration: Long) {
        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator?
        if (vibrator?.hasVibrator() == true) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                // Deprecated in API 26
                vibrator.vibrate(duration)
            }
        }
    }

    private fun getStockQuantityFromDatabase(productId: String): Int {
        // Fetch the product from the database using the productId
        val dbHelper = DatabaseHelper(this)
        val product = dbHelper.getProductById(productId.toInt()) // Assuming productId is an integer
        return product?.stockQtty ?: 0
    }
}
