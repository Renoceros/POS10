package com.kelompok10.pos

data class CartItem(
    val id: Int,
    val name: String,
    val stockQtty: Int,
    var quantity: Int,
    val price: Double,
    var subTot: Double,
)
