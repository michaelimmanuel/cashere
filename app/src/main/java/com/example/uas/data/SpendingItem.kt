package com.example.uas.data

import com.squareup.moshi.Json

data class SpendingItem(
    val id: Int,
    val userId: Int,
    val name: String,
    val amount: Double,
    val desc: String,
    val type: String,
    @Json(name = "date")
    var date: String,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String?
)