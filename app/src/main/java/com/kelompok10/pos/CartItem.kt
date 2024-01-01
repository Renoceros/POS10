package com.kelompok10.pos

data class CartItem(
    val id: String,
    val name: String,
    var quantity: Int,
    val price: Double,
    val subTot: Double,
)
