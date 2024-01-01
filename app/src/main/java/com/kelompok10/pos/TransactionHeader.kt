package com.kelompok10.pos

import java.time.LocalDateTime

data class TransactionHeader(
    val id: Long, // Transaction ID
    val customerId: Int,
    val userId: Int,
    val totalAmount: Double,
    val amountPaid: Double,
    val tip: Double,
    val changes: Double,
    val dateTime: LocalDateTime
)