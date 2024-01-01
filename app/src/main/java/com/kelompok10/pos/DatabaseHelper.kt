package com.kelompok10.pos

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeParseException
import java.util.*

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


    companion object {

            const val DATABASE_NAME = "POSAPPKEL10.db"
            const val DATABASE_VERSION = 1

            // User table columns
            const val USER_TABLE_NAME = "User"
            const val USER_COLUMN_ID = "user_id"
            const val USER_COLUMN_NAME = "name"
            const val USER_COLUMN_PASSWORD = "password"
            const val USER_COLUMN_POSITION = "position"

            // Product table columns
            const val PRODUCT_TABLE_NAME = "Product"
            const val PRODUCT_COLUMN_ID = "prod_id"
            const val PRODUCT_COLUMN_NAME = "name"
            const val PRODUCT_COLUMN_PRICE = "price"
            const val PRODUCT_COLUMN_STOCK_QTTY = "stock_qtty"

            // TransactionHeader table columns
            const val TRANSACTION_HEADER_TABLE_NAME = "TransactionHeader"
            const val TRANSACTION_HEADER_COLUMN_ID = "header_id"
            const val TRANSACTION_HEADER_COLUMN_CUSTOMER_ID = "customer_id"
            const val TRANSACTION_HEADER_COLUMN_USER_ID = "user_id"
            const val TRANSACTION_HEADER_COLUMN_TOTAL_AMOUNT = "total_amount"
            const val TRANSACTION_HEADER_COLUMN_AMOUNT_PAID = "amount_paid"
            const val TRANSACTION_HEADER_COLUMN_TIP = "tip"
            const val TRANSACTION_HEADER_COLUMN_CHANGES = "changes"
            const val TRANSACTION_HEADER_COLUMN_DATE_TIME = "date_time"

            // TransactionDetail table columns
            const val TRANSACTION_DETAIL_TABLE_NAME = "TransactionDetail"
            const val TRANSACTION_DETAIL_COLUMN_ID = "detail_id"
            const val TRANSACTION_DETAIL_COLUMN_TRANSACTION_HEADER_ID = "header_id"
            const val TRANSACTION_DETAIL_COLUMN_PRODUCT_ID = "product_id"
            const val TRANSACTION_DETAIL_COLUMN_SALE_QTTY = "sale_qtty"
            const val TRANSACTION_DETAIL_COLUMN_SALE_PRICE = "sale_price"
            const val TRANSACTION_DETAIL_COLUMN_SUB_TOTAL = "sub_total"

            // Purchase table columns
            const val PURCHASES_TABLE_NAME = "Purchases"
            const val PURCHASES_COLUMN_ID = "purchase_id"
            const val PURCHASES_COLUMN_PROD_ID = "prod_id"
            const val PURCHASES_COLUMN_PUR_QTY = "pur_qty"
            const val PURCHASES_COLUMN_PUR_PRICE = "pur_price"
            const val PURCHASES_COLUMN_DATE_TIME = "date_time"
        }

        override fun onCreate(db: SQLiteDatabase) {
            createTables(db)
            insertInitialData(db)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            // Handle database upgrades if needed
        }

        private fun createTables(db: SQLiteDatabase) {
            // Create Customer Table
            db.execSQL("CREATE TABLE Customer (customer_id TEXT PRIMARY KEY);")

            // Create User Table
            db.execSQL(
                "CREATE TABLE User (" +
                        "user_id INTEGER PRIMARY KEY, " +
                        "name TEXT, " +
                        "password TEXT, " +
                        "position TEXT);"
            )

            // Create Product Table
            db.execSQL(
                "CREATE TABLE Product (" +
                        "prod_id INTEGER PRIMARY KEY, " +
                        "name TEXT, " +
                        "price DECIMAL(10, 2), " +
                        "stock_qtty INTEGER);"
            )

            // Create Transaction Header Table
            db.execSQL(
                "CREATE TABLE TransactionHeader (" +
                        "header_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "customer_id INTEGER, " +
                        "user_id INTEGER, " +
                        "total_amount DECIMAL(10, 2), " +
                        "amount_paid DECIMAL(10, 2), " +
                        "tip DECIMAL(10, 2), " +
                        "changes DECIMAL(10, 2), " +
                        "date_time DATETIME, " +
                        "FOREIGN KEY (customer_id) REFERENCES Customer(customer_id), " +
                        "FOREIGN KEY (user_id) REFERENCES User(user_id));"
            )

            // Create Transaction Detail Table
            db.execSQL(
                "CREATE TABLE TransactionDetail (" +
                        "detail_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "header_id INTEGER, " +
                        "product_id INTEGER, " +
                        "sale_qtty INTEGER, " +
                        "sale_price DECIMAL(10, 2), " +
                        "sub_total DECIMAL(10, 2), " +
                        "FOREIGN KEY (header_id) REFERENCES TransactionHeader(header_id), " +
                        "FOREIGN KEY (product_id) REFERENCES Product(prod_id));"
            )
            db.execSQL("CREATE TABLE Logs (" +
                    "Log_id TEXT PRIMARY KEY," +
                    "Log_msg TEXT);"
            )
            db.execSQL(
                "CREATE TABLE Purchases (" +
                        "purchase_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "prod_id INTEGER, " +
                        "pur_qty INTEGER, " +
                        "pur_price DECIMAL(10, 2), " +
                        "date_time DATETIME, " +
                        "FOREIGN KEY (prod_id) REFERENCES Product(prod_id));"
            )
        }

        private fun insertInitialData(db: SQLiteDatabase) {
            // Insert Users
            db.execSQL(
                "INSERT INTO User (user_id, name, password, position) VALUES " +
                        "(32210013, 'Moreno', '100303', 'Admin'), " +
                        "(32210069, 'Tobby', '100303', 'Cashier'), " +
                        "(32210081, 'Andy', '100303', 'Cashier'), " +
                        "(32210082, 'Ivan', '100303', 'Cashier');"
            )

            // Insert Products
            db.execSQL(
                "INSERT INTO Product (prod_id, name, price, stock_qtty) VALUES " +
                        "(10001, 'Widget A', 100000, 50)," +
                        "(10002, 'Gizmo X', 95000, 30)," +
                        "(10003, 'Thingamajig Y', 3000000, 20)," +
                        "(10004, 'DooHickey Z', 175000, 15)," +
                        "(10005, 'Whatchamacallit P', 35000, 40)," +
                        "(10006, 'Sprocket Q', 70000, 25)," +
                        "(10007, 'Gadget R', 250000, 35)," +
                        "(10008, 'Contraption S', 58000, 10)," +
                        "(10009, 'Apparatus T', 55000, 18)," +
                        "(10010, 'Device U', 83000, 22)," +
                        "(10011, 'UltraWidget 2000', 120000, 50)," +
                        "(10012, 'TurboGizmo Pro', 2300000, 30)," +
                        "(10013, 'MegaThingamajig Deluxe', 349000, 20)," +
                        "(10014, 'SuperDooHickey Advanced', 4500000, 15)," +
                        "(10015, 'PowerWhatchamacallit Plus', 80000, 40)," +
                        "(10016, 'HyperSprocket X', 170000, 25)," +
                        "(10017, 'TechGadget Supreme', 250000, 35)," +
                        "(10018, 'UltimateContraption XL', 390000, 10)," +
                        "(10019, 'MasterApparatus Pro', 55000, 18)," +
                        "(10020, 'EpicDevice 5000', 68000, 22)," +
                        "(10021, 'WhizBang Gizmo', 5000000.00, 15)," +
                        "(10022, 'FunkyDoodle Widget', 1500000.00, 25)," +
                        "(10023, 'ZigZag Thingamajig', 3000000.00, 20)," +
                        "(10024, 'DizzyDooHickey X', 2000000.00, 30)," +
                        "(10025, 'WackyWhatchamacallit', 1000000.00, 40)," +
                        "(10026, 'SillySprocket Y', 6000000.00, 18)," +
                        "(10027, 'CrazyGadget Z', 1200000.00, 22)," +
                        "(10028, 'LoopyContraption', 4000000.00, 12)," +
                        "(10029, 'QuirkyApparatus', 1800000.00, 28)," +
                        "(10030, 'GoofyDevice 3000', 2500000.00, 35);"
            )
        }

    fun authenticateUser(userId: Int, password: String): User? {
        val db = readableDatabase
        var user: User? = null

        // Query the database for the user based on userId
        val cursor = db.query(
            USER_TABLE_NAME,
            arrayOf(USER_COLUMN_ID, USER_COLUMN_NAME, USER_COLUMN_POSITION, USER_COLUMN_PASSWORD),
            "$USER_COLUMN_ID = ?",
            arrayOf("$userId"),
            null,
            null,
            null
        )

        if (cursor.moveToFirst()) {
            // Retrieve user information from the cursor
            val idIndex = cursor.getColumnIndex(USER_COLUMN_ID)
            val nameIndex = cursor.getColumnIndex(USER_COLUMN_NAME)
            val positionIndex = cursor.getColumnIndex(USER_COLUMN_POSITION)
            val passwordIndex = cursor.getColumnIndex(USER_COLUMN_PASSWORD)

            // Log the retrieved user information
            Log.d(CONST.OoH, "Retrieved User: ID=${cursor.getInt(idIndex)}, Name=${cursor.getString(nameIndex)}, Position=${cursor.getString(positionIndex)}")

            // Check if the entered password matches the stored password
            if (cursor.getString(passwordIndex) == password) {
                user = User(
                    userId = cursor.getInt(idIndex),
                    name = cursor.getString(nameIndex),
                    position = cursor.getString(positionIndex),
                    password = cursor.getString(passwordIndex)
                )

                // Log successful authentication
                Log.d(CONST.OoH, "Authentication successful for User: ID=$userId, Name=${cursor.getString(nameIndex)}, Position=${cursor.getString(positionIndex)}")
            } else {
                // Log password mismatch
                Log.d(CONST.OoH, "Authentication failed: Password mismatch for User ID=$userId")
            }
        } else {
            // Log user not found
            Log.d(CONST.OoH, "User not found for ID=$userId")
        }

        cursor.close()


        // Log the result of authentication
        if (user != null) {
            Log.d(CONST.OoH, "Authentication successful. Returning User: ID=${user.userId}, Name=${user.name}, Position=${user.position}")
        } else {
            Log.d(CONST.OoH, "Authentication failed. Returning null.")
        }

        return user
    }

    //CREATE LOGS ====================================================
        private fun getCurrentTimestamp(): String {
            val dateFormat = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
            val currentDateTime = Date()
            return dateFormat.format(currentDateTime).toString()
        }
        fun createLog(logMessage: String) {
            val db = writableDatabase
            val contentValues = ContentValues()

            contentValues.put("Log_id", getCurrentTimestamp())
            contentValues.put("Log_msg", logMessage)

            db.insert("Logs", null, contentValues)

        }
    //CRUD PRODUCT TABLE ==============================================
        fun createNewProduct(prodId: Int, name: String, price: Double, stockQtty: Int) {
            val db = writableDatabase

            val values = ContentValues().apply {
                put(PRODUCT_COLUMN_ID, prodId)
                put(PRODUCT_COLUMN_NAME, name)
                put(PRODUCT_COLUMN_PRICE, price)
                put(PRODUCT_COLUMN_STOCK_QTTY, stockQtty)
            }
            db.insert(PRODUCT_TABLE_NAME, null, values)
            createLog("Product created $prodId $name")

        }
        fun getAllProducts(): List<Product> {
        val db = readableDatabase
        val products = mutableListOf<Product>()

        val cursor = db.query(
            PRODUCT_TABLE_NAME,
            arrayOf(PRODUCT_COLUMN_ID, PRODUCT_COLUMN_NAME, PRODUCT_COLUMN_PRICE, PRODUCT_COLUMN_STOCK_QTTY),
            null,
            null,
            null,
            null,
            null
        )

        while (cursor.moveToNext()) {
            val idIndex = cursor.getColumnIndex(PRODUCT_COLUMN_ID)
            val nameIndex = cursor.getColumnIndex(PRODUCT_COLUMN_NAME)
            val priceIndex = cursor.getColumnIndex(PRODUCT_COLUMN_PRICE)
            val stockQtyIndex = cursor.getColumnIndex(PRODUCT_COLUMN_STOCK_QTTY)

            val product = Product(
                prodId = cursor.getInt(idIndex),
                name = cursor.getString(nameIndex),
                price = cursor.getDouble(priceIndex),
                stockQtty = cursor.getInt(stockQtyIndex)
            )

            products.add(product)
        }

        cursor.close()

        return products
        }
        fun getProductById(prodId: Int): Product? {
            val db = readableDatabase
            var product: Product? = null

            val cursor = db.query(
                PRODUCT_TABLE_NAME,
                arrayOf(
                    PRODUCT_COLUMN_ID,
                    PRODUCT_COLUMN_NAME,
                    PRODUCT_COLUMN_PRICE,
                    PRODUCT_COLUMN_STOCK_QTTY
                ),
                "$PRODUCT_COLUMN_ID = ?",
                arrayOf("$prodId"),
                null,
                null,
                null
            )

            if (cursor.moveToFirst()) {
                val idIndex = cursor.getColumnIndex(PRODUCT_COLUMN_ID)
                val nameIndex = cursor.getColumnIndex(PRODUCT_COLUMN_NAME)
                val priceIndex = cursor.getColumnIndex(PRODUCT_COLUMN_PRICE)
                val stockQtyIndex = cursor.getColumnIndex(PRODUCT_COLUMN_STOCK_QTTY)

                product = Product(
                    prodId = cursor.getInt(idIndex),
                    name = cursor.getString(nameIndex),
                    price = cursor.getDouble(priceIndex),
                    stockQtty = cursor.getInt(stockQtyIndex)
                )
            }

            cursor.close()


            return product
        }
        fun saveEditProduct(prodId: Int, name: String, price: Double, stockQtty: Int): Product? {
        val db = writableDatabase
        val product = getProductById(prodId)

        if (product != null) {
            try {
                val oldStockQty = product.stockQtty

                val values = ContentValues().apply {
                    put(PRODUCT_COLUMN_NAME, name)
                    put(PRODUCT_COLUMN_PRICE, price)
                    put(PRODUCT_COLUMN_STOCK_QTTY, stockQtty)
                }

                db.update(
                    PRODUCT_TABLE_NAME,
                    values,
                    "$PRODUCT_COLUMN_ID = ?",
                    arrayOf(prodId.toString())
                )

                createLog("Product Edited $prodId $name $price $stockQtty")

                // Check if the new quantity is larger than the old quantity
                if (stockQtty > oldStockQty) {
                    // Add entry to the purchases table
                    createPurchase(prodId, stockQtty - oldStockQty, price)
                }
            } catch (e: Exception) {
                Log.d(CONST.OoH, "Error in saveEditProduct: ${e.message}")
            }
        }

        return product
    }
        fun deleteProduct(prodId: Int): Product? {
            Log.d(CONST.OoH, "Attempting to delete Product with ID=$prodId")

            val db = writableDatabase
            val product = getProductById(prodId)

            if (product != null) {
                val productName = product.name

                try {
                    Log.d(CONST.OoH, "Product Found. Deleting Product: ID=$prodId, Name=$productName")

                    db.delete(
                        PRODUCT_TABLE_NAME,
                        "$PRODUCT_COLUMN_ID = ?",
                        arrayOf(prodId.toString())
                    )

                    createLog("Product Deleted: ID=$prodId, Name=$productName")
                } catch (e: Exception) {
                    Log.e(CONST.OoH, "Error in deleteProduct: ${e.message}")
                }
            } else {
                Log.d(CONST.OoH, "Product not found with ID=$prodId")
            }


            return product
        }
    //CRUD USER TABLE =================================================
        fun createNewUser(id:Int,name: String, password: String, position: String): Long {
            val db = writableDatabase
            val contentValues = ContentValues()
            contentValues.put(USER_COLUMN_ID, id)
            contentValues.put(USER_COLUMN_NAME, name)
            contentValues.put(USER_COLUMN_PASSWORD, password)
            contentValues.put(USER_COLUMN_POSITION, position)

            val result = db.insert(USER_TABLE_NAME, null, contentValues)
            createLog("User created $id $name")

            return result
        }
        fun getUserById(userId: Int): User? {
            val db = readableDatabase
            var user: User? = null

            val cursor = db.query(
                USER_TABLE_NAME,
                arrayOf(USER_COLUMN_ID, USER_COLUMN_NAME, USER_COLUMN_POSITION, USER_COLUMN_PASSWORD),
                "$USER_COLUMN_ID = ?",
                arrayOf("$userId"),
                null,
                null,
                null
            )
            if (cursor.moveToFirst()) {
                val idIndex = cursor.getColumnIndex(USER_COLUMN_ID)
                val nameIndex = cursor.getColumnIndex(USER_COLUMN_NAME)
                val positionIndex = cursor.getColumnIndex(USER_COLUMN_POSITION)
                val passwordIndex = cursor.getColumnIndex(USER_COLUMN_PASSWORD)

                user = User(
                    userId = cursor.getInt(idIndex),
                    name = cursor.getString(nameIndex),
                    position = cursor.getString(positionIndex),
                    password = cursor.getString(passwordIndex)
                )
            }

            cursor.close()


            return user
        }
        fun updateUser(userId: Int, name: String, password: String, position: String): Int {
            val db = writableDatabase
            val contentValues = ContentValues()

            contentValues.put(USER_COLUMN_NAME, name)
            contentValues.put(USER_COLUMN_PASSWORD, password)
            contentValues.put(USER_COLUMN_POSITION, position)

            val result = db.update(
                USER_TABLE_NAME,
                contentValues,
                "$USER_COLUMN_ID = ?",
                arrayOf("$userId")
            )
            createLog("User Updated $userId $name $position")

            return result
        }
        fun deleteUser(userId: Int): Int {
            val db = writableDatabase
            val user = getUserById(userId)
            val result = db.delete(
                USER_TABLE_NAME,
                "$USER_COLUMN_ID = ?",
                arrayOf("$userId")
            )

            // Check if the user exists before creating the log
            if (user != null) {
                createLog("Deleted User $userId ${user.name}")
            }

            return result
        }
    //CRUD CUSTOMER TABLE =================================================
        fun addCustomer(): Boolean {
            val db = this.writableDatabase
            val contentValues = ContentValues()

            val currentTimestamp = getCurrentTimestamp()

            contentValues.put("customer_id", currentTimestamp)

            val insertResult = db.insert("Customer", null, contentValues)

            if (insertResult != -1L) {
                // Update ViewModel with the current customer ID
                viewModel.currentCustomerId = currentTimestamp.toString()
                return true
            }

            return false
        }
        private val viewModel = CustomerViewModel()
       fun getCurrentCustomerId(): String? {
        val db = readableDatabase
        var customerId: String? = null

        val cursor = db.query(
            "Customer",
            arrayOf("customer_id"),
            null,
            null,
            null,
            null,
            null
        )

        if (cursor.moveToLast()) {
            val customerIdIndex = cursor.getColumnIndex("customer_id")
            customerId = cursor.getString(customerIdIndex)
        }

        cursor.close()

        return customerId
    }
    //CRUD PURCHASE TABLE =================================================
        fun createPurchase(prodId: Int, purQty: Int, purPrice: Double) {
            val db = writableDatabase

            val values = ContentValues().apply {
                put(PURCHASES_COLUMN_PROD_ID, prodId)
                put(PURCHASES_COLUMN_PUR_QTY, purQty)
                put(PURCHASES_COLUMN_PUR_PRICE, purPrice)
                put(PURCHASES_COLUMN_DATE_TIME, getCurrentTimestamp())
            }

            db.insert(PURCHASES_TABLE_NAME, null, values)
            createLog("Purchase Created: Product ID=$prodId, Quantity=$purQty, Price=$purPrice")

        }
        fun getAllPurchases(): List<Purchase> {
            val db = readableDatabase
            val purchases = mutableListOf<Purchase>()

            val cursor = db.query(
                PURCHASES_TABLE_NAME,
                arrayOf(PURCHASES_COLUMN_ID, PURCHASES_COLUMN_PROD_ID, PURCHASES_COLUMN_PUR_QTY, PURCHASES_COLUMN_PUR_PRICE, PURCHASES_COLUMN_DATE_TIME),
                null,
                null,
                null,
                null,
                null
            )

            while (cursor.moveToNext()) {
                val purchaseIdColumnIndex = cursor.getColumnIndex(PURCHASES_COLUMN_ID)
                val prodIdColumnIndex = cursor.getColumnIndex(PURCHASES_COLUMN_PROD_ID)
                val purQtyColumnIndex = cursor.getColumnIndex(PURCHASES_COLUMN_PUR_QTY)
                val purPriceColumnIndex = cursor.getColumnIndex(PURCHASES_COLUMN_PUR_PRICE)
                val dateTimeColumnIndex = cursor.getColumnIndex(PURCHASES_COLUMN_DATE_TIME)

                val purchaseId = if (purchaseIdColumnIndex != -1) cursor.getInt(purchaseIdColumnIndex) else 0
                val prodId = if (prodIdColumnIndex != -1) cursor.getInt(prodIdColumnIndex) else 0
                val purQty = if (purQtyColumnIndex != -1) cursor.getInt(purQtyColumnIndex) else 0
                val purPrice = if (purPriceColumnIndex != -1) cursor.getDouble(purPriceColumnIndex) else 0.0
                val dateTime = if (dateTimeColumnIndex != -1) cursor.getString(dateTimeColumnIndex) else ""

                val purchase = Purchase(purchaseId, prodId, purQty, purPrice, dateTime)
                purchases.add(purchase)
            }

            cursor.close()


            return purchases
        }
        fun getPurchaseById(purchaseId: Int): Purchase? {
            val db = readableDatabase
            var purchase: Purchase? = null
            val purchases = mutableListOf<Purchase>()

            val cursor = db.query(
                PURCHASES_TABLE_NAME,
                arrayOf(PURCHASES_COLUMN_PROD_ID, PURCHASES_COLUMN_PUR_QTY, PURCHASES_COLUMN_PUR_PRICE, PURCHASES_COLUMN_DATE_TIME),
                "$PURCHASES_COLUMN_ID = ?",
                arrayOf("$purchaseId"),
                null,
                null,
                null
            )

            while (cursor.moveToNext()) {
                val purchaseIdColumnIndex = cursor.getColumnIndex(PURCHASES_COLUMN_ID)
                val prodIdColumnIndex = cursor.getColumnIndex(PURCHASES_COLUMN_PROD_ID)
                val purQtyColumnIndex = cursor.getColumnIndex(PURCHASES_COLUMN_PUR_QTY)
                val purPriceColumnIndex = cursor.getColumnIndex(PURCHASES_COLUMN_PUR_PRICE)
                val dateTimeColumnIndex = cursor.getColumnIndex(PURCHASES_COLUMN_DATE_TIME)

                val purchaseId = if (purchaseIdColumnIndex != -1) cursor.getInt(purchaseIdColumnIndex) else 0
                val prodId = if (prodIdColumnIndex != -1) cursor.getInt(prodIdColumnIndex) else 0
                val purQty = if (purQtyColumnIndex != -1) cursor.getInt(purQtyColumnIndex) else 0
                val purPrice = if (purPriceColumnIndex != -1) cursor.getDouble(purPriceColumnIndex) else 0.0
                val dateTime = if (dateTimeColumnIndex != -1) cursor.getString(dateTimeColumnIndex) else ""

                val purchase = Purchase(purchaseId, prodId, purQty, purPrice, dateTime)
                purchases.add(purchase)
            }


            cursor.close()


            return purchase
        }
        fun updatePurchase(purchaseId: Int, purQty: Int, purPrice: Double) {
            val db = writableDatabase

            val values = ContentValues().apply {
                put(PURCHASES_COLUMN_PUR_QTY, purQty)
                put(PURCHASES_COLUMN_PUR_PRICE, purPrice)
                put(PURCHASES_COLUMN_DATE_TIME, getCurrentTimestamp())
            }

            db.update(
                PURCHASES_TABLE_NAME,
                values,
                "$PURCHASES_COLUMN_ID = ?",
                arrayOf("$purchaseId")
            )

            createLog("Purchase Updated: Purchase ID=$purchaseId, Quantity=$purQty, Price=$purPrice")

        }
        fun deletePurchase(purchaseId: Int) {
            val db = writableDatabase
            db.delete(
                PURCHASES_TABLE_NAME,
                "$PURCHASES_COLUMN_ID = ?",
                arrayOf("$purchaseId")
            )
            createLog("Purchase Deleted: Purchase ID=$purchaseId")

        }
    //CRUD TRANSACTION TABLE =================================================
        //CRUD operations for TransactionHeader
        fun addTransactionHeader(transactionHeader: TransactionHeader): Long {
            val db = this.writableDatabase
            val values = ContentValues()
            values.put(TRANSACTION_HEADER_COLUMN_CUSTOMER_ID, transactionHeader.customerId)
            values.put(TRANSACTION_HEADER_COLUMN_USER_ID, transactionHeader.userId)
            values.put(TRANSACTION_HEADER_COLUMN_TOTAL_AMOUNT, transactionHeader.totalAmount)
            values.put(TRANSACTION_HEADER_COLUMN_AMOUNT_PAID, transactionHeader.amountPaid)
            values.put(TRANSACTION_HEADER_COLUMN_TIP, transactionHeader.tip)
            values.put(TRANSACTION_HEADER_COLUMN_CHANGES, transactionHeader.changes)
            values.put(TRANSACTION_HEADER_COLUMN_DATE_TIME, transactionHeader.dateTime.toString())

            // Inserting Row
            val id = db.insert(TRANSACTION_HEADER_TABLE_NAME, null, values)
            return id
        }
        fun getAllTransactionHeaders(): List<TransactionHeader> {
            val headers = mutableListOf<TransactionHeader>()
            val db = readableDatabase

            val cursor = db.query(
                TRANSACTION_HEADER_TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
            )

            while (cursor.moveToNext()) {
                val tHeaderIdIndex = cursor.getColumnIndex(TRANSACTION_HEADER_COLUMN_ID)
                val customerIdIndex = cursor.getColumnIndex(TRANSACTION_HEADER_COLUMN_CUSTOMER_ID)
                val userIdIndex = cursor.getColumnIndex(TRANSACTION_HEADER_COLUMN_USER_ID)
                val totalAmountIndex = cursor.getColumnIndex(TRANSACTION_HEADER_COLUMN_TOTAL_AMOUNT)
                val amountPaidIndex = cursor.getColumnIndex(TRANSACTION_HEADER_COLUMN_AMOUNT_PAID)
                val tipIndex = cursor.getColumnIndex(TRANSACTION_HEADER_COLUMN_TIP)
                val changesIndex = cursor.getColumnIndex(TRANSACTION_HEADER_COLUMN_CHANGES)
                val dateTimeIndex = cursor.getColumnIndex(TRANSACTION_HEADER_COLUMN_DATE_TIME)

                // Check if the column index is valid before retrieving the value
                val tHeaderId = if (tHeaderIdIndex != -1) cursor.getLong(tHeaderIdIndex) else 0L
                val customerId = if (customerIdIndex != -1) cursor.getInt(customerIdIndex) else 0
                val userId = if (userIdIndex != -1) cursor.getInt(userIdIndex) else 0
                val totalAmount = if (totalAmountIndex != -1) cursor.getDouble(totalAmountIndex) else 0.0
                val amountPaid = if (amountPaidIndex != -1) cursor.getDouble(amountPaidIndex) else 0.0
                val tip = if (tipIndex != -1) cursor.getDouble(tipIndex) else 0.0
                val changes = if (changesIndex != -1) cursor.getDouble(changesIndex) else 0.0
                val dateTimeString = if (dateTimeIndex != -1) cursor.getString(dateTimeIndex) else ""


                val dateTime = try {
                    LocalDateTime.parse(dateTimeString)
                } catch (e: DateTimeParseException) {
                    LocalDateTime.now()
                }

                val transactionHeader = TransactionHeader(
                    tHeaderId,
                    customerId,
                    userId,
                    totalAmount,
                    amountPaid,
                    tip,
                    changes,
                    dateTime
                )

                headers.add(transactionHeader)
            }

            cursor.close()
            return headers
        }
        fun getTransactionHeaderById(tHeaderId: Long): TransactionHeader? {
            val db = this.readableDatabase
            val cursor = db.query(
                TRANSACTION_HEADER_TABLE_NAME,
                null,
                "$TRANSACTION_HEADER_COLUMN_ID = ?",
                arrayOf(tHeaderId.toString()),
                null, null, null, null
            )

            return if (cursor.moveToFirst()) {
                val customerIdIndex = cursor.getColumnIndex(TRANSACTION_HEADER_COLUMN_CUSTOMER_ID)
                val userIdIndex = cursor.getColumnIndex(TRANSACTION_HEADER_COLUMN_USER_ID)
                val totalAmountIndex = cursor.getColumnIndex(TRANSACTION_HEADER_COLUMN_TOTAL_AMOUNT)
                val amountPaidIndex = cursor.getColumnIndex(TRANSACTION_HEADER_COLUMN_AMOUNT_PAID)
                val tipIndex = cursor.getColumnIndex(TRANSACTION_HEADER_COLUMN_TIP)
                val changesIndex = cursor.getColumnIndex(TRANSACTION_HEADER_COLUMN_CHANGES)
                val dateTimeIndex = cursor.getColumnIndex(TRANSACTION_HEADER_COLUMN_DATE_TIME)

                // Check if the column index is valid before retrieving the value
                val customerId = if (customerIdIndex != -1) cursor.getInt(customerIdIndex) else 0
                val userId = if (userIdIndex != -1) cursor.getInt(userIdIndex) else 0
                val totalAmount = if (totalAmountIndex != -1) cursor.getDouble(totalAmountIndex) else 0.0
                val amountPaid = if (amountPaidIndex != -1) cursor.getDouble(amountPaidIndex) else 0.0
                val tip = if (tipIndex != -1) cursor.getDouble(tipIndex) else 0.0
                val changes = if (changesIndex != -1) cursor.getDouble(changesIndex) else 0.0
                val dateTimeString = if (dateTimeIndex != -1) cursor.getString(dateTimeIndex) else ""

                // Validate and parse dateTimeString to LocalDateTime
                val dateTime = if (!dateTimeString.isNullOrEmpty()) {
                    LocalDateTime.parse(dateTimeString)
                } else {
                    // Handle the case when dateTimeString is empty or null
                    LocalDateTime.now() // Provide a default value or handle as needed
                }

                TransactionHeader(
                    tHeaderId,
                    customerId,
                    userId,
                    totalAmount,
                    amountPaid,
                    tip,
                    changes,
                    dateTime
                )
            }else {
                null
            }.also {
                cursor.close()
            }
        }
        fun getTransactionHeader(tHeaderId: Long): TransactionHeader? {
            val db = this.readableDatabase
            val cursor = db.query(
                TRANSACTION_HEADER_TABLE_NAME,
                null,
                "$TRANSACTION_HEADER_COLUMN_ID = ?",
                arrayOf(tHeaderId.toString()),
                null, null, null, null
            )
            var transactionHeader: TransactionHeader? = null

            if (cursor.moveToFirst()) {
                val customerIdIndex = cursor.getColumnIndex(TRANSACTION_HEADER_COLUMN_CUSTOMER_ID)
                val userIdIndex = cursor.getColumnIndex(TRANSACTION_HEADER_COLUMN_USER_ID)
                val totalAmountIndex = cursor.getColumnIndex(TRANSACTION_HEADER_COLUMN_TOTAL_AMOUNT)
                val amountPaidIndex = cursor.getColumnIndex(TRANSACTION_HEADER_COLUMN_AMOUNT_PAID)
                val tipIndex = cursor.getColumnIndex(TRANSACTION_HEADER_COLUMN_TIP)
                val changesIndex = cursor.getColumnIndex(TRANSACTION_HEADER_COLUMN_CHANGES)
                val dateTimeIndex = cursor.getColumnIndex(TRANSACTION_HEADER_COLUMN_DATE_TIME)

                // Validate and retrieve values
                val customerId = if (customerIdIndex != -1) maxOf(0, cursor.getInt(customerIdIndex)) else 0
                val userId = if (userIdIndex != -1) maxOf(0, cursor.getInt(userIdIndex)) else 0
                val totalAmount = if (totalAmountIndex != -1) maxOf(0.0, cursor.getDouble(totalAmountIndex)) else 0.0
                val amountPaid = if (amountPaidIndex != -1) maxOf(0.0, cursor.getDouble(amountPaidIndex)) else 0.0
                val tip = if (tipIndex != -1) maxOf(0.0, cursor.getDouble(tipIndex)) else 0.0
                val changes = if (changesIndex != -1) maxOf(0.0, cursor.getDouble(changesIndex)) else 0.0
                val dateTimeString = if (dateTimeIndex != -1) cursor.getString(dateTimeIndex) else ""

                // Validate and parse dateTimeString to LocalDateTime
                val dateTime = if (!dateTimeString.isNullOrEmpty()) {
                    try {
                        LocalDateTime.parse(dateTimeString)
                    } catch (e: DateTimeParseException) {
                        // Handle the case when dateTimeString is not a valid LocalDateTime
                        LocalDateTime.now() // Provide a default value or handle as needed
                    }
                } else {
                    // Handle the case when dateTimeString is empty or null
                    LocalDateTime.now() // Provide a default value or handle as needed
                }

                transactionHeader = TransactionHeader(
                    tHeaderId,
                    customerId,
                    userId,
                    totalAmount,
                    amountPaid,
                    tip,
                    changes,
                    dateTime
                )
            }

            cursor.close()
            return transactionHeader
        }
        fun updateTransactionHeader(transactionHeader: TransactionHeader): Int {
            val db = this.writableDatabase
            val values = ContentValues()
            values.put(TRANSACTION_HEADER_COLUMN_CUSTOMER_ID, transactionHeader.customerId)
            values.put(TRANSACTION_HEADER_COLUMN_USER_ID, transactionHeader.userId)
            values.put(TRANSACTION_HEADER_COLUMN_TOTAL_AMOUNT, transactionHeader.totalAmount)
            values.put(TRANSACTION_HEADER_COLUMN_AMOUNT_PAID, transactionHeader.amountPaid)
            values.put(TRANSACTION_HEADER_COLUMN_TIP, transactionHeader.tip)
            values.put(TRANSACTION_HEADER_COLUMN_CHANGES, transactionHeader.changes)
            values.put(TRANSACTION_HEADER_COLUMN_DATE_TIME, transactionHeader.dateTime.toString())

            // Updating Row
            val result = db.update(
                TRANSACTION_HEADER_TABLE_NAME,
                values,
                "$TRANSACTION_HEADER_COLUMN_ID = ?",
                arrayOf(transactionHeader.id.toString())
            )
            return result
        }
        fun deleteTransactionHeader(tHeaderId: Long): Int {
            val db = this.writableDatabase
            val result = db.delete(
                TRANSACTION_HEADER_TABLE_NAME,
                "$TRANSACTION_HEADER_COLUMN_ID = ?",
                arrayOf(tHeaderId.toString())
            )
            return result
        }
        // CRUD operations for TransactionDetail
        fun addTransactionDetail(transactionDetail: TransactionDetail): Long {
            val db = this.writableDatabase
            val values = ContentValues()
            values.put(TRANSACTION_DETAIL_COLUMN_TRANSACTION_HEADER_ID, transactionDetail.tHeaderId)
            values.put(TRANSACTION_DETAIL_COLUMN_PRODUCT_ID, transactionDetail.productId)
            values.put(TRANSACTION_DETAIL_COLUMN_SALE_QTTY, transactionDetail.saleQuantity)
            values.put(TRANSACTION_DETAIL_COLUMN_SALE_PRICE, transactionDetail.salePrice)
            values.put(TRANSACTION_DETAIL_COLUMN_SUB_TOTAL, transactionDetail.subTotal)

            // Inserting Row
            val id = db.insert(TRANSACTION_DETAIL_TABLE_NAME, null, values)
            return id
        }
        fun getAllTransactionDetails(tHeaderId: Long): List<TransactionDetail> {
        val details = mutableListOf<TransactionDetail>()
        val db = readableDatabase

        val cursor = db.query(
            TRANSACTION_DETAIL_TABLE_NAME,
            null,
            "$TRANSACTION_DETAIL_COLUMN_TRANSACTION_HEADER_ID = ?",
            arrayOf(tHeaderId.toString()),
            null,
            null,
            null
        )

        while (cursor.moveToNext()) {
            val detailIdIndex = cursor.getColumnIndex(TRANSACTION_DETAIL_COLUMN_ID)
            val productIdIndex = cursor.getColumnIndex(TRANSACTION_DETAIL_COLUMN_PRODUCT_ID)
            val saleQuantityIndex = cursor.getColumnIndex(TRANSACTION_DETAIL_COLUMN_SALE_QTTY)
            val salePriceIndex = cursor.getColumnIndex(TRANSACTION_DETAIL_COLUMN_SALE_PRICE)
            val subTotalIndex = cursor.getColumnIndex(TRANSACTION_DETAIL_COLUMN_SUB_TOTAL)

            // Check if the column index is valid before retrieving the value
            val detailId = if (detailIdIndex != -1) cursor.getInt(detailIdIndex) else 0L
            val productId = if (productIdIndex != -1) cursor.getInt(productIdIndex) else 0
            val saleQuantity = if (saleQuantityIndex != -1) cursor.getInt(saleQuantityIndex) else 0
            val salePrice = if (salePriceIndex != -1) cursor.getDouble(salePriceIndex) else 0.0
            val subTotal = if (subTotalIndex != -1) cursor.getDouble(subTotalIndex) else 0.0

            val transactionDetail = TransactionDetail(
                detailId.toInt(),
                tHeaderId,
                productId,
                saleQuantity,
                salePrice,
                subTotal
            )

            details.add(transactionDetail)
        }

        cursor.close()
        return details
    }
        fun getTransactionDetailById(tDetailId: Long): TransactionDetail? {
            val db = this.readableDatabase
            val cursor = db.query(
                TRANSACTION_DETAIL_TABLE_NAME,
                null,
                "$TRANSACTION_DETAIL_COLUMN_ID = ?",
                arrayOf(tDetailId.toString()),
                null, null, null, null
            )

            return if (cursor.moveToFirst()) {
                val tDetailIdIndex = cursor.getColumnIndex(TRANSACTION_DETAIL_COLUMN_ID)
                val tHeaderIdIndex = cursor.getColumnIndex(TRANSACTION_DETAIL_COLUMN_TRANSACTION_HEADER_ID)
                val productIdIndex = cursor.getColumnIndex(TRANSACTION_DETAIL_COLUMN_PRODUCT_ID)
                val saleQuantityIndex = cursor.getColumnIndex(TRANSACTION_DETAIL_COLUMN_SALE_QTTY)
                val salePriceIndex = cursor.getColumnIndex(TRANSACTION_DETAIL_COLUMN_SALE_PRICE)
                val subTotalIndex = cursor.getColumnIndex(TRANSACTION_DETAIL_COLUMN_SUB_TOTAL)

                // Validate and retrieve values
                val tDetailId = if (tDetailIdIndex != -1) maxOf(0, cursor.getInt(tDetailIdIndex)) else 0
                val tHeaderId = if (tHeaderIdIndex != -1) maxOf(0, cursor.getInt(tHeaderIdIndex)) else 0
                val productId = if (productIdIndex != -1) maxOf(0, cursor.getInt(productIdIndex)) else 0
                val saleQuantity = if (saleQuantityIndex != -1) maxOf(0, cursor.getInt(saleQuantityIndex)) else 0
                val salePrice = if (salePriceIndex != -1) maxOf(0.0, cursor.getDouble(salePriceIndex)) else 0.0
                val subTotal = if (subTotalIndex != -1) maxOf(0.0, cursor.getDouble(subTotalIndex)) else 0.0

                TransactionDetail(
                    tDetailId,
                    tHeaderId.toLong(),
                    productId,
                    saleQuantity,
                    salePrice,
                    subTotal
                )
            } else {
                null
            }.also {
                cursor.close()
            }
        }
        fun getTransactionDetail(tHeaderId: Long): List<TransactionDetail> {
            val db = this.readableDatabase
            val cursor = db.query(
                TRANSACTION_DETAIL_TABLE_NAME,
                null,
                "$TRANSACTION_DETAIL_COLUMN_TRANSACTION_HEADER_ID = ?",
                arrayOf(tHeaderId.toString()),
                null, null, null, null
            )
            val transactionDetails = mutableListOf<TransactionDetail>()
            while (cursor.moveToNext()) {
                val tDetailId = cursor.getColumnIndex(TRANSACTION_DETAIL_COLUMN_ID)
                val productIdIndex = cursor.getColumnIndex(TRANSACTION_DETAIL_COLUMN_PRODUCT_ID)
                val saleQuantityIndex = cursor.getColumnIndex(TRANSACTION_DETAIL_COLUMN_SALE_QTTY)
                val salePriceIndex = cursor.getColumnIndex(TRANSACTION_DETAIL_COLUMN_SALE_PRICE)
                val subTotalIndex = cursor.getColumnIndex(TRANSACTION_DETAIL_COLUMN_SUB_TOTAL)

                // Validate and retrieve values
                //val tDetailId = if()
                val productId = if (productIdIndex != -1) maxOf(0, cursor.getInt(productIdIndex)) else 0
                val saleQuantity = if (saleQuantityIndex != -1) maxOf(0, cursor.getInt(saleQuantityIndex)) else 0
                val salePrice = if (salePriceIndex != -1) maxOf(0.0, cursor.getDouble(salePriceIndex)) else 0.0
                val subTotal = if (subTotalIndex != -1) maxOf(0.0, cursor.getDouble(subTotalIndex)) else 0.0


                transactionDetails.add(
                    TransactionDetail(
                        tDetailId,
                        tHeaderId,
                        productId,
                        saleQuantity,
                        salePrice,
                        subTotal
                    )
                )
            }
            cursor.close()
            return transactionDetails
        }
        fun updateTransactionDetail(transactionDetail: TransactionDetail): Int {
            val db = this.writableDatabase
            val values = ContentValues()
            values.put(TRANSACTION_DETAIL_COLUMN_TRANSACTION_HEADER_ID, transactionDetail.tHeaderId)
            values.put(TRANSACTION_DETAIL_COLUMN_PRODUCT_ID, transactionDetail.productId)
            values.put(TRANSACTION_DETAIL_COLUMN_SALE_QTTY, transactionDetail.saleQuantity)
            values.put(TRANSACTION_DETAIL_COLUMN_SALE_PRICE, transactionDetail.salePrice)
            values.put(TRANSACTION_DETAIL_COLUMN_SUB_TOTAL, transactionDetail.subTotal)

            // Updating Row
            val result = db.update(
                TRANSACTION_DETAIL_TABLE_NAME,
                values,
                "$TRANSACTION_DETAIL_COLUMN_ID = ?",
                arrayOf(transactionDetail.tDetailId.toString())
            )
            return result
        }
        fun deleteTransactionDetail(transactionDetailId: Long): Int {
            val db = this.writableDatabase
            val result = db.delete(
                TRANSACTION_DETAIL_TABLE_NAME,
                "$TRANSACTION_DETAIL_COLUMN_ID = ?",
                arrayOf(transactionDetailId.toString())
            )
            return result
        }
}