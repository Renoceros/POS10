package com.kelompok10.pos

import android.os.Parcel
import android.os.Parcelable

data class User(
    val userId: Int,
    val name: String,
    val password: String,
    val position: String
)
