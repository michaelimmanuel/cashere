package com.example.uas.data

import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import com.squareup.moshi.Json
import java.time.format.DateTimeParseException

data class SpendingItem(
    val id: Int,
    val userId: Int,
    val name: String,
    val amount: Double,
    val desc: String,
    val type: String,
    @Json(name = "date")
    var date: String, // Updated type to OffsetDateTime
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String?
)