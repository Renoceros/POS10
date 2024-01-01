package com.kelompok10.pos

data class Purchase(
    val purchaseId: Int,
    val prodId: Int,
    val purQty: Int,
    val purPrice: Double,
    val dateTime: String
)
