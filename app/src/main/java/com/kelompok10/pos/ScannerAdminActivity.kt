package com.kelompok10.pos

import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.widget.Button
import android.widget.EditText
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
import com.kelompok10.pos.databinding.ScannerAdminActivityBinding


class ScannerAdminActivity : AppCompatActivity() {

    private lateinit var pageTitle: TextView
    private lateinit var textClock: TextClock
    private lateinit var profileButton: ImageButton
    private lateinit var captureButton: Button
    private lateinit var binding: ScannerAdminActivityBinding
    private lateinit var barcodeScanner: BarcodeScanner
    private lateinit var cameraExecutor: ExecutorService

    private var imageCapture: ImageCapture? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ScannerAdminActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pageTitle = binding.PageTitle
        textClock = binding.textClock
        profileButton = binding.ProfileButton
        captureButton = binding.ScanButton
        profileButton.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_CODE_128)
            .build()
        barcodeScanner = BarcodeScanning.getClient(options)

        cameraExecutor = Executors.newSingleThreadExecutor()

        if (allPermissionGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        captureButton.setOnClickListener {
            takePicture()
        }
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
                }

                override fun onError(exc: ImageCaptureException) {
                    Log.e(CONST.TAG, "Error capturing image: ${exc.message}", exc)
                }
            }
        )
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
            val rawValueint = rawValue.toInt()
            Log.d(CONST.TAG, "Barcode raw value: $rawValueint")
            val dbHelper = DatabaseHelper(this) // Replace 'context' with your actual context
            val product = dbHelper.getProductById(rawValueint)

            Log.d(CONST.TAG, "Product: $product")
            if (product != null) {
                showDataPopup(product.prodId, product.name, product.price, product.stockQtty)
                vibrate(100)
            } else {
                showToast("Product not found in the database. Do you want to add a new Product?")
                showAddPopup()
            }
        }
    }

    private fun vibrate(duration: Long) {
        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator?
        if (vibrator?.hasVibrator() == true) {
            vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }



    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this@ScannerAdminActivity, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
    private fun showDataPopup(id: Int, name: String,price : Double, quantity : Int) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.product_data_popup)

        val textId = dialog.findViewById<TextView>(R.id.textId)
        val textName = dialog.findViewById<TextView>(R.id.textName)
        val textPrice = dialog.findViewById<TextView>(R.id.textPrice)
        val textQuantity = dialog.findViewById<TextView>(R.id.textQuantity)
        val editBttn = dialog.findViewById<Button>(R.id.editBttn)

        textId.text = "ID: $id"
        textName.text = "Name: $name"
        textPrice.text = "Price: $price"
        textQuantity.text = "Quantity: $quantity"

        editBttn.setOnClickListener {
            showEditPopup(id, name, price, quantity)
            dialog.dismiss()
        }

        dialog.show()
    }
    private fun showAddPopup() {
        val addPopupDialog = Dialog(this)
        addPopupDialog.setContentView(R.layout.product_data_add_popup)

        val editTextId = addPopupDialog.findViewById<EditText>(R.id.editTextID)
        val editTextName = addPopupDialog.findViewById<EditText>(R.id.editTextName)
        val editTextPrice = addPopupDialog.findViewById<EditText>(R.id.editTextPrice)
        val btnAdd = addPopupDialog.findViewById<Button>(R.id.addBttn)
        val btnCancel = addPopupDialog.findViewById<Button>(R.id.cancelBttn)

        btnAdd.setOnClickListener {
            // Get the values from the EditText fields
            val prodId = editTextId.text.toString().toInt()
            val name = editTextName.text.toString()
            val price = editTextPrice.text.toString().toDouble()

            // Add the product to the database
            val db = DatabaseHelper(this)
            db.createNewProduct(prodId, name, price, 0) // Assuming initial stock is 0 for simplicity
            addPopupDialog.dismiss()
        }
        btnCancel.setOnClickListener {
            addPopupDialog.dismiss()
        }
        addPopupDialog.show()
    }
    private fun showEditPopup(id: Int, name: String,price : Double, quantity : Int) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.product_data_edit_popup)

        val textId = dialog.findViewById<TextView>(R.id.textId)
        val textName = dialog.findViewById<TextView>(R.id.textName)
        val textQuantity = dialog.findViewById<TextView>(R.id.textQuantity)
        val editID = dialog.findViewById<EditText>(R.id.editTextID)
        val editName = dialog.findViewById<EditText>(R.id.editTextName)
        val editPrice = dialog.findViewById<EditText>(R.id.editTextPrice)
        val textQuantityValue = dialog.findViewById<TextView>(R.id.textQuantityValue)
        val btnDecrease = dialog.findViewById<Button>(R.id.btnDecrease)
        val btnIncrease = dialog.findViewById<Button>(R.id.btnIncrease)
        val btnSave = dialog.findViewById<Button>(R.id.saveBttn)
        val btnDelete = dialog.findViewById<Button>(R.id.deleteBttn)

        editID.setText(id.toString())
        editName.setText(name)
        editPrice.setText(price.toString())
        textQuantityValue.text = quantity.toString()

        var quantityVal = quantity

        btnDecrease.setOnClickListener {
            if (quantityVal > 0) {
                quantityVal--
                textQuantityValue.text = quantityVal.toString()
            }
            if (quantityVal == 0) {
                showConfirmDeletePopup(id,name)
            }
        }
        btnIncrease.setOnClickListener {
            quantityVal++
            textQuantityValue.text = quantityVal.toString()
        }
        btnSave.setOnClickListener {
            try {
                val dbHelper = DatabaseHelper(this)
                dbHelper.saveEditProduct(id, name, price, quantityVal)
                dialog.dismiss()
            } catch (e: Exception) {
                Log.d(CONST.OoH, "Error in btnSave.setOnClickListener: ${e.message}")
            }
        }
        btnDelete.setOnClickListener {try{
            showConfirmDeletePopup(id, name)
            dialog.dismiss()
        }
        catch (e: Exception){
            Log.d(CONST.OoH, "Error in btnDelete.setOnClickListener: ${e.message}")
        }
        }

        dialog.show()
    }
    private fun showConfirmDeletePopup(id: Int, name: String) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.confirm_delete_popup)
        val textDisclaimer = dialog.findViewById<TextView>(R.id.textDisclaimer)
        val btnYes = dialog.findViewById<Button>(R.id.btnYES)
        val btnNo = dialog.findViewById<Button>(R.id.btnNO)

        textDisclaimer.text = "Are you sure you want to delete $name?"

        btnYes.setOnClickListener {
            try {
                val dbHelper = DatabaseHelper(this)
                dbHelper.deleteProduct(id)
                dialog.dismiss()
                Log.d(CONST.OoH, "Product deleted successfully: $id $name")
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e(CONST.OoH, "Error deleting product: $id $name", e)
                // Handle the error or show a toast/message as needed
            }
        }

        btnNo.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }



}

