package com.kelompok10.pos

data class TransactionDetail(
    val tDetailId: Int,
    val tHeaderId: Long,
    val productId: Int,
    val saleQuantity: Int,
    val salePrice: Double,
    val subTotal: Double
)

